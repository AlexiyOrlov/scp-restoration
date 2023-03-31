package dev.buildtool.scp.harddrivecracker;

import dev.buildtool.satako.Container2;
import dev.buildtool.satako.ItemHandlerSlot;
import dev.buildtool.scp.events.SCPContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

public class HardDriveCrackerContainer extends Container2 {
    HardDriveCrackerEntity entity;
    public HardDriveCrackerContainer(int i, PlayerInventory inventory, PacketBuffer buffer) {
        super(SCPContainers.hardDriveCrackerContainer, i);
        entity= (HardDriveCrackerEntity) inventory.player.level.getBlockEntity(buffer.readBlockPos());
        addSlot(new ItemHandlerSlot(entity.itemHandler, 0,20*4,0));
    }
}
