package dev.buildtool.scp.human;

import dev.buildtool.satako.gui.BetterButton;
import dev.buildtool.satako.gui.Screen2;
import dev.buildtool.scp.SCP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class HumanHireScreen extends Screen2 {
    private final BlockPos blockPos;

    public HumanHireScreen(ITextComponent title, BlockPos blockPos) {
        super(title);
        this.blockPos = blockPos;
    }

    @Override
    public void init() {
        super.init();
        addButton(new BetterButton(centerX, 60, new TranslationTextComponent("scp.hire.male.commoner"), p_onPress_1_ -> {
            SCP.channel.sendToServer(new SpawnHuman(true, blockPos.above()));
            onClose();
        }));
        addButton(new BetterButton(centerX, 80, new TranslationTextComponent("scp.hire.female.commoner"), p_onPress_1_ -> {
            SCP.channel.sendToServer(new SpawnHuman(false, blockPos.above()));
            onClose();
        }));
    }


}
