package dev.buildtool.scp.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.function.Predicate;

public class GuardPosition<E extends MobEntity, T extends LivingEntity> extends NamedGoal {
    protected E guard;
    protected BlockPos position;
    protected Class<T>[] targetClasses;
    protected Predicate<T> guardFrom;
    protected T target;

    public GuardPosition(E guard, Class<T>[] targetClasses, Predicate<T> guardFrom) {
        this.guard = guard;
        this.targetClasses = targetClasses;
        this.guardFrom = guardFrom;
    }

    @Override
    public boolean canUse() {
        return isOn();
    }

    @Override
    public void start() {
        super.start();
        position = guard.blockPosition();
    }

    @Override
    public void tick() {
        if (target == null || !target.isAlive()) {
            for (Class<T> targetClass : targetClasses) {
                List<T> list = guard.level.getEntitiesOfClass(targetClass, new AxisAlignedBB(position).inflate(guard.getAttributeValue(Attributes.FOLLOW_RANGE)), guardFrom.and(guard::canSee));
                if (!list.isEmpty()) {
                    target = list.get(guard.getRandom().nextInt(list.size()));
                    guard.setTarget(target);
                    break;
                }
            }
        }
        if (target == null || !target.isAlive())
            guard.getNavigation().moveTo(position.getX(), position.getY(), position.getZ(), 1);
    }

    @Override
    public void stop() {
        super.stop();
        position = null;
        guard.setTarget(null);
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("scp.guardposition.goal");
    }

    @Override
    public GoalAction getAction() {
        return GoalAction.GUARD_POSITION;
    }
}
