package dev.buildtool.scp.table;

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

public class TableEntity extends BlockEntity2 implements LootContainer {
    ItemHandler itemHandler=new ItemHandler(1,this);
    LazyOptional<ItemHandler> lazyOptional;
    ProxyInventory proxyInventory=new ProxyInventory(itemHandler);
    public TableEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        lazyOptional=LazyOptional.of(() -> itemHandler);
    }

    @Override
    public IInventory provide() {
        return proxyInventory;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put(ITEMS, itemHandler.serializeNBT());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        itemHandler.deserializeNBT(nbt.getCompound(ITEMS));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? lazyOptional.cast() : super.getCapability(cap);
    }
}
