package dev.buildtool.scp.humansrefuted;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.registration.Entities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;

import java.util.Collections;

public class EggEntity extends LivingEntity {
    private int growthTime;

    public EggEntity(EntityType<? extends LivingEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        growthTime++;
        if (growthTime >= Functions.minutesToTicks(59)) { //59 minutes
            HumanRefutedChild humanRefuted = Entities.humanRefutedChild.create(level);
            humanRefuted.setPos(getX(), getY(), getZ());
            level.addFreshEntity(humanRefuted);
            remove();
        }
        if (getRemainingFireTicks() > 0)
            growthTime += 2;
    }

//    /**
//     * Handles turning. Disabled
//     */
//    @Override
//    protected float updateDistance(float p_110146_1_, float p_110146_2_) {
//        return 0;
//    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Time", growthTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        growthTime = compound.getInt("Time");
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }
}
