package dev.buildtool.scp.crate;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.satako.ItemHandler;
import dev.buildtool.scp.LootContainer;
import dev.buildtool.scp.ProxyInventory;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrateEntity extends BlockEntity2 implements INamedContainerProvider, LootContainer {
    public ItemHandler itemHandler;
    LazyOptional<IItemHandler> lazyOptional;
    private ProxyInventory proxyInventory;

    public CrateEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        itemHandler=new ItemHandler(36,this);
        lazyOptional=LazyOptional.of(() -> itemHandler);
        proxyInventory = new ProxyInventory(itemHandler);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap== CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? lazyOptional.cast():super.getCapability(cap);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put(ITEMS,itemHandler.serializeNBT());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        itemHandler.deserializeNBT(nbt.getCompound(ITEMS));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        PacketBuffer packetBuffer=new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeBlockPos(getBlockPos());
        return new CrateContainer(p_createMenu_1_,p_createMenu_2_,packetBuffer);
    }

    @Override
    public IInventory provide() {
        return proxyInventory;
    }

}
