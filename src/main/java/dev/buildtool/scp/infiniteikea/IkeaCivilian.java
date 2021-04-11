package dev.buildtool.scp.infiniteikea;

import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.human.Human;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.OpenDoorGoal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class IkeaCivilian extends Human {
    public IkeaCivilian(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        entityData.set(VARIANT, random.nextInt(3));

        ITagCollection<Item> itemTags = TagCollectionManager.getInstance().getItems();
        if (random.nextBoolean()) {
            ITag<Item> itemITag = itemTags.getTag(new ResourceLocation("swords"));
            setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(itemITag.getRandomElement(random)));
        } else if (random.nextBoolean()) {
            setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        } else if (random.nextBoolean()) {
            setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
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
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        setVariant(compound.getInt("Variant"));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.removeGoal(defend);
        goalSelector.addGoal(4, defend = new HurtByTargetGoal(this).setAlertOthers(MaleCivilian.class, FemaleCivilian.class));
        goalSelector.addGoal(5, new OpenDoorGoal(this, true));
        targetSelector.addGoal(1, new AttackMonsters(this, true, false));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !hasOwner() && !hasCustomName();
    }

}
