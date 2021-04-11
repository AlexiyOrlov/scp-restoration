package dev.buildtool.scp.shelf;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.satako.ItemHandler;
import dev.buildtool.scp.LootContainer;
import dev.buildtool.scp.ProxyInventory;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class ShelfEntity extends BlockEntity2 implements LootContainer {
    public ItemHandler itemHandler = new ItemHandler(4, this);
    ProxyInventory proxyInventory = new ProxyInventory(itemHandler);
    LazyOptional<ItemHandler> lazyOptional = LazyOptional.of(() -> itemHandler);
    public ShelfEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put(ITEMS, itemHandler.serializeNBT());
        return super.save(tag);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        itemHandler.deserializeNBT(tag.getCompound(ITEMS));
    }

    @Override
    public IInventory provide() {
        return proxyInventory;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? lazyOptional.cast() : super.getCapability(cap);
    }
}
