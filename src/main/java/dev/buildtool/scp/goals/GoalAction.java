package dev.buildtool.scp.goals;

public enum GoalAction {
    IDLE(NamedGoal.class),
    GUARD_POSITION(GuardPosition.class),
    PROTECT_OWNER(Protect.class),
    ASSIST(Assist.class),
    PROTECT_AND_ASSIST(ProtectAndAssist.class),
    FOLLOW(Follow.class);

    public Class<? extends NamedGoal> associatedGoal;

    GoalAction(Class<? extends NamedGoal> associatedGoal) {
        this.associatedGoal = associatedGoal;
    }
}
