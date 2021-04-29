package dev.buildtool.scp.goals;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Hand;

public class TridentAttack extends RangedAttackGoal {

    protected MobEntity mobEntity;

    public TridentAttack(IRangedAttackMob p_i1649_1_, double speed, int interval, float range) {
        this(p_i1649_1_, speed, interval, interval, range);
        mobEntity = (MobEntity) p_i1649_1_;
    }

    public TridentAttack(IRangedAttackMob p_i1650_1_, double p_i1650_2_, int p_i1650_4_, int p_i1650_5_, float p_i1650_6_) {
        super(p_i1650_1_, p_i1650_2_, p_i1650_4_, p_i1650_5_, p_i1650_6_);
        mobEntity = (MobEntity) p_i1650_1_;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && mobEntity.getMainHandItem().getItem() instanceof TridentItem;
    }

    @Override
    public void start() {
        super.start();
        mobEntity.setAggressive(true);
        mobEntity.startUsingItem(Hand.MAIN_HAND);
    }

    @Override
    public void stop() {
        super.stop();
        mobEntity.stopUsingItem();
        mobEntity.setAggressive(false);
    }
}
