package dev.buildtool.scp.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Predicate;

public class Assist<M extends MobEntity, L extends LivingEntity> extends NamedGoal {

    M helper;
    L attacker;
    Class<L> helped;
    Predicate<L> filter;

    public Assist(M helper, Class<L> helped, Predicate<L> filter) {
        this.helper = helper;
        this.helped = helped;
        this.filter = filter;
    }

    @Override
    public GoalAction getAction() {
        return GoalAction.ASSIST;
    }

    @Override
    public boolean canUse() {
        return isOn();
    }

    @Override
    public void start() {
        helper.level.getEntitiesOfClass(helped, new AxisAlignedBB(helper.blockPosition()).inflate(helper.getAttributeValue(Attributes.FOLLOW_RANGE)), filter).stream().findFirst().ifPresent(l -> attacker = l);
    }

    @Override
    public void tick() {
        super.tick();
        if (attacker != null) {
            if (attacker.getLastHurtMob() != null && attacker.getLastHurtMob() != helper) {
                helper.setTarget(attacker.getLastHurtMob());
            } else if (helper.distanceTo(attacker) > 4) {
                helper.getNavigation().moveTo(attacker, 1);
            } else
                helper.getNavigation().stop();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return attacker != null && attacker.isAlive() && isOn();
    }

    @Override
    public void stop() {
        super.stop();
        attacker = null;
        helper.setTarget(null);
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("scp.assist.goal");
    }
}
