package dev.buildtool.scp;

import dev.buildtool.satako.ItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ProxyInventory implements IInventory {
    private final ItemHandler itemHandler;

    public ProxyInventory(ItemHandler itemHandler) {
        this.itemHandler = itemHandler;
    }

    @Override
    public int getContainerSize() {
        return itemHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        return itemHandler.isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return itemHandler.getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return itemHandler.extractItem(index, count, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return itemHandler.extractItem(index, 64, false);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        itemHandler.setStackInSlot(index, stack);
    }

    @Override
    public void setChanged() {
        itemHandler.getOwner().setChanged();
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    @Override
    public void clearContent() {
        itemHandler.getItems().clear();
    }
}
