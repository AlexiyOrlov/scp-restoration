package dev.buildtool.scp.human;

import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.goals.BowAttack;
import dev.buildtool.scp.goals.RevengeGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ChaosInsurgencySoldier extends Human {
    public ChaosInsurgencySoldier(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
        return ActionResultType.PASS;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        namedGoals = null;
        goalSelector.availableGoals.removeIf(prioritizedGoal -> prioritizedGoal.getGoal() instanceof BowAttack);
        goalSelector.addGoal(1, new BowAttack<>(this, 1, 25, 50));
        goalSelector.removeGoal(defend);
        goalSelector.addGoal(1, defend = new RevengeGoal(this, livingEntity -> livingEntity.getClass() == getClass()).setAlertOthers(ChaosInsurgencySoldier.class));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Human.class, 0, true, false, livingEntity -> livingEntity instanceof Human && ((Human) livingEntity).getOwner() != null));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true, false));
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        entityData.set(VARIANT, random.nextInt(3));
        ITagCollection<Item> itemTags = TagCollectionManager.getInstance().getItems();
        if (random.nextBoolean()) {
            ITag<Item> itemITag = itemTags.getTag(new ResourceLocation("swords"));
            setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(itemITag.getRandomElement(random)));
            if (random.nextInt(4) == 1) {
                setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SHIELD));
            }
        } else if (random.nextBoolean()) {
            setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        } else if (random.nextBoolean()) {
            setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
        } else {
            setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(itemTags.getTag(new ResourceLocation("swords")).getRandomElement(random)));
        }


        if (random.nextBoolean()) {
            setItemSlot(EquipmentSlotType.HEAD, new ItemStack(itemTags.getTag(new ResourceLocation("helmets")).getRandomElement(random)));
        }
        if (random.nextBoolean()) {
            setItemSlot(EquipmentSlotType.FEET, new ItemStack(itemTags.getTag(new ResourceLocation("boots")).getRandomElement(random)));
        }
        if (random.nextBoolean()) {
            setItemSlot(EquipmentSlotType.LEGS, new ItemStack(itemTags.getTag(new ResourceLocation("leggings")).getRandomElement(random)));
        }
        if (random.nextBoolean()) {
            setItemSlot(EquipmentSlotType.CHEST, new ItemStack(itemTags.getTag(new ResourceLocation("chestplates")).getRandomElement(random)));
        }
        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return true;
    }
}
