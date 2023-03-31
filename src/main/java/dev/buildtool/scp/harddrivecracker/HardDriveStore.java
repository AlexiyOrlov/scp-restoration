package dev.buildtool.scp.harddrivecracker;

import dev.buildtool.scp.events.SCPTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HardDriveStore extends Block {
    public HardDriveStore(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public ActionResultType use(BlockState p_225533_1_, World world, BlockPos blockPos, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        HardDriveStoreEntity storeEntity= (HardDriveStoreEntity) world.getBlockEntity(blockPos);
        if(!world.isClientSide){
            if(!storeEntity.hardDrive.isEmpty()){
                world.addFreshEntity(new ItemEntity(world,blockPos.getX()+0.5,blockPos.getY()+2,blockPos.getZ()+0.5,storeEntity.hardDrive.copy()));
                storeEntity.hardDrive=ItemStack.EMPTY;
            }
        }
        return ActionResultType.SUCCESS;
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
