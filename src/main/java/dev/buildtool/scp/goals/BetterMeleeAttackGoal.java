package dev.buildtool.scp.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class BetterMeleeAttackGoal extends Goal {
   protected final CreatureEntity attacker;
   private final double speedTowardsTarget;
   private final boolean longMemory;
   private Path path;
   private double targetX;
   private double targetY;
   private double targetZ;
   private int delayCounter;
   private int attackCooldown;
   private final int attackInterval;
   private long field_220720_k;
   private int failedPathFindingPenalty = 0;
   private final boolean canPenalize = false;

   public BetterMeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory, int timeBetweenAttacks) {
      this.attacker = creature;
      this.speedTowardsTarget = speedIn;
      this.longMemory = useLongMemory;
      this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
      this.attackInterval = timeBetweenAttacks;
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean canUse() {
      long i = this.attacker.level.getGameTime();
      if (i - this.field_220720_k < 20L) {
         return false;
      } else {
         this.field_220720_k = i;
         LivingEntity livingentity = this.attacker.getTarget();
         if (livingentity == null) {
            return false;
         } else if (!livingentity.isAlive()) {
            return false;
         } else {
            if (canPenalize) {
               if (--this.delayCounter <= 0) {
                  this.path = this.attacker.getNavigation().createPath(livingentity, 0);
                  this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
                  return this.path != null;
               } else {
                  return true;
               }
            }
            this.path = this.attacker.getNavigation().createPath(livingentity, 0);
            if (this.path != null) {
               return true;
            } else {
               return this.getAttackReachSqr(livingentity) >= this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            }
         }
      }
   }

   /**
    * Returns whether an in-progress EntityAIBase should continue executing
    */
   public boolean canContinueToUse() {
      LivingEntity livingentity = this.attacker.getTarget();
      if (livingentity == null) {
         return false;
      } else if (!livingentity.isAlive()) {
         return false;
      } else if (!this.longMemory) {
         return !this.attacker.getNavigation().isStuck();
      } else if (!this.attacker.isWithinRestriction(livingentity.blockPosition())) {
         return false;
      } else {
         return !(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity) livingentity).isCreative();
      }
   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void startExecuting() {
      this.attacker.getNavigation().moveTo(this.path, this.speedTowardsTarget);
      this.attacker.setAggressive(true);
      this.delayCounter = 0;
      this.attackCooldown = 0;
   }

   /**
    * Reset the task's internal state. Called when this task is interrupted by another one
    */
   public void stop() {
      LivingEntity livingentity = this.attacker.getTarget();
      if (!EntityPredicates.ATTACK_ALLOWED.test(livingentity)) {
         this.attacker.setTarget(null);
      }

      this.attacker.setAggressive(false);
      this.attacker.getNavigation().stop();
//      attacker.resetActiveHand();
   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() {
      LivingEntity livingentity = this.attacker.getTarget();
      if (livingentity != null) {
         this.attacker.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
         double d0 = this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
         this.delayCounter = Math.max(this.delayCounter - 1, 0);
         if ((this.longMemory || this.attacker.getSensing().canSee(livingentity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
            this.targetX = livingentity.getX();
            this.targetY = livingentity.getY();
            this.targetZ = livingentity.getZ();
            this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
            if (this.canPenalize) {
               this.delayCounter += failedPathFindingPenalty;
               if (this.attacker.getNavigation().getPath() != null) {
                  net.minecraft.pathfinding.PathPoint finalPathPoint = this.attacker.getNavigation().getPath().getEndNode();
                  if (finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                     failedPathFindingPenalty = 0;
                  else
                     failedPathFindingPenalty += 10;
               } else {
                  failedPathFindingPenalty += 10;
               }
            }
            if (d0 > 1024.0D) {
               this.delayCounter += 10;
            } else if (d0 > 256.0D) {
               this.delayCounter += 5;
            }

            if (!this.attacker.getNavigation().moveTo(livingentity, this.speedTowardsTarget)) {
               this.delayCounter += 15;
            }
         }

         this.attackCooldown = Math.max(this.getAttackCooldown() - 1, 0);
         this.checkAndPerformAttack(livingentity, d0);
      }
   }

   protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
      double d0 = this.getAttackReachSqr(enemy);
      if (distToEnemySqr > d0) {
         //randomly activate shield
         if (attacker.getOffhandItem().isShield(attacker) && attacker.getRandom().nextBoolean()) {
//            attacker.setActiveHand(Hand.OFF_HAND);
         }
      } else if (distToEnemySqr <= d0 && canAttack()) {
//         attacker.setActiveHand(Hand.MAIN_HAND);
         this.resetAttackCooldown();
         this.attacker.swing(Hand.MAIN_HAND);
         this.attacker.doHurtTarget(enemy);
      }
//      else
//         attacker.resetActiveHand();

   }

   protected void resetAttackCooldown() {
      this.attackCooldown = cooldownTime();
   }

   protected boolean canAttack() {
      return this.attackCooldown <= 0;
   }

   protected int getAttackCooldown() {
      return this.attackCooldown;
   }

   protected int cooldownTime() {
      return attackInterval;
   }

   protected double getAttackReachSqr(LivingEntity attackTarget) {
      return this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth();
   }
}
