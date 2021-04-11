package dev.buildtool.scp.lock;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.satako.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.UUID;

public class LockEntity extends BlockEntity2 implements ITickableTileEntity {
    public String password = "";
    public UUID owner = Constants.NULL_UUID;
    public short openTime;

    public LockEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public void open() {
        BlockState blockState = level.getBlockState(getBlockPos());
        level.setBlockAndUpdate(getBlockPos(), blockState.setValue(ElectronicLock.OPEN, true));
        level.updateNeighborsAt(getBlockPos().relative(blockState.getValue(ElectronicLock.FACING).getOpposite()), getBlockState().getBlock());
        openTime = 60;
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        owner = nbt.getUUID("Owner");
        password = nbt.getString("Password");
        openTime = nbt.getShort("Time open");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putString("Password", password);
        compound.putUUID("Owner", owner);
        compound.putShort("Time open", openTime);
        return super.save(compound);
    }

    @Override
    public void tick() {
        if (openTime > 0)
            openTime--;
        else {
            if (openTime == 0) {
                BlockState state = level.getBlockState(getBlockPos());
                level.setBlockAndUpdate(getBlockPos(), state.setValue(ElectronicLock.OPEN, false));
                level.updateNeighborsAt(getBlockPos().relative(state.getValue(ElectronicLock.FACING).getOpposite()), getBlockState().getBlock());
                openTime = -1;
            }
        }
    }
}
