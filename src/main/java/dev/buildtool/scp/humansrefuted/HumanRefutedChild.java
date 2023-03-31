package dev.buildtool.scp.humansrefuted;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.registration.Entities;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class HumanRefutedChild extends HumanRefuted {
    int timUntilGrowth;

    public HumanRefutedChild(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        timUntilGrowth++;
        if (growthTime() == Functions.minutesToTicks(40)) { //40 minutes
            grow();
        }
    }

    @Override
    public void grow() {
        HumanRefuted humanRefuted = Entities.humanRefuted.create(level);
        humanRefuted.setPos(getX(),getY(),getZ());
//        humanRefuted.set(getX(), getY(), getZ(), getViewYRot(1), getViewXRot(1));
        humanRefuted.setHealth(getHealth());
        level.addFreshEntity(humanRefuted);
        remove();
    }

    @Override
    public int growthTime() {
        return timUntilGrowth;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Age", timUntilGrowth);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        timUntilGrowth = compound.getInt("Age");
    }

    @Override
    public boolean isBaby() {
        return true;
    }
}
