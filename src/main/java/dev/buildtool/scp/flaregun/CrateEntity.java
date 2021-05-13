package dev.buildtool.scp.flaregun;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.satako.ItemHandler;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class CrateEntity extends BlockEntity2 {
    public ItemHandler itemHandler = new ItemHandler(1);

    public CrateEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        p_189515_1_.put("Item", itemHandler.serializeNBT());
        return super.save(p_189515_1_);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        itemHandler.deserializeNBT(p_230337_2_.getCompound("Item"));
    }
}
