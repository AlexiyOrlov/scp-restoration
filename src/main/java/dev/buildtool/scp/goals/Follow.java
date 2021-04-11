package dev.buildtool.scp.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.function.Predicate;

public class Follow<E extends MobEntity, C extends LivingEntity> extends NamedGoal {
    protected E follower;
    protected Class<C> targetClass;
    protected Predicate<C> filter;
    protected C target;

    public Follow(E follower, Class<C> targetClass, Predicate<C> filter) {
        this.follower = follower;
        this.targetClass = targetClass;
        this.filter = filter;
    }

    @Override
    public boolean canUse() {
        if (isOn()) {
            double range = follower.getAttributeValue(Attributes.FOLLOW_RANGE);
            List<C> entities = follower.level.getEntitiesOfClass(targetClass, new AxisAlignedBB(follower.blockPosition()).inflate(range));
            if (!entities.isEmpty()) {
                entities.stream().filter(filter).findAny().ifPresent(c -> target = c);
            }
        }
        return isOn() && target != null;
    }

    @Override
    public boolean canContinueToUse() {
        return isOn() && target != null && target.isAlive();
    }

    @Override
    public void tick() {
        follower.getNavigation().moveTo(target, 1);
        if (follower.distanceTo(target) < 4) {
            follower.getNavigation().stop();
        }
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("scp.follow.goal");
    }

    @Override
    public GoalAction getAction() {
        return GoalAction.FOLLOW;
    }
}
