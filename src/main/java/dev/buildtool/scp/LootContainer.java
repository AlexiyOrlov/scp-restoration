package dev.buildtool.scp;

import net.minecraft.inventory.IInventory;

public interface LootContainer {

    /**
     * @return an inventory to operate on
     */
    IInventory provide();
}
