package dev.buildtool.scp.human;

import dev.buildtool.scp.SCPEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MaleCommoner extends Human {
    public MaleCommoner(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        entityData.set(VARIANT, random.nextInt(6));
        if (random.nextInt(100) < 5) {
            setCustomName(new StringTextComponent("Alexiy"));
            setCustomNameVisible(true);
            getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Health bonus", 10, AttributeModifier.Operation.ADDITION));
            heal(10);
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
        entityData.set(VARIANT, compound.getInt("Variant"));
    }
}
