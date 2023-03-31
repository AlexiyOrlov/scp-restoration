package dev.buildtool.scp.harddrivecracker;

import dev.buildtool.satako.Container2;
import dev.buildtool.satako.ItemHandlerSlot;
import dev.buildtool.scp.registration.SCPContainers;
import dev.buildtool.scp.registration.SCPItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class HardDriveCrackerContainer extends Container2 {
    HardDriveCrackerEntity entity;
    public HardDriveCrackerContainer(int i, PlayerInventory inventory, PacketBuffer buffer) {
        super(SCPContainers.hardDriveCrackerContainer, i);
        entity= (HardDriveCrackerEntity) inventory.player.level.getBlockEntity(buffer.readBlockPos());
        addSlot(new ItemHandlerSlot(entity.itemHandler, 0,20*4-8,0));
        addPlayerInventory(0,40,inventory);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack stack=getSlot(index).getItem();
        if(stack.getItem()== SCPItems.scpHardDrive)
        {
            if(!moveItemStackTo(stack,0,1,false))
                return ItemStack.EMPTY;
        }
        return super.quickMoveStack(playerIn, index);
    }
}
