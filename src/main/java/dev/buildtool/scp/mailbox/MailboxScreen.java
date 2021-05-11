package dev.buildtool.scp.mailbox;

import dev.buildtool.satako.gui.BetterButton;
import dev.buildtool.satako.gui.ContainerScreen2;
import dev.buildtool.satako.gui.Label;
import dev.buildtool.satako.gui.TextField;
import dev.buildtool.scp.SCP;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

public class MailboxScreen extends ContainerScreen2<MailboxContainer> {
    MailboxContainer mailboxContainer;

    public MailboxScreen(MailboxContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name, true);
        mailboxContainer = container;
    }

    @Override
    public void init() {
        super.init();
        Label hint = addButton(new Label(leftPos, topPos, new TranslationTextComponent("scp.select.position")));
        Label x = addButton(new Label(leftPos, hint.y + hint.getHeight(), new StringTextComponent("X")));
        Label y = addButton(new Label(leftPos, x.y + x.getHeight(), new StringTextComponent("Y")));
        Label z = addButton(new Label(leftPos, y.y + y.getHeight(), new StringTextComponent("Z")));

        TextField textFieldX = new TextField(x.x + x.getWidth(), x.y, new StringTextComponent(""), 60);
        TextField textFieldY = new TextField(y.x + y.getWidth(), y.y, new StringTextComponent(""), 60);
        TextField textFieldZ = new TextField(z.x + z.getWidth(), z.y, new StringTextComponent(""), 60);
        addButton(textFieldX);
        addButton(textFieldY);
        addButton(textFieldZ);

        BetterButton send = new BetterButton(leftPos, textFieldZ.y + textFieldZ.getHeight(), new TranslationTextComponent("scp.send"), p_onPress_1_ -> {
            if (!mailboxContainer.mailboxEntity.itemHandler.isEmpty()) {
                try {
                    BlockPos blockPos = new BlockPos(Integer.parseInt(textFieldX.getValue()), Integer.parseInt(textFieldY.getValue()), Integer.parseInt(textFieldZ.getValue()));
                    SCP.channel.sendToServer(new SendMail(mailboxContainer.mailboxEntity.getBlockPos(), blockPos));
                    onClose();
                } catch (NumberFormatException e) {
                    minecraft.player.sendMessage(new TranslationTextComponent("scp.invalid.value"), UUID.randomUUID());
                }
            }
        });
        addButton(send);
    }
}
