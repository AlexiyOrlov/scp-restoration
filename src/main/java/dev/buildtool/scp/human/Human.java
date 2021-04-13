package dev.buildtool.scp.human;

import dev.buildtool.satako.Constants;
import dev.buildtool.satako.Functions;
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
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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

public class Human extends SCPEntity implements IRangedAttackMob, ICrossbowUser, INamedContainerProvider {
    protected static final DataParameter<Integer> VARIANT = EntityDataManager.defineId(Human.class, DataSerializers.INT);
    protected static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.defineId(Human.class, DataSerializers.OPTIONAL_UUID);

    public Human(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
        setCanPickUpLoot(true);
    }

    public HurtByTargetGoal defend;
    protected GoalAction activeCommand = GoalAction.IDLE;
    protected Follow<Human, PlayerEntity> follow;
    protected GuardPosition<Human, MobEntity> guardPosition;
    protected Protect<Human, PlayerEntity> protectOwner;
    protected Assist<Human, PlayerEntity> assistPlayer;
    public UniqueList<NamedGoal> namedGoals;

    @Override
    protected void registerGoals() {
        super.registerGoals();
        namedGoals = new UniqueList<>();
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(1, new BowAttack<>(this, 1, 13, 50));
        goalSelector.addGoal(2, new CrossbowAttack<>(this, 1, 50));
        goalSelector.addGoal(3, new BetterMeleeAttackGoal(this, 1, true, 20));
        goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(5, defend = new RevengeGoal(this, livingEntity -> livingEntity.getClass() == getClass()));
        goalSelector.addGoal(6, new AvoidEntityGoal<>(this, CreeperEntity.class, 5, 1, 1.5));
        goalSelector.addGoal(7, follow = new Follow<>(this, PlayerEntity.class, playerEntity -> playerEntity.getUUID().equals(getOwner())));
        Predicate<MobEntity> filter = mobEntity -> {
            if (mobEntity instanceof CreeperEntity) {
                return Functions.isHolding(item -> item==Items.BOW, this) || Functions.isHolding(item -> item==Items.CROSSBOW, this);
            }
            return mobEntity instanceof IMob;
        };
        goalSelector.addGoal(8, guardPosition = new GuardPosition<>(this, new Class[]{MobEntity.class}, filter));
        goalSelector.addGoal(9, protectOwner = new Protect<>(this, PlayerEntity.class, playerEntity -> playerEntity.getUUID().equals(getOwner())));
        goalSelector.addGoal(10, assistPlayer = new Assist<>(this, PlayerEntity.class, playerEntity -> playerEntity.getUUID().equals(getOwner())));
        goalSelector.addGoal(10, new LookRandomlyGoal(this));
        goalSelector.addGoal(9, new LookAtGoal(this, LivingEntity.class, 32));
        namedGoals.add(follow);
        namedGoals.add(guardPosition);
        namedGoals.add(protectOwner);
        namedGoals.add(assistPlayer);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        lookAt(target, 360, 180);
        ArrowEntity arrowEntity = new ArrowEntity(level, this);
        double dx = target.getX() - getX();
        double dz = target.getZ() - getZ();
        double dy = target.getY(0.333) - arrowEntity.getY();
        double sqrrt = Math.sqrt(dx * dx + dz * dz);
        arrowEntity.shoot(dx, dy + sqrrt * 0.2, dz, 1.6f, 0);
        playSound(SoundEvents.SKELETON_SHOOT, 1, random.nextFloat());
        level.addFreshEntity(arrowEntity);
    }

    //TODO check
    @Override
    protected void blockUsingShield(LivingEntity entityIn) {
        super.blockUsingShield(entityIn);
        if (entityIn.getMainHandItem().getItem().canDisableShield(this.getOffhandItem(), this.useItem, this, entityIn)) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            f += 0.75F;
            if (this.random.nextFloat() < f) {
                this.blockedByShield(entityIn);
//                this.resetActiveHand();
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
        if (damage >= 3.0F && this.useItem.getItem().isShield(this.useItem, this)) {
            int i = 1 + MathHelper.floor(damage);
            this.useItem.hurtAndBreak(i, this, human -> human.broadcastBreakEvent(getUsedItemHand()));

            if (this.useItem.isEmpty()) {
                Hand enumhand = this.getUsedItemHand();
                if (enumhand == Hand.MAIN_HAND) {
                    this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                }

                this.useItem = ItemStack.EMPTY;
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
        if (player.getItemInHand(hand).isEmpty()) {
            if (level instanceof ServerWorld)
                NetworkHooks.openGui((ServerPlayerEntity) player, this, packetBuffer -> packetBuffer.writeInt(getId()));
            return ActionResultType.SUCCESS;
        }
        return super.interactAt(player, vec, hand);
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
}
