package dev.buildtool.scp.goals;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class AdditionalMeleeAttack extends BetterMeleeAttackGoal {
    protected float damage;

    public AdditionalMeleeAttack(CreatureEntity creature, double speedIn, boolean useLongMemory, int timeBetweenAttacks, float damage) {
        super(creature, speedIn, useLongMemory, timeBetweenAttacks);
        this.damage = damage;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= d0 && canAttack()) {
            this.resetAttackCooldown();
            this.attacker.swing(Hand.MAIN_HAND);
            float f = damage;
            float knockBack = 0;
            LivingEntity attackTarget = this.attacker.getTarget();
            if (attackTarget != null) {
                f += EnchantmentHelper.getDamageBonus(attacker.getMainHandItem(), attackTarget.getMobType());
                knockBack += (float) EnchantmentHelper.getKnockbackBonus(attacker.getTarget());


                int i = EnchantmentHelper.getFireAspect(attacker);
                if (i > 0) {
                    attackTarget.setRemainingFireTicks(i * 4);
                }

                boolean flag = attackTarget.hurt(DamageSource.mobAttack(attacker), f);
                if (flag) {
                    if (knockBack > 0.0F) {
                        attackTarget.knockback(knockBack * 0.5F, MathHelper.sin(attacker.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(attacker.yRot * ((float) Math.PI / 180F)));
                        attacker.setDeltaMovement(attacker.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                    }


                    attacker.doEnchantDamageEffects(attacker, attackTarget);
                    attacker.setLastHurtMob(attackTarget);
                }
            }
        }
    }
}
