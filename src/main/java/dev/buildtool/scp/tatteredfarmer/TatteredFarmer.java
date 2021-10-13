package dev.buildtool.scp.tatteredfarmer;

import dev.buildtool.satako.Functions;
import dev.buildtool.satako.InanimateEntity;
import dev.buildtool.satako.RandomizedList;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.goals.AdditionalMeleeAttack;
import dev.buildtool.scp.human.Human;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@SCPObject(name = "The Tattered Farmer", number = "872", classification = SCPObject.Classification.SAFE)
public class TatteredFarmer extends InanimateEntity {
    HashSet<AnimalEntity> affectedAnimals = new HashSet<>(200);
    static RandomizedList<Class<? extends AnimalEntity>> animalClasses = new RandomizedList<>(Arrays.asList(AbstractHorseEntity.class, ChickenEntity.class, PigEntity.class, SheepEntity.class, CowEntity.class, RabbitEntity.class));

    public TatteredFarmer(EntityType<? extends LivingEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount % 80 == 0) {
            List<AnimalEntity> animals = level.getEntitiesOfClass(AnimalEntity.class, getBoundingBox().inflate(100), animalEntity -> animalEntity instanceof AbstractHorseEntity ||
                    animalEntity instanceof ChickenEntity || animalEntity instanceof PigEntity || animalEntity instanceof SheepEntity || animalEntity instanceof CowEntity ||
                    animalEntity instanceof RabbitEntity);
            if (animals.size() < 40) {
                animals.forEach(animalEntity -> {
                    if (!affectedAnimals.contains(animalEntity)) {
                        animalEntity.goalSelector.addGoal(5, new AdditionalMeleeAttack(animalEntity, 1.5f, true, 20, 1 + animalEntity.getBbWidth()));
                        animalEntity.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(animalEntity, PlayerEntity.class, true, false));
                        animalEntity.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(animalEntity, Human.class, true, false));
                        animalEntity.goalSelector.availableGoals.stream().filter(prioritizedGoal -> prioritizedGoal.getGoal() instanceof PanicGoal).findFirst().ifPresent(prioritizedGoal -> {
                            animalEntity.goalSelector.availableGoals.remove(prioritizedGoal);
                            animalEntity.goalSelector.removeGoal(prioritizedGoal);
                        });
                        animalEntity.goalSelector.availableGoals.stream().filter(prioritizedGoal -> prioritizedGoal.getGoal() instanceof AvoidEntityGoal).collect(Collectors.toSet()).forEach(prioritizedGoal -> {
                            animalEntity.goalSelector.availableGoals.remove(prioritizedGoal);
                            animalEntity.goalSelector.removeGoal(prioritizedGoal);
                        });
                        affectedAnimals.add(animalEntity);
                    }
                });
            }
            for (Class<? extends AnimalEntity> animalClass : animalClasses) {
                List<AnimalEntity> animalEntities = level.getEntitiesOfClass(animalClass, getBoundingBox().inflate(100));
                if (animalEntities.size() > 1) {
                    animals.stream().filter(AnimalEntity::canFallInLove).forEach(animalEntity -> animalEntity.setInLoveTime(Functions.secondsToTicks(40)));
                }
                if (animalEntities.size() > 10) {
                    animalEntities.stream().filter(animalEntity1 -> !animalEntity1.isBaby()).findAny().ifPresent(animalEntity1 -> animalEntity1.hurt(DamageSource.GENERIC, animalEntity1.getMaxHealth()));
                }
            }
        }
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_BREAK_BLOCK;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.WOOD_BREAK;
    }


}
