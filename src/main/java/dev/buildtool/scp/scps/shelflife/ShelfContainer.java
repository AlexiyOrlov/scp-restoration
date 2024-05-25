package dev.buildtool.scp.scps.shelflife;

import dev.buildtool.satako.Container2;
import dev.buildtool.satako.ItemHandlerSlot;
import dev.buildtool.scp.registration.SCPContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;

public class ShelfContainer extends Container2 {
    public ShelfEntity shelfEntity;

    public ShelfContainer(int i, PlayerInventory inventory, PacketBuffer packetBuffer) {
        super(SCPContainers.shelfContainer, i);
        PlayerEntity playerEntity = inventory.player;
        ;
        shelfEntity = (ShelfEntity) playerEntity.level.getBlockEntity(packetBuffer.readBlockPos());
        for (int j = 0; j < shelfEntity.books.size(); j++) {
            addSlot(new ItemHandlerSlot(shelfEntity.books.get(j), 0, 18 * j, 0) {

                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() == Items.ENCHANTED_BOOK;
                }
            });
        }

        addPlayerInventory(playerEntity, 0, 40);
    }
}
