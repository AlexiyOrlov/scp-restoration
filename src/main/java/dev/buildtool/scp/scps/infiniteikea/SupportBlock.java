package dev.buildtool.scp.scps.infiniteikea;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

/**
 * Connects with other blocks
 */
public class SupportBlock extends SixWayBlock {

    /**
     * @param apothem width
     */
    public SupportBlock(float apothem, Properties properties) {
        super(apothem, properties);
        registerDefaultState(this.stateDefinition.any().setValue(DOWN, false).setValue(UP, false).setValue(NORTH, false).setValue(SOUTH, false).setValue(EAST, false).setValue(WEST, false));
    }

    protected BlockState makeConnections(IBlockReader blockReader, BlockPos pos) {
        BlockState blockState = blockReader.getBlockState(pos.below());
        BlockState blockState1 = blockReader.getBlockState(pos.above());
        BlockState blockState2 = blockReader.getBlockState(pos.north());
        BlockState blockState3 = blockReader.getBlockState(pos.east());
        BlockState blockState4 = blockReader.getBlockState(pos.south());
        BlockState blockState5 = blockReader.getBlockState(pos.west());
        return this.defaultBlockState().setValue(DOWN, blockState.isFaceSturdy(blockReader, pos.below(), Direction.UP)).setValue(UP, blockState1.isFaceSturdy(blockReader, pos.above(), Direction.DOWN)).setValue(NORTH, blockState2.isFaceSturdy(blockReader, pos.north(), Direction.SOUTH)).setValue(EAST, blockState3.isFaceSturdy(blockReader, pos.east(), Direction.WEST)).setValue(SOUTH, blockState4.isFaceSturdy(blockReader, pos.south(), Direction.NORTH)).setValue(WEST, blockState5.isFaceSturdy(blockReader, pos.west(), Direction.EAST));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return makeConnections(context.getLevel(), context.getClickedPos());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EAST, WEST, SOUTH, NORTH, UP, DOWN);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        boolean flag = facingState.isFaceSturdy(worldIn, facingPos, facing) || facingState.getBlock() == this;
        return stateIn.setValue(PROPERTY_BY_DIRECTION.get(facing), flag);
    }
}
