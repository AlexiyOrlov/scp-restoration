package dev.buildtool.scp.lootblock;

import dev.buildtool.satako.gui.BetterButton;
import dev.buildtool.satako.gui.Screen2;
import dev.buildtool.satako.gui.TextField;
import dev.buildtool.scp.SCP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class LootBlockScreen extends Screen2 {
    private final LootBlockEntity lootBlockEntity;

    public LootBlockScreen(ITextComponent title, LootBlockEntity lootBlockEntity) {
        super(title);
        this.lootBlockEntity = lootBlockEntity;
    }

    @Override
    public void init() {
        super.init();
        TextField textField = addButton(new TextField(centerX, 100, new StringTextComponent(lootBlockEntity.identifier), 150));
        addButton(new BetterButton(centerX, 120, new StringTextComponent("Set identifier"), p_onPress_1_ -> {
            lootBlockEntity.identifier = textField.getValue();
            SCP.channel.sendToServer(new SetIdentifier(textField.getValue(), lootBlockEntity.getBlockPos()));
            minecraft.player.closeContainer();
        }));
    }
}
