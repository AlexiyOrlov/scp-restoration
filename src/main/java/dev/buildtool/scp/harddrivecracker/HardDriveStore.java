package dev.buildtool.scp.harddrivecracker;

import dev.buildtool.scp.registration.SCPTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HardDriveStore extends Block {
    public HardDriveStore(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public void onRemove(BlockState p_196243_1_, World world, BlockPos blockPos, BlockState p_196243_4_, boolean p_196243_5_) {
        HardDriveStoreEntity storeEntity = (HardDriveStoreEntity) world.getBlockEntity(blockPos);
        if (!world.isClientSide) {
            if (!storeEntity.hardDrive.isEmpty()) {
                world.addFreshEntity(new ItemEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 2, blockPos.getZ() + 0.5, storeEntity.hardDrive.copy()));
            }
        }
        super.onRemove(p_196243_1_, world, blockPos, p_196243_4_, p_196243_5_);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.hardDriveStore.create();
    }
}
