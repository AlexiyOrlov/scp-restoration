package dev.buildtool.scp.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;
import java.util.function.Predicate;

public class RevengeGoal extends HurtByTargetGoal {
    Predicate<LivingEntity> filter;
    public RevengeGoal(CreatureEntity creatureIn, Predicate<LivingEntity> dontTouch, Class<?>... excludeReinforcementTypes) {
        super(creatureIn, excludeReinforcementTypes);
        filter = dontTouch;
    }

    @Override
    public boolean canUse() {
        if (filter != null && this.mob.getLastHurtByMob() != null && filter.test(mob.getLastHurtByMob()))
            return false;
        return super.canUse();
    }

    @Override
    protected void alertOthers() {
        super.alertOthers();
        AxisAlignedBB axisalignedbb = AxisAlignedBB.unitCubeFromLowerCorner(this.mob.position()).inflate(getFollowDistance(), 10.0D, getFollowDistance());
        List<MobEntity> list = this.mob.level.getLoadedEntitiesOfClass(this.mob.getClass(), axisalignedbb);
        list.forEach(mobEntity -> {
            if (mobEntity.getTarget() == null)
                mobEntity.setTarget(mob.getLastHurtByMob());
        });
    }
}
