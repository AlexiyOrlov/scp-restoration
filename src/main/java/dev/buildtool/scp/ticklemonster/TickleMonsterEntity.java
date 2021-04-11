package dev.buildtool.scp.ticklemonster;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.human.Human;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@SCPObject(number = "999", name = "Tickle Monster", classification = SCPObject.Classification.SAFE)
public class TickleMonsterEntity extends SCPEntity {
    public TickleMonsterEntity(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(5, new BuffGoal(livingEntity -> livingEntity instanceof PlayerEntity && livingEntity.getHealth() < livingEntity.getMaxHealth(), 5));
        goalSelector.addGoal(6, new BuffGoal(livingEntity -> livingEntity instanceof Human && livingEntity.getHealth() < livingEntity.getMaxHealth(), 5));
        goalSelector.addGoal(7, new BuffGoal(livingEntity -> livingEntity instanceof VillagerEntity && livingEntity.getHealth() < livingEntity.getMaxHealth(), 5));
        goalSelector.addGoal(8, new BuffGoal(livingEntity -> livingEntity instanceof AnimalEntity && livingEntity.getHealth() < livingEntity.getMaxHealth(), 5));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {

    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SLIME_HURT;
    }

    class BuffGoal extends Goal {
        Predicate<LivingEntity> followTo;
        double distance;
        LivingEntity target;

        public BuffGoal(Predicate<LivingEntity> followTo, double distance) {
            this.followTo = followTo;
            this.distance = distance;
        }

        @Override
        public boolean canUse() {
            List<LivingEntity> livingEntities = level.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(blockPosition()).inflate(40), followTo);
            if (!livingEntities.isEmpty()) {
                target = livingEntities.get(0);
                return true;
            }
            return false;
        }

        @Override
        public void tick() {
            super.tick();
            if (target != null) {
                if (distance < distanceTo(target))
                    getNavigation().moveTo(target, 1);
                else {
                    target.addEffect(new EffectInstance(Effects.REGENERATION, Functions.minutesToTicks(15)));
                    target.addEffect(new EffectInstance(Effects.LUCK, Functions.minutesToTicks(15)));
                }
            }
        }

        @Override
        public void stop() {
            super.stop();
            target = null;
        }
    }
}
