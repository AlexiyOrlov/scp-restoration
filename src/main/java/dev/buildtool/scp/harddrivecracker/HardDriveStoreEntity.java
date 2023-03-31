package dev.buildtool.scp.harddrivecracker;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.scp.events.SCPItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class HardDriveStoreEntity extends BlockEntity2 {
    public ItemStack hardDrive=new ItemStack(SCPItems.scpHardDrive);
    public HardDriveStoreEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        p_189515_1_.put("Drive",hardDrive.serializeNBT());
        return super.save(p_189515_1_);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        hardDrive.deserializeNBT(p_230337_2_.getCompound("Drive"));
    }
}
