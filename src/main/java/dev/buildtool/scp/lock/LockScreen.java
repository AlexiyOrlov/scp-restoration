package dev.buildtool.scp.lock;

import dev.buildtool.satako.gui.BetterButton;
import dev.buildtool.satako.gui.Label;
import dev.buildtool.satako.gui.Screen2;
import dev.buildtool.satako.gui.TextField;
import dev.buildtool.scp.SCP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

public class LockScreen extends Screen2 {
    private LockEntity lock;

    public LockScreen(ITextComponent title, LockEntity lockEntity) {
        super(title);
        this.lock = lockEntity;
    }

    @Override
    public void init() {
        super.init();
        if (lock.password.isEmpty() || (minecraft.player.getUUID().equals(lock.owner) && minecraft.player.isCrouching())) {
            Label label = new Label(centerX, centerY - 60, new TranslationTextComponent("scp.set.password"));
            addButton(label);
            TextField textField = addButton(new TextField(centerX, label.y + 20, new StringTextComponent(lock.password), 120));
            BetterButton setPassword = new BetterButton(centerX, textField.y + 20, new TranslationTextComponent("scp.set.new.password"), p_onPress_1_ -> {
                String password = textField.getValue();
                if (!password.isEmpty()) {
                    lock.password = password;
                    SCP.channel.sendToServer(new SetPassword(lock.getBlockPos(), password));
                    minecraft.player.sendMessage(new TranslationTextComponent("scp.password.set").append(": " + password), UUID.randomUUID());
                }
            });
            addButton(setPassword);
        } else {
            Label label = new Label(centerX, centerY - 60, new TranslationTextComponent("scp.password"));
            addButton(label);
            TextField passwordinput = new TextField(centerX, label.y + 20, 160);
            addButton(passwordinput);
            BetterButton confirm = new BetterButton(centerX, passwordinput.y + 20, new TranslationTextComponent("scp.confirm.password"), p_onPress_1_ -> {
                String input = passwordinput.getValue();
                if (!input.isEmpty()) {
                    if (input.equals(lock.password)) {
                        SCP.channel.sendToServer(new OpenLock(lock.getBlockPos()));
                        minecraft.player.closeContainer();
                    } else {
                        minecraft.player.sendMessage(new TranslationTextComponent("scp.wrong.password"), UUID.randomUUID());
                    }
                }
            });
            addButton(confirm);
        }
    }
}
