package dev.buildtool.scp.scps.plaguedoctor;

import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.human.Human;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class Corpse extends SCPEntity {
    public static final DataParameter<Optional<UUID>> KILLED= EntityDataManager.defineId(Corpse.class, DataSerializers.OPTIONAL_UUID);
    public Corpse(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(5, new HurtByTargetGoal(this));
        goalSelector.addGoal(11, new LookRandomlyGoal(this));
        goalSelector.addGoal(12,new LookAtGoal(this,PlayerEntity.class,8));
        goalSelector.addGoal(9,new MeleeAttackGoal(this,1,true));
        goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this,1));
        targetSelector.addGoal(12, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true, false));
        targetSelector.addGoal(13, new NearestAttackableTargetGoal<>(this, Human.class, true, false));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(KILLED,Optional.empty());
    }


    public void setKilled(UUID uuid)
    {
        entityData.set(KILLED,Optional.of(uuid));
    }

    public Optional<UUID> getKilled()
    {
        return entityData.get(KILLED);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        setKilled(compound.getUUID("Killed"));
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        getKilled().ifPresent(uuid -> compound.putUUID("Killed",uuid));
    }
}
