package dev.buildtool.scp.goals;

import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.EnumSet;

public class CrossbowAttack<T extends MobEntity & IRangedAttackMob & ICrossbowUser> extends Goal {
    protected final T actor;

    @Override
    public boolean canUse() {
        return hasAliveTarget() && this.isEntityHoldingCrossbow();
    }

    protected CrossbowAttack.Stage stage;
    protected final double speed;
    protected final float squaredRange;
    protected int seeingTargetTicker;
    protected int chargedTicksLeft;
    protected boolean backward;
    protected boolean movingToLeft;

    public CrossbowAttack(T actor, double movementSpeed, float range) {
        this.stage = CrossbowAttack.Stage.UNCHARGED;
        this.actor = actor;
        this.speed = movementSpeed;
        this.squaredRange = range * range;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }


    private boolean isEntityHoldingCrossbow() {
        return this.actor.isHolding(item -> item instanceof CrossbowItem);
    }

    public boolean shouldContinueExecuting() {
        return this.hasAliveTarget() && (this.canUse() || !this.actor.getNavigation().isStuck()) && this.isEntityHoldingCrossbow();
    }

    private boolean hasAliveTarget() {
        return this.actor.getTarget() != null && this.actor.getTarget().isAlive();
    }

    public void stop() {
        super.stop();
        this.actor.setAggressive(false);
        this.actor.setTarget(null);
        this.seeingTargetTicker = 0;
        if (this.actor.isUsingItem()) {
            this.actor.stopUsingItem();
            this.actor.setChargingCrossbow(false);
            CrossbowItem.setCharged(this.actor.getUseItem(), false);
        }

    }

    public void tick() {
        LivingEntity livingEntity = this.actor.getTarget();
        if (livingEntity != null) {
            boolean canSeeTarget = this.actor.getSensing().canSee(livingEntity);
            boolean seenTicksPositive = this.seeingTargetTicker > 0;
            if (canSeeTarget != seenTicksPositive) {
                this.seeingTargetTicker = 0;
            }

            if (canSeeTarget) {
                ++this.seeingTargetTicker;
            } else {
                --this.seeingTargetTicker;
            }

            double d = this.actor.distanceToSqr(livingEntity);
            boolean bl3 = (d > (double) this.squaredRange || this.seeingTargetTicker < 5) && this.chargedTicksLeft == 0;
            if (bl3) {
                this.actor.getNavigation().moveTo(livingEntity, this.isUncharged() ? this.speed : this.speed * 0.5D);
            } else {
                this.actor.getNavigation().stop();
            }

            //
            if (d > (double) (this.squaredRange * 0.75F)) {
                this.backward = false;
            } else if (d < (double) (this.squaredRange * 0.25F)) {
                this.backward = true;
            }
            if ((double) this.actor.getRandom().nextFloat() < 0.3D) {
                this.movingToLeft = !this.movingToLeft;
            }
            this.actor.getMoveControl().strafe(this.backward ? -0.5F : 0.5F, this.movingToLeft ? 0.5F : -0.5F);

            //

            this.actor.getLookControl().setLookAt(livingEntity, 30.0F, 30.0F);
            if (this.stage == CrossbowAttack.Stage.UNCHARGED) {
                if (!bl3) {
                    this.actor.startUsingItem(ProjectileHelper.getWeaponHoldingHand(this.actor, Items.CROSSBOW));
                    this.stage = CrossbowAttack.Stage.CHARGING;
                    this.actor.setChargingCrossbow(true);
                }
            } else if (this.stage == CrossbowAttack.Stage.CHARGING) {
                if (!this.actor.isUsingItem()) {
                    this.stage = CrossbowAttack.Stage.UNCHARGED;
                }

                int i = this.actor.getTicksUsingItem();
                ItemStack itemStack = this.actor.getUseItem();
                if (i >= CrossbowItem.getChargeDuration(itemStack)) {
                    this.actor.releaseUsingItem();
                    this.stage = CrossbowAttack.Stage.CHARGED;
                    this.chargedTicksLeft = 20 + this.actor.getRandom().nextInt(20);
                    this.actor.setChargingCrossbow(false);
                }
            } else if (this.stage == CrossbowAttack.Stage.CHARGED) {
                --this.chargedTicksLeft;
                if (this.chargedTicksLeft == 0) {
                    this.stage = CrossbowAttack.Stage.READY_TO_ATTACK;
                }
            } else if (this.stage == CrossbowAttack.Stage.READY_TO_ATTACK && canSeeTarget) {
                this.actor.performRangedAttack(livingEntity, 1.0F);
                ItemStack heldItem = this.actor.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this.actor, Items.CROSSBOW));
                CrossbowItem.setCharged(heldItem, false);
                this.stage = CrossbowAttack.Stage.UNCHARGED;
            }

        }
    }

    private boolean isUncharged() {
        return this.stage == CrossbowAttack.Stage.UNCHARGED;
    }

    enum Stage {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;
    }
}
