package dev.buildtool.scp.crate;

import dev.buildtool.satako.Container2;
import dev.buildtool.satako.ItemHandlerSlot;
import dev.buildtool.scp.events.SCPContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class CrateContainer extends Container2 {
    CrateEntity crateEntity;
    public CrateContainer(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        super(SCPContainers.crateContainer, id);
        crateEntity= (CrateEntity) playerInventory.player.level.getBlockEntity(packetBuffer.readBlockPos());
        int i=0;
        for (int j = 0; j < 9; j++) {
            for (int k = 0; k < 4; k++) {
                addSlot(new ItemHandlerSlot(crateEntity.itemHandler, i++, j * 18, k * 18));
            }
        }
        addPlayerInventory(0, 80, playerInventory);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack stack = getSlot(index).getItem();
        if (!stack.isEmpty()) {
            if (index < crateEntity.itemHandler.getSlots()) {
                if (!moveItemStackTo(stack, 36, slots.size(), false))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(stack, 0, 36, false))
                return ItemStack.EMPTY;
        }
        return super.quickMoveStack(playerIn, index);
    }
}
