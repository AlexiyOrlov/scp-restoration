package dev.buildtool.scp.human;

import dev.buildtool.satako.Container2;
import dev.buildtool.satako.ItemHandler;
import dev.buildtool.satako.gui.ItemHandlerDisplaySlot;
import dev.buildtool.scp.registration.SCPContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class InteractionContainer extends Container2 {
    public Human human;

    public InteractionContainer(int i, PlayerInventory inventory, PacketBuffer packetBuffer) {
        super(SCPContainers.humanInterContainer, i);
        human = (Human) inventory.player.level.getEntity(packetBuffer.readInt());
        int g = 0;
        ItemHandler itemHandler = new ItemHandler(6);
        for (ItemStack itemStack : human.getArmorSlots()) {
            itemHandler.setStackInSlot(g++, itemStack);
        }
        itemHandler.setStackInSlot(4, human.getMainHandItem());
        itemHandler.setStackInSlot(5, human.getOffhandItem());
        for (int h = 0; h < itemHandler.getSlots(); h++) {
            addSlot(new ItemHandlerDisplaySlot(itemHandler, h, 20 * h, 0));
        }
    }
}
