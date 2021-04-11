package dev.buildtool.scp.clockworks;

import dev.buildtool.satako.gui.*;
import dev.buildtool.scp.SCP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ClockworksScreen extends Screen2 {
    private ClockworksEntity clockworks;
    SwitchButton autoInput;
    ButtonGroup buttonGroup;
    public ClockworksScreen(ClockworksEntity clockworksEntity,ITextComponent title) {
        super(title);
        this.clockworks = clockworksEntity;
    }

    @Override
    public void init() {
        super.init();
        String oneToOne="1 to 1";
        int x=font.width(oneToOne)/2;
        RadioButton rough=new RadioButton(centerX-x,60,new StringTextComponent("Rough"));
        RadioButton coarse=new RadioButton(centerX-x,80,new StringTextComponent("Coarse"));
        RadioButton oneone=new RadioButton(centerX-x,100,new StringTextComponent(oneToOne));
        RadioButton fine=new RadioButton(centerX-x,120,new StringTextComponent("Fine"));
        RadioButton veryFine=new RadioButton(centerX-x,140,new StringTextComponent("Very fine"));

        buttonGroup = new ButtonGroup(rough, coarse, oneone, fine, veryFine);
        buttonGroup.connect();
        buttonGroup.getButtons().forEach(this::addButton);
        buttonGroup.setSelected(clockworks.mode.ordinal());

        BetterButton start = new BetterButton(centerX - x, 180, new StringTextComponent("Start"), ip -> {
            clockworks.working = true;
            minecraft.setScreen(null);
        });
        addButton(start);
        autoInput = addButton(new SwitchButton(centerX - x, 200, new StringTextComponent("Auto-input on"), new StringTextComponent("Auto-input off"), clockworks.autoInput, p_onPress_1_ -> {
            clockworks.autoInput = !clockworks.autoInput;
            autoInput.state = !autoInput.state;
        }));

    }

    @Override
    public void onClose() {
        RadioButton radioButton = buttonGroup.getSelected();
        ClockworksRecipe.Mode mode = ClockworksRecipe.Mode.values()[buttonGroup.getButtons().indexOf(radioButton)];
        clockworks.mode = mode;
        SCP.channel.sendToServer(new Settings(clockworks.autoInput, mode, clockworks.working, clockworks.getBlockPos()));
        super.onClose();
    }
}
