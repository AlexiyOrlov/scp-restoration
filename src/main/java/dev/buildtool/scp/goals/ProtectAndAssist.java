package dev.buildtool.scp.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Predicate;

public class ProtectAndAssist<T extends MobEntity, L extends LivingEntity> extends NamedGoal {
    T mob;
    L protectee;
    Predicate<L> condition;
    Class<L> protecteeClass;

    public ProtectAndAssist(T mob, Predicate<L> condition, Class<L> protecteeClass) {
        this.mob = mob;
        this.condition = condition;
        this.protecteeClass = protecteeClass;
    }

    @Override
    public GoalAction getAction() {
        return GoalAction.PROTECT_AND_ASSIST;
    }

    @Override
    public boolean canUse() {
        return isOn() && (protecteeClass != null);
    }

    @Override
    public void start() {
        mob.level.getEntitiesOfClass(protecteeClass, new AxisAlignedBB(mob.blockPosition())
                .inflate(mob.getAttributeValue(Attributes.FOLLOW_RANGE)), condition).stream().filter(condition).findAny()
                .ifPresent(l -> protectee = l);
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive()) {
            LivingEntity lastHurtMob = protectee.getLastHurtMob();
            if (lastHurtMob != null && lastHurtMob != mob && lastHurtMob.isAlive() && !(lastHurtMob instanceof CreeperEntity))
                mob.setTarget(lastHurtMob);
            else {
                LivingEntity lastHurtByMob = protectee.getLastHurtByMob();
                if (lastHurtByMob != null && lastHurtByMob != mob && lastHurtByMob.isAlive() && !(lastHurtByMob instanceof CreeperEntity))
                    mob.setTarget(lastHurtByMob);
                else if (mob.getTarget() == null && mob.distanceToSqr(protectee) > 4)
                    mob.getNavigation().moveTo(protectee, 1);
                else mob.getNavigation().stop();
            }

//            if (target != null && (!target.isAlive() || target instanceof CreeperEntity || target == mob || (target instanceof Human && protectee.getUUID().equals(((Human) target).getOwner())))) {
//                mob.setTarget(null);
//            }
        }
    }

    @Override
    public void stop() {
        mob.setTarget(null);
        protectee = null;
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("scp.protect.and.assist");
    }
}
