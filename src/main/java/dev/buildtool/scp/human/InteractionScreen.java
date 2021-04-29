package dev.buildtool.scp.human;

import dev.buildtool.satako.gui.*;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.goals.GoalAction;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class InteractionScreen extends ContainerScreen2<InteractionContainer> {
    private Human human;

    public InteractionScreen(InteractionContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name, true);
        human = container.human;
    }

    ButtonGroup buttonGroupGoals;


    @Override
    public void init() {
        super.init();
        Label name = addButton(new Label(10, 10, human.getDisplayName()));
        addButton(new Label(name.x + name.getWidth() + 10, 10, new StringTextComponent((int) human.getHealth() + "/" + human.getMaxHealth() + " health")));
        addButton(new Label(10, 20, new StringTextComponent("ID: " + human.getUUID())));
        addButton(new Label(10, height - 20, new StringTextComponent("Range: " + human.getAttribute(Attributes.FOLLOW_RANGE).getValue())));
        if (human.hasOwner()) {
            addButton(new Label(10, 30, new TranslationTextComponent("scp.owner").append(": ").append(minecraft.level.getPlayerByUUID(human.getOwner()).getName()).append(" - " + human.getOwner().toString())));
            if (human.getOwner().equals(minecraft.player.getUUID())) {
                buttonGroupGoals = new ButtonGroup();
                int v = 3;
                for (GoalAction goalAction : GoalAction.values()) {
                    RadioButton radioButton = new RadioButton(10, v++ * 20, new StringTextComponent(goalAction.name()));
                    addButton(radioButton);
                    buttonGroupGoals.add(radioButton);
                }
                buttonGroupGoals.connect();
                buttonGroupGoals.getButtons().stream().filter(radioButton -> human.getActiveCommand() == GoalAction.valueOf(radioButton.string)).findAny().ifPresent(buttonGroupGoals::setSelected);
                addButton(new BetterButton(10, v++ * 20, new TranslationTextComponent("scp.sorry.me"), p_onPress_1_ -> {
                    SCP.channel.sendToServer(new SorryPlayer(human.getId()));
                }));
            }
        } else {
            addButton(new BetterButton(centerX, 50, new TranslationTextComponent("scp.claim"), p_onPress_1_ -> {
                human.setOwner(minecraft.player.getUUID());
                SCP.channel.sendToServer(new SetOwner(human.getId(), minecraft.player.getUUID()));
                this.onClose();
            }));
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        if (buttonGroupGoals != null) {
            RadioButton selected = buttonGroupGoals.getSelected();
            if (selected != null) {
                GoalAction goalAction = GoalAction.valueOf(selected.string);
                //send-activate goal
                if (human.getActiveCommand() != goalAction) {
                    human.setActiveCommand(goalAction);
                    SCP.channel.sendToServer(new ActivateGoal(goalAction, human.getId()));
                }
            }
        }
    }
}
