package dev.buildtool.scp.human;

import dev.buildtool.scp.goals.GoalAction;

public class ActivateGoal {
    public GoalAction goalAction;
    public int humanId;

    public ActivateGoal() {
    }

    public ActivateGoal(GoalAction goalAction, int humanId) {
        this.goalAction = goalAction;
        this.humanId = humanId;
    }
}
