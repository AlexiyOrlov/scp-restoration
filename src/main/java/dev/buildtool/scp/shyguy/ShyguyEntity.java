package dev.buildtool.scp.shyguy;

import dev.buildtool.satako.Functions;
import dev.buildtool.satako.RandomizedList;
import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.events.Sounds;
import dev.buildtool.scp.human.Human;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SCPObject(number = "096", classification = SCPObject.Classification.EUCLID, name = "Shyguy")
public class ShyguyEntity extends SCPEntity {
    List<UUID> targets;
    int aggroTime;
    int cryInterval;
    RandomizedList<LivingEntity> watching = new RandomizedList<>();
    static public final DataParameter<Byte> state = EntityDataManager.defineId(ShyguyEntity.class, DataSerializers.BYTE);
    static final Predicate<PlayerEntity> playerEntityPredicate = playerEntity -> !playerEntity.isCreative() && !playerEntity.isSpectator();

    public ShyguyEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        targets = new ArrayList<>();
    }

    @Override
    protected PathNavigator createNavigation(World worldIn) {
        return new GroundPathNavigator(this, worldIn);
    }

    @Override
    public boolean onClimbable() {
        return horizontalCollision && navigation instanceof ClimberPathNavigator;
    }

    @Override
    protected SoundEvent attackSound() {
        return Sounds.shyguyAttack;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return Sounds.shyguyHurt;
    }

    //TODO check if looking at FACE

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide) {
            Byte aByte = entityData.get(ShyguyEntity.state);
            List<LivingEntity> watchers = level.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(blockPosition()).inflate(getRange()), pr -> Functions.isInSightOf(this, pr, 0.1f));
            watchers.remove(this);
            watchers.removeIf(livingEntity -> !(livingEntity instanceof PlayerEntity) && !(livingEntity instanceof Human));
            watchers.removeIf(EntityPredicates.NO_CREATIVE_OR_SPECTATOR.negate()::test);
            watching.addAll(watchers);
            switch (aByte) {
                //idle
                case 0:

                    if (!watching.isEmpty()) {
                        entityData.set(state, (byte) 1);
                        aggroTime = Functions.secondsToTicks(10);
                    }
                    break;
                //crying
                case 1:
                    if (aggroTime == 0) {
                        {
                            entityData.set(state, (byte) 2);
                            cryInterval = 0;
                            navigation = new ClimberPathNavigator(this, level);
                        }
                    } else {
                        aggroTime--;
                        if (cryInterval == 0) {
                            playSound(Sounds.angryScreams, 1, 1);
                            cryInterval = Functions.secondsToTicks(10);
                        } else {
                            cryInterval--;
                        }

                    }
                    break;
                //attacking
                case 2:
                    if (getTarget() == null) {
                        LivingEntity randEntity = watching.getRandom();
                        setTarget(randEntity);
                    } else if (!getTarget().isAlive()) {
                        setTarget(null);
                        watching.remove(getTarget());
                    }
                    if (getTarget() != null && getNavigation().isStuck()) {
                        Direction direction = getDirection();
                        BlockPos pos = blockPosition().above();
                        if (getTarget().getY() <= getY()) {
                            Stream.of(pos.relative(direction), pos.below().relative(direction)).forEach(pos1 -> {
                                if (level.getBlockState(pos1).getDestroySpeed(level, pos1) != -1)
                                    level.destroyBlock(pos1, true);
                            });
                        }

                    }
                    watching.removeIf(livingEntity -> !livingEntity.isAlive());
                    if (watching.isEmpty()) {
                        entityData.set(state, (byte) 0);
                        navigation = new GroundPathNavigator(this, level);
                    }
                    break;
            }
        }
        if (getHealth() < getMaxHealth()) {
            EffectInstance effectInstance = new EffectInstance(Effects.REGENERATION, 20, 0, false, false);
            addEffect(effectInstance);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AggroTime", aggroTime);
        int n = 0;
        for (UUID livingEntity : targets) {
            compound.putUUID("#" + n++, livingEntity);
        }
        compound.putInt("Watched count", n);
        compound.putByte("State", entityData.get(state));
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        aggroTime = compound.getInt("AggroTime");
        for (int i = 1; i < compound.getInt("Watched count"); i++) {
            targets.add(compound.getUUID("#" + i));
        }
        entityData.set(state, compound.getByte("State"));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1, true));
        goalSelector.addGoal(7, new LookRandomlyGoal(this));
        goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.25) {
            @Override
            public boolean canUse() {
                return entityData.get(state) == State.IDLE.aByte && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return entityData.get(state) == State.IDLE.aByte && super.canContinueToUse();
            }
        });
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(state, State.IDLE.aByte);
    }

    public enum State {
        IDLE(0),
        CRYING(1),
        ACTIVE(2);
        public byte aByte;

        State(int i) {
            aByte = (byte) i;
        }
    }

    State getState() {
        return State.values()[entityData.get(state)];
    }


    @Override
    protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
        return p_213348_2_.height;
    }


}
