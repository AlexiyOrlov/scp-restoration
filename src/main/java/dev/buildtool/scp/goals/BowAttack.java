package dev.buildtool.scp.goals;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.BowItem;
import net.minecraft.item.Items;

import java.util.EnumSet;

public class BowAttack<E extends MobEntity & IRangedAttackMob> extends Goal {
    protected final E actor;
    protected final double speed;
    protected int attackInterval;
    protected final float squaredRange;
    protected int cooldown = -1;
    protected int targetSeeingTicker;
    protected boolean movingToLeft;
    protected boolean backward;
    protected int combatTicks = -1;

    public BowAttack(E actor, double speed, int attackInterval, float squaredRange) {
        this.actor = actor;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.squaredRange = squaredRange;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return actor.getTarget() != null && isHoldingBow();
    }

    protected boolean isHoldingBow() {
        return actor.isHolding(item -> item instanceof BowItem);
    }

    @Override
    public boolean canContinueToUse() {
        return (canUse() || !actor.getNavigation().isStuck()) && isHoldingBow();
    }

    @Override
    public void start() {
        super.start();
        actor.setAggressive(true);
    }

    @Override
    public void stop() {
        super.stop();
        actor.setAggressive(false);
        targetSeeingTicker = 0;
        cooldown = -1;
        actor.stopUsingItem();
    }

    @Override
    public void tick() {
        LivingEntity target = actor.getTarget();
        if (target != null && target.isAlive()) {
            double sqDistanceToTarget = actor.distanceToSqr(target.getX(), target.getY(), target.getZ());
            boolean canSeeTarget = this.actor.getSensing().canSee(target);
            boolean seenTicksIsPostive = this.targetSeeingTicker > 0;
            if (canSeeTarget != seenTicksIsPostive) {
                this.targetSeeingTicker = 0;
            }

            if (canSeeTarget) {
                ++this.targetSeeingTicker;
            } else {
                --this.targetSeeingTicker;
            }

            if (sqDistanceToTarget <= (double) this.squaredRange && this.targetSeeingTicker >= 20) {
                this.actor.getNavigation().stop();
                ++this.combatTicks;
            } else {
                this.actor.getNavigation().moveTo(target, this.speed);
                this.combatTicks = -1;
            }

            if (this.combatTicks >= 20) {
                if ((double) this.actor.getRandom().nextFloat() < 0.3D) {
                    this.movingToLeft = !this.movingToLeft;
                }

                if ((double) this.actor.getRandom().nextFloat() < 0.3D) {
                    this.backward = !this.backward;
                }

                this.combatTicks = 0;
            }

            if (this.combatTicks > -1) {
                if (sqDistanceToTarget > (double) (this.squaredRange * 0.75F)) {
                    this.backward = false;
                } else if (sqDistanceToTarget < (double) (this.squaredRange * 0.25F)) {
                    this.backward = true;
                }

                this.actor.getMoveControl().strafe(this.backward ? -0.5F : 0.5F, this.movingToLeft ? 0.5F : -0.5F);
                this.actor.lookAt(target, 180.0F, 30.0F);
            } else {
                this.actor.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }

            if (this.actor.isUsingItem()) {
                if (!canSeeTarget && this.targetSeeingTicker < -60) {
                    this.actor.stopUsingItem();
                } else if (canSeeTarget) {
                    int i = this.actor.getUseItemRemainingTicks();
                    if (i >= 20) {
                        this.actor.stopUsingItem();
                        this.actor.performRangedAttack(target, BowItem.getPowerForTime(i));
                        this.cooldown = this.attackInterval;
                    }
                }
            } else if (--this.cooldown <= 0 && this.targetSeeingTicker >= -60) {
                this.actor.startUsingItem(ProjectileHelper.getWeaponHoldingHand(this.actor, Items.BOW));
            }
        }
    }
}
