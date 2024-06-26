package dev.buildtool.scp.human;

import dev.buildtool.satako.Constants;
import dev.buildtool.satako.UniqueList;
import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.goals.*;
import io.netty.buffer.Unpooled;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class Human extends SCPEntity implements IRangedAttackMob, ICrossbowUser, INamedContainerProvider {
    protected static final DataParameter<Integer> VARIANT = EntityDataManager.defineId(Human.class, DataSerializers.INT);
    protected static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.defineId(Human.class, DataSerializers.OPTIONAL_UUID);

    public Human(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
        setCanPickUpLoot(true);
        GroundPathNavigator groundPathNavigator = (GroundPathNavigator) getNavigation();
        groundPathNavigator.setCanOpenDoors(true);
    }

    public HurtByTargetGoal defend;
    protected GoalAction activeCommand = GoalAction.IDLE;
    protected Follow<Human, PlayerEntity> follow;
    protected GuardPosition<Human, MobEntity> guardPosition;
    protected Protect<Human, PlayerEntity> protectOwner;
    protected Assist<Human, PlayerEntity> assistPlayer;
    protected ProtectAndAssist<Human, PlayerEntity> protectAndAssist;
    public UniqueList<NamedGoal> namedGoals;

    @Override
    protected void registerGoals() {
        super.registerGoals();
        namedGoals = new UniqueList<>();
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(1, new BowAttack<>(this, 1, 13, 50));
        goalSelector.addGoal(2, new CrossbowAttack<>(this, 1, 50));
        goalSelector.addGoal(3, new TridentAttack(this, 1, 40, 7));
        goalSelector.addGoal(5, new BetterMeleeAttackGoal(this, 1, true, 20));
        goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(7, defend = new RevengeGoal(this, livingEntity -> livingEntity.getClass() == getClass() || livingEntity.getUUID().equals(getOwner())));
        goalSelector.addGoal(8, new OpenDoorGoal(this, true));
        goalSelector.addGoal(4, new AvoidEntityGoal<>(this, CreeperEntity.class, 5, 1, 1.5));
        goalSelector.addGoal(9, follow = new Follow<>(this, PlayerEntity.class, playerEntity -> playerEntity.getUUID().equals(getOwner())));
        Predicate<MobEntity> filter = mobEntity -> {
            if (mobEntity instanceof CreeperEntity) {
                return isHolding(item -> item instanceof TridentItem) || isHolding(item -> item instanceof BowItem) || isHolding(item -> item instanceof CrossbowItem);
            }
            if (mobEntity instanceof ChaosInsurgencySoldier)
                return true;
            return mobEntity instanceof IMob;
        };
        goalSelector.addGoal(10, guardPosition = new GuardPosition<>(this, new Class[]{MobEntity.class}, filter));
        goalSelector.addGoal(11, protectOwner = new Protect<>(this, PlayerEntity.class, playerEntity -> playerEntity.getUUID().equals(getOwner())));
        goalSelector.addGoal(12, assistPlayer = new Assist<>(this, PlayerEntity.class, playerEntity -> playerEntity.getUUID().equals(getOwner())));
        goalSelector.addGoal(13, protectAndAssist = new ProtectAndAssist<>(this, playerEntity -> playerEntity.getUUID().equals(getOwner()), PlayerEntity.class));
        goalSelector.addGoal(14, new LookRandomlyGoal(this));

        goalSelector.addGoal(15, new LookAtGoal(this, LivingEntity.class, 32));

        namedGoals.add(follow);
        namedGoals.add(guardPosition);
        namedGoals.add(protectOwner);
        namedGoals.add(assistPlayer);
        namedGoals.add(protectAndAssist);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        lookAt(target, 360, 180);
        ProjectileEntity projectileEntity = null;
        if (isHolding(item -> item instanceof BowItem || item instanceof CrossbowItem))
            projectileEntity = new ArrowEntity(level, this);
        else if (isHolding(item -> item instanceof TridentItem))
            projectileEntity = new TridentEntity(level, this, ItemStack.EMPTY);
        if (projectileEntity != null) {
            double dx = target.getX() - getX();
            double dz = target.getZ() - getZ();
            double dy = target.getY(0.333) - projectileEntity.getY();
            double sqrrt = Math.sqrt(dx * dx + dz * dz);
            projectileEntity.shoot(dx, dy + sqrrt * 0.2, dz, 1.6f, 0);
            playSound(SoundEvents.SKELETON_SHOOT, 1, random.nextFloat());
            level.addFreshEntity(projectileEntity);
        }
    }

    //TODO check
    @Override
    protected void blockUsingShield(LivingEntity entityIn) {
        super.blockUsingShield(entityIn);
        if (entityIn.getMainHandItem().getItem().canDisableShield(entityIn.getMainHandItem(), this.getOffhandItem(), this, entityIn)) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            f += 0.75F;
            if (this.random.nextFloat() < f) {
                this.blockedByShield(entityIn);
                this.level.broadcastEntityEvent(this, (byte) 30);
            }
        }
    }

    //works
    @Override
    public boolean isBlocking() {
        ItemStack off = getOffhandItem();
        if (off.getItem().isShield(off, this)) {
            boolean blocking = random.nextBoolean();
            if (blocking)
                playSound(SoundEvents.SHIELD_BLOCK, 1, 1);
            return blocking;
        }
        return super.isBlocking();
    }

    //TODO check
    @Override
    protected void blockedByShield(LivingEntity p_213371_1_) {
        super.blockedByShield(p_213371_1_);
    }

    //TODO check
    @Override
    protected void hurtCurrentlyUsedShield(float damage) {
        if (damage >= 3.0F && this.getOffhandItem().getItem().isShield(this.getOffhandItem(), this)) {
            int i = 1 + MathHelper.floor(damage);
            this.getOffhandItem().hurtAndBreak(i, this, human -> human.broadcastBreakEvent(getUsedItemHand()));

            if (this.getOffhandItem().isEmpty()) {
                Hand enumhand = this.getUsedItemHand();
                if (enumhand == Hand.MAIN_HAND) {
                    this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                }
                this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
            } else {
                playSound(SoundEvents.SHIELD_BLOCK, 1, 1);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(VARIANT, 0);
        getEntityData().define(OWNER, Optional.empty());
    }



    public int getVariant() {
        return getEntityData().get(VARIANT);
    }

    public void setVariant(int i) {
        getEntityData().set(VARIANT, i);
    }

    public GoalAction getActiveCommand() {
        return activeCommand;
    }

    public void setActiveCommand(GoalAction activeCommand) {
        this.activeCommand = activeCommand;
    }

    public UUID getOwner() {
        if (getEntityData().get(OWNER).isPresent())
            return getEntityData().get(OWNER).get();
        return null;
    }

    public void setOwner(UUID owner) {
        getEntityData().set(OWNER, Optional.of(owner));
    }

    public boolean hasOwner() {
        return getEntityData().get(OWNER).isPresent();
    }

    @Override
    public void setChargingCrossbow(boolean isCharging) {
        //visual feedback
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Current goal"))
            activeCommand = GoalAction.valueOf(compound.getString("Current goal"));
        if (compound.contains("Owner")) {
            UUID owner = compound.getUUID("Owner");
            if (!owner.equals(Constants.NULL_UUID))
                setOwner(owner);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (hasOwner())
            compound.putUUID("Owner", getOwner());
        compound.putString("Current goal", activeCommand.name());
    }

    /**
     * Shoot crossbow
     */
    @Override
    public void shootCrossbowProjectile(LivingEntity target, ItemStack p_230284_2_, ProjectileEntity projectile, float multiShotSpray) {
        double d = target.getX() - this.getX();
        double e = target.getZ() - this.getZ();
        double f = MathHelper.sqrt(d * d + e * e);
        double g = target.getY(0.3333333333333333D) - projectile.getY() + f * 0.20000000298023224D;
        Vector3f vector3f = this.getProjectileShotVector(target, new Vector3d((float) d, (float) g, (float) e), multiShotSpray);
        projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 1.6F, (float) (14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public void onCrossbowAttackPerformed() {
        noActionTime = 0;
    }

    @Override
    public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.getItem().isEdible() && getHealth() < getMaxHealth()) {
            this.heal(itemInHand.getItem().getFoodProperties().getNutrition() / 2f);
            itemInHand.getItem().getFoodProperties().getEffects().forEach(effectInstanceFloatPair -> {
                if (random.nextFloat() < effectInstanceFloatPair.getSecond())
                    addEffect(effectInstanceFloatPair.getFirst());
            });
            itemInHand.shrink(1);
            level.playSound(player, this, SoundEvents.GENERIC_EAT, getSoundSource(), 1, 1);
        } else {
            if (level instanceof ServerWorld)
                NetworkHooks.openGui((ServerPlayerEntity) player, this, packetBuffer -> packetBuffer.writeInt(getId()));
        }
        return ActionResultType.SUCCESS;
    }


    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeInt(getId());
        return new InteractionContainer(p_createMenu_1_, p_createMenu_2_, packetBuffer);
    }

    @Override
    public void tick() {
        super.tick();
        if (namedGoals != null) {
            if (getActiveCommand() == GoalAction.IDLE) {
                namedGoals.forEach(NamedGoal::turnOff);
            } else {
                for (NamedGoal namedGoal : namedGoals) {
                    if (getActiveCommand() == namedGoal.getAction()) {
                        if (!namedGoal.isOn())
                            namedGoal.turnOn();
                    } else
                        namedGoal.turnOff();
                }
            }
        }
    }

    @Override
    protected SoundEvent attackSound() {
        Item item = getItemBySlot(EquipmentSlotType.MAINHAND).getItem();
        if (item instanceof SwordItem || item instanceof ToolItem)
            return SoundEvents.PLAYER_ATTACK_STRONG;
        return SoundEvents.PLAYER_ATTACK_KNOCKBACK;
    }

    @Override
    public int getMaxFallDistance() {
        return 3;
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack candidate, ItemStack current) {
        if (current.isEmpty()) {
            return true;
        } else if (candidate.getItem() instanceof SwordItem) {
            if (!(current.getItem() instanceof SwordItem)) {
                return true;
            } else {
                SwordItem sworditem = (SwordItem) candidate.getItem();
                SwordItem sworditem1 = (SwordItem) current.getItem();
                if (sworditem.getDamage() != sworditem1.getDamage()) {
                    return sworditem.getDamage() > sworditem1.getDamage();
                } else {
                    return this.canReplaceEqualItem(candidate, current);
                }
            }
        } else if (candidate.getItem() instanceof BowItem && current.getItem() instanceof BowItem) {
            return this.canReplaceEqualItem(candidate, current);
        } else if (candidate.getItem() instanceof CrossbowItem && current.getItem() instanceof CrossbowItem) {
            return this.canReplaceEqualItem(candidate, current);
        } else if (candidate.getItem() instanceof ArmorItem) {
            if (EnchantmentHelper.hasBindingCurse(current)) {
                return false;
            } else if (!(current.getItem() instanceof ArmorItem)) {
                return true;
            } else {
                ArmorItem armoritem = (ArmorItem) candidate.getItem();
                ArmorItem armoritem1 = (ArmorItem) current.getItem();
                if (armoritem.getDefense() != armoritem1.getDefense()) {
                    return armoritem.getDefense() > armoritem1.getDefense();
                } else if (armoritem.getToughness() != armoritem1.getToughness()) {
                    return armoritem.getToughness() > armoritem1.getToughness();
                } else {
                    return this.canReplaceEqualItem(candidate, current);
                }
            }
        } else {
            if (candidate.getItem() instanceof ToolItem) {
                if (current.getItem() instanceof BlockItem) {
                    return true;
                }

                if (current.getItem() instanceof ToolItem) {
                    ToolItem toolitem = (ToolItem) candidate.getItem();
                    ToolItem toolitem1 = (ToolItem) current.getItem();
                    if (toolitem.getAttackDamage() != toolitem1.getAttackDamage()) {
                        return toolitem.getAttackDamage() > toolitem1.getAttackDamage();
                    }

                    return this.canReplaceEqualItem(candidate, current);
                }
            }

            Item currentItem = current.getItem();
            return !(currentItem instanceof CrossbowItem || currentItem instanceof BowItem || currentItem instanceof TridentItem || currentItem instanceof SwordItem || currentItem instanceof ToolItem);
        }
    }
}
