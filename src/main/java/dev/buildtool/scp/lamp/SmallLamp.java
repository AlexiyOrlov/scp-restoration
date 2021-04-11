package dev.buildtool.scp.lamp;

import dev.buildtool.satako.blocks.BlockDirectional;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class SmallLamp extends BlockDirectional {
    private static final VoxelShape UP= VoxelShapes.box(6/16f,0,6/16f,10/16f,3/16f,10/16f),
            DOWN=VoxelShapes.box(6/16f,13/16f,6/16f,10/16f,1,10/16f),
            WEST=VoxelShapes.box(13/16f,6/16f,6/16f,1,10/16f,10/16f),
            EAST=VoxelShapes.box(0,6/16f,6/16f,3/16f,10/16f,10/16f),
            NORTH=VoxelShapes.box(6/16f,6/16f,13/16f,10/16f,10/16f,1),
            SOUTH=VoxelShapes.box(6/16f,6/16f,0,10/16f,10/16f,3/16f);
    public SmallLamp(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(FACING))
        {
            case EAST:return EAST;
            case NORTH:return NORTH;
            case SOUTH:return SOUTH;
            case UP:return UP;
            case DOWN:return DOWN;
            case WEST:return WEST;
        }
        return super.getShape(state, worldIn, pos, context);
    }
}
