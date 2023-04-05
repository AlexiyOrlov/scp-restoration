package dev.buildtool.scp.sculpture;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.human.Human;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.OpenDoorGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@SCPObject(number = "173", name = "Sculpture", classification = SCPObject.Classification.EUCLID)
public class Sculpture extends SCPEntity {
    public Sculpture(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
        setInvulnerable(true);
        GroundPathNavigator groundPathNavigator = (GroundPathNavigator) getNavigation();
        groundPathNavigator.setCanOpenDoors(true);
    }

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(8, new MeleeAttackGoal(this, 1, true));
        goalSelector.addGoal(10, new Wander(this, 1, 0.001f));
        goalSelector.addGoal(9, new OpenDoorGoal(this, false));
        targetSelector.addGoal(1, new Target(this, LivingEntity.class, 1, true, false, livingEntity -> livingEntity instanceof Human || (livingEntity instanceof PlayerEntity && !((PlayerEntity) livingEntity).isCreative())));
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    public static boolean canMove(Sculpture sculptureEntity) {
        double range = sculptureEntity.getRange();
        List<LivingEntity> entities = sculptureEntity.level.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(sculptureEntity.blockPosition()).inflate(range), livingEntity -> livingEntity instanceof Human || (livingEntity instanceof PlayerEntity && (!((PlayerEntity) livingEntity).isCreative() || livingEntity.isSpectator())));

        entities.removeIf(entityLivingBase -> entityLivingBase.hasEffect(Effects.BLINDNESS) && sculptureEntity.distanceTo(entityLivingBase) > 6);
        for (LivingEntity entity : entities) {
            if (Functions.isInSightOf(sculptureEntity, entity, 80)) {
                return false;
            }
        }
        return true;
    }

    static class Wander extends WaterAvoidingRandomWalkingGoal {
        public Wander(CreatureEntity creature, double speedIn, float probabilityIn) {
            super(creature, speedIn, probabilityIn);
        }

        @Override
        public boolean canUse() {
            return this.mob.getTarget() == null && canMove((Sculpture) mob) && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return canMove((Sculpture) mob) && super.canContinueToUse();
        }
    }

    static class Target extends NearestAttackableTargetGoal<LivingEntity> {
        public Target(MobEntity goalOwnerIn, Class<LivingEntity> targetClassIn, int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn, @Nullable Predicate<LivingEntity> targetPredicate) {
            super(goalOwnerIn, targetClassIn, targetChanceIn, checkSight, nearbyOnlyIn, targetPredicate);
        }

        @Override
        public boolean canUse() {
            return canMove((Sculpture) mob) && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return canMove((Sculpture) mob) && super.canContinueToUse();
        }
    }
}
