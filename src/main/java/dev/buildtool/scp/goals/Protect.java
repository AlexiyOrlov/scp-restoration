package dev.buildtool.scp.goals;

import dev.buildtool.scp.human.Human;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.function.Predicate;

public class Protect<M extends MobEntity, L extends LivingEntity> extends NamedGoal {

    M protector;
    Class<L> protectedClass;
    L protectee;
    Predicate<L> filter;

    public Protect(M protector, Class<L> protectedClass, Predicate<L> filter) {
        this.protector = protector;
        this.protectedClass = protectedClass;
        this.filter = filter;
    }

    @Override
    public GoalAction getAction() {
        return GoalAction.PROTECT_OWNER;
    }

    @Override
    public boolean canUse() {
        return isOn();
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("scp.protect.owner.goal");
    }

    @Override
    public void start() {
        List<L> pro = protector.level.getEntitiesOfClass(protectedClass, new AxisAlignedBB(protector.blockPosition()).inflate(protector.getAttributeValue(Attributes.FOLLOW_RANGE)), filter);
        pro.stream().findAny().ifPresent(l -> protectee = l);
    }

    @Override
    public boolean canContinueToUse() {
        return isOn() && protectee != null && protectee.isAlive();
    }

    @Override
    public void tick() {
        if (protectee != null) {
            LivingEntity revengeTarget = protectee.getLastHurtByMob();
            if (revengeTarget != null && revengeTarget != protector && !(revengeTarget instanceof Human && protectee.getUUID().equals(((Human) revengeTarget).getOwner()))) {
                protector.setTarget(revengeTarget);
            } else {
                protector.setTarget(null);
                if (protector.distanceTo(protectee) > 4) {
                    protector.getNavigation().moveTo(protectee, 1);
                } else {
                    protector.getNavigation().stop();
                }
            }
        }
    }

    @Override
    public void stop() {
        protectee = null;
        protector.setTarget(null);
    }
}
