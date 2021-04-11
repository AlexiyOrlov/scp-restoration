package dev.buildtool.scp.goals;

import dev.buildtool.scp.Switchable;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.INameable;

public abstract class NamedGoal extends Goal implements INameable, Switchable {
    protected boolean runs;

    @Override
    public void turnOff() {
        runs = false;
    }

    @Override
    public void turnOn() {
        runs = true;
    }

    @Override
    public boolean isOn() {
        return runs;
    }

    public abstract GoalAction getAction();
}
