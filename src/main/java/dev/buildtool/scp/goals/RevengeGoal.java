package dev.buildtool.scp.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;

import java.util.function.Predicate;

public class RevengeGoal extends HurtByTargetGoal {
    Predicate<LivingEntity> filter;

    public RevengeGoal(CreatureEntity creatureIn, Predicate<LivingEntity> dontTouch, Class<?>... excludeReinforcementTypes) {
        super(creatureIn, excludeReinforcementTypes);
        filter = dontTouch;
    }

    @Override
    public boolean canUse() {
        if (filter != null && this.mob.getTarget() != null && filter.test(mob.getTarget()))
            return false;
        return super.canUse();
    }
}
