package dev.buildtool.scp.scps.mailbox;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.satako.ItemHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MailboxEntity extends BlockEntity2 implements INamedContainerProvider {
    BlockPos destination = BlockPos.ZERO;
    ItemHandler itemHandler = new ItemHandler(1);
    LazyOptional<ItemHandler> lazyOptional = LazyOptional.of(() -> itemHandler);
    public int prevX, prevY, prevZ;

    public MailboxEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("SCP-3821");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeBlockPos(getBlockPos());
        return new MailboxContainer(p_createMenu_1_, p_createMenu_2_, packetBuffer);
    }

    @Override
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        p_189515_1_.putLong("Destination", destination.asLong());
        p_189515_1_.put("Item", itemHandler.serializeNBT());
        p_189515_1_.putInt("X", prevX);
        p_189515_1_.putInt("Y", prevY);
        p_189515_1_.putInt("Z", prevZ);
        return super.save(p_189515_1_);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        destination = BlockPos.of(p_230337_2_.getLong("Destination"));
        itemHandler.deserializeNBT(p_230337_2_.getCompound("Item"));
        prevX = p_230337_2_.getInt("X");
        prevY = p_230337_2_.getInt("Y");
        prevZ = p_230337_2_.getInt("Z");
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return lazyOptional.cast();
        return super.getCapability(cap);
    }
}
