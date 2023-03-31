package dev.buildtool.scp.harddrivecracker;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.buildtool.satako.Functions;
import dev.buildtool.satako.gui.BetterButton;
import dev.buildtool.satako.gui.ContainerScreen2;
import dev.buildtool.satako.gui.Label;
import dev.buildtool.scp.SCP;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DateFormat;
import java.util.Date;

public class HardDriveCrackerScreen extends ContainerScreen2<HardDriveCrackerContainer> {
    HardDriveCrackerContainer container;
    public HardDriveCrackerScreen(HardDriveCrackerContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name, true);
        this.container=container;
    }

    @Override
    public void init() {
        super.init();
        if(container.entity.time==Functions.minutesToTicks(15)) {
            TranslationTextComponent translationTextComponent = new TranslationTextComponent("scp.start.cracking.drive");
            BetterButton startCracking = new BetterButton(centerX-font.width(translationTextComponent.getString())/2, 40, translationTextComponent, button -> {
                if(!container.entity.itemHandler.isEmpty()) {
                    SCP.channel.sendToServer(new StartCracking(container.entity.getBlockPos()));
                    button.active = false;
                }
            });
            addButton(startCracking);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
        super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
        if(!container.entity.itemHandler.isEmpty()) {
            int secondsPart = Functions.ticksToSeconds(container.entity.time) - Functions.ticksToMinutes(container.entity.time) * 60;
            drawCenteredString(matrixStack, font, new TranslationTextComponent("scp.time.left", Functions.ticksToMinutes(container.entity.time) + ":" + (secondsPart > 9 ? secondsPart : "0" + secondsPart)), centerX, 20, 0xffffffff);
        }
    }
}
