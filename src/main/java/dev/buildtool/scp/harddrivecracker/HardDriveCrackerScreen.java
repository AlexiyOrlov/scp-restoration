package dev.buildtool.scp.harddrivecracker;

import dev.buildtool.satako.gui.BetterButton;
import dev.buildtool.satako.gui.ContainerScreen2;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class HardDriveCrackerScreen extends ContainerScreen2<HardDriveCrackerContainer> {
    HardDriveCrackerContainer container;
    public HardDriveCrackerScreen(HardDriveCrackerContainer container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name, true);
        this.container=container;
    }

    @Override
    public void init() {
        super.init();
        BetterButton startCracking=new BetterButton(centerX, centerY, new TranslationTextComponent("scp.start.cracking.drive"), button -> {

        });
        addButton(startCracking);
    }
}
