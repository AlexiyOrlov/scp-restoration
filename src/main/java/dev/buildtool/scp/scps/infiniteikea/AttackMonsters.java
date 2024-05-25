package dev.buildtool.scp.scps.infiniteikea;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

public class AttackMonsters extends NearestAttackableTargetGoal<IkeaMonster> {
    public AttackMonsters(MobEntity goalOwnerIn, boolean checkSight, boolean nearbyOnlyIn) {
        super(goalOwnerIn, IkeaMonster.class, checkSight, nearbyOnlyIn);
    }

    @Override
    public boolean canUse() {
        return mob.level.isNight() && super.canUse();
    }

}
