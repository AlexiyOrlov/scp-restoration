package dev.buildtool.scp.slidingdoor;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.scp.events.Sounds;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

public class SlidingDoorEntity extends BlockEntity2 implements ITickableTileEntity {
    int closeTime,openTime=16;
    boolean closing=true,opening;
    public SlidingDoorEntity(TileEntityType<SlidingDoorEntity> tileEntityType) {
        super(tileEntityType);
    }


    @Override
    public void tick() {
        if(opening)
        {
            if(openTime>0) {
                openTime--;
                closeTime++;
            }
        }
        if(closing)
        {
            if(closeTime>0) {
                closeTime--;
                openTime++;
                if (closeTime == 2) {
                    level.playLocalSound(getX(), getY(), getZ(), Sounds.doorClosed, SoundCategory.BLOCKS, 1, 0.8f, false);
                }
            }
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if(id==0)
        {
            opening=true;
            closing=false;
            openTime=type;
        }
        else if(id==1)
        {
            opening=false;
            closing=true;
            closeTime=type;
        }
        return true;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putBoolean("Closing",closing);
        compound.putBoolean("Opening",opening);
        compound.putInt("Open time",openTime);
        compound.putInt("Close time",closeTime);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        closing = nbt.getBoolean("Closing");
        opening = nbt.getBoolean("Opening");
        openTime = nbt.getInt("Open time");
        closeTime = nbt.getInt("Close time");
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos()).inflate(0, 1, 0);
    }
}
