package dev.buildtool.scp.humansrefuted;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.Ageable;
import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.events.Entities;
import dev.buildtool.scp.events.Sounds;
import dev.buildtool.scp.human.Human;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Can't swim and reproduce in liquid
 */
@SCPObject(number = "3199", name = "Humans, Refuted", classification = SCPObject.Classification.KETER)
public class HumanRefuted extends SCPEntity implements Ageable {
    private int timUntilGrowth, timeUntilEgg;
    public HumanRefuted(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(4, new HurtByTargetGoal(this));
        goalSelector.addGoal(5, new MeleeAttackGoal(this, 1, true));
        goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(7, new LookRandomlyGoal(this));
        goalSelector.addGoal(8, new LookAtGoal(this, LivingEntity.class, 32));
        targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false, false));
        targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Human.class, false, false));
        targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AnimalEntity.class, false, false));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Time", timUntilGrowth);
        compound.putInt("Time until egg", timeUntilEgg);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        timUntilGrowth = compound.getInt("Time");
        timeUntilEgg = compound.getInt("Time until egg");
    }


    @Override
    public void tick() {
        super.tick();
        if (!isBaby()) {
            if (timeUntilEgg == Functions.minutesToTicks(28)) //28 minutes
            {
                if (level.getFluidState(blockPosition()).isEmpty() && level.getEntities(this, new AxisAlignedBB(blockPosition()).inflate(3), entity -> entity.getClass() == HumanRefutedChild.class || entity.getClass() == getClass()).isEmpty()) {
                    EggEntity eggEntity = Entities.humanRefutedEgg.create(level);
                    eggEntity.setPos(getX(), getY(), getZ());
                    level.addFreshEntity(eggEntity);
                    timeUntilEgg = 0;
                }
            } else timeUntilEgg++;
        }
    }


    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void grow() {

    }

    @Override
    public int growthTime() {
        return 0;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return Sounds.scp3199Idle;
    }

    @Override
    protected float getSoundVolume() {
        return 0.5f;
    }

    @Override
    protected float getVoicePitch() {
        return isBaby() ? 1.25f : 1f;
    }

    @Override
    public int getAmbientSoundInterval() {
        return Functions.secondsToTicks(10);
    }
}
