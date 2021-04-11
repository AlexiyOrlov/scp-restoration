package dev.buildtool.scp.slidingdoor;

import dev.buildtool.satako.blocks.BlockHorizontal;
import dev.buildtool.scp.events.SCPTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SlidingDoorBlock extends BlockHorizontal {
    VoxelShape shapeX=Block.box(0,0,6,16,16,10);
    VoxelShape shapeZ=Block.box(6,0,0,10,16,16);
    static EnumProperty<Half> half=BlockStateProperties.HALF;
    public SlidingDoorBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(half,Half.BOTTOM));
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.getValue(half)==Half.BOTTOM;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.slidingDoorEntity.create();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(half);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {

        SlidingDoorEntity doorEntity= (SlidingDoorEntity) worldIn.getBlockEntity(state.getValue(half)==Half.TOP?pos.below():pos);
        if(doorEntity!=null) {
            if(doorEntity.opening) {
                switch (state.getValue(FACING)) {
                    case SOUTH:
                        return shapeX.move(1-doorEntity.openTime/16f,0,0);
                    case EAST:
                        return shapeZ.move(0,0,doorEntity.openTime/16f-1);
                    case NORTH:
                        return shapeX.move(doorEntity.openTime/16f-1, 0, 0);
                    case WEST:
                        return shapeZ.move(0,0,1-doorEntity.openTime/16f);
                }
            }
            else{
                switch (state.getValue(FACING)) {
                    case SOUTH:
                        return shapeX.move(doorEntity.closeTime/16f,0,0);
                    case EAST:
                        return shapeZ.move(0,0,-doorEntity.closeTime/16f);
                    case NORTH:
                        return shapeX.move(-doorEntity.closeTime/16f, 0, 0);
                    case WEST:
                        return shapeZ.move(0,0,doorEntity.closeTime/16f);
                }
            }
        }
        return VoxelShapes.block();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext itemUseContext) {
        World world=itemUseContext.getLevel();
        BlockPos pos=itemUseContext.getClickedPos();
        if (world.getBlockState(pos).canBeReplaced(itemUseContext) && world.getBlockState(pos.above()).canBeReplaced(itemUseContext))
            return super.getStateForPlacement(itemUseContext);
        return null;
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        worldIn.setBlockAndUpdate(pos.above(),state.setValue(half,Half.TOP));
    }

    @Override
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        super.playerWillDestroy(worldIn, pos, state, player);
        if(state.getValue(half)==Half.BOTTOM)
            worldIn.removeBlock(pos.above(),false);
        else
            worldIn.removeBlock(pos.below(),false);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        Direction direction = state.getValue(FACING);
        BlockState opposite = worldIn.getBlockState(pos.relative(direction.getClockWise()));
        Half half = state.getValue(SlidingDoorBlock.half);
        SlidingDoorEntity doorEntity = (SlidingDoorEntity) worldIn.getBlockEntity(half == Half.TOP ? pos.below() : pos);
        if (doorEntity != null) {
            if (worldIn.hasNeighborSignal(pos) || blockIn == Blocks.BARRIER) {
                doorEntity.opening = true;
                doorEntity.closing = false;
                worldIn.blockEvent(pos, this, 0, Math.min(doorEntity.openTime, 16));
                if (half == Half.TOP)
                    worldIn.blockEvent(pos.below(), this, 0, Math.min(16, doorEntity.openTime));
                //open adjacent door
                if (opposite.getBlock() == this && opposite.getValue(FACING) == direction.getOpposite()) {
                    neighborChanged(state, worldIn, pos.relative(direction.getClockWise()), Blocks.BARRIER, pos, isMoving);
                }
            } else if(!worldIn.hasNeighborSignal(pos) || blockIn==Blocks.STRUCTURE_VOID) {
                doorEntity.closing = true;
                doorEntity.opening = false;
                worldIn.blockEvent(pos, this, 1, Math.min(16, doorEntity.closeTime));
                if (half == Half.TOP)
                    worldIn.blockEvent(pos.below(), this, 1, Math.min(16, doorEntity.closeTime));

                //close adjacent door
                if (opposite.getBlock() == this && opposite.getValue(FACING) == direction.getOpposite()) {
                    neighborChanged(state, worldIn, pos.relative(direction.getClockWise()), Blocks.STRUCTURE_VOID, pos, isMoving);
                }
            }
        }
    }


//    @Override
//    public boolean isTransparent(BlockState state) {
//        return true;
//    }
//
//    @Override
//    public boolean isVariableOpacity() {
//        return true;
//    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        SlidingDoorEntity entity= getEntity(worldIn,state,pos);
        return entity != null && entity.opening;
    }

//    @Override
//    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
//        return 1;
//    }

    private SlidingDoorEntity getEntity(IBlockReader world,BlockState state,BlockPos pos)
    {
        return (SlidingDoorEntity) world.getBlockEntity(state.getValue(half)==Half.TOP ? pos.below():pos);
    }
}
