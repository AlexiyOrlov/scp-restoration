package dev.buildtool.scp.chairs;

import dev.buildtool.satako.blocks.BlockHorizontal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class Chair extends BlockHorizontal {
    private static final VoxelShape shape = Block.box(0, 0, 0, 16, 8, 16);
    public Chair(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide) {
            Sittable sittable = new Sittable(worldIn);
            sittable.setNoGravity(true);
            sittable.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            worldIn.addFreshEntity(sittable);
            player.startRiding(sittable, true);
        }
        return ActionResultType.SUCCESS;
    }


    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return shape;
    }
}
