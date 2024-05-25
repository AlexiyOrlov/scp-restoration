package dev.buildtool.scp.scps.oldai;

import dev.buildtool.satako.blocks.BlockHorizontal;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.registration.SCPTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SCPObject(name = "Old AI", number = "079", classification = SCPObject.Classification.EUCLID)
public class OldAIBlock extends BlockHorizontal {
    public static BooleanProperty active = BlockStateProperties.ENABLED;

    public OldAIBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(active, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(active);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.oldAIEntity.create();
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos blockPos, Block p_220069_4_, BlockPos from, boolean p_220069_6_) {
        super.neighborChanged(state, world, blockPos, p_220069_4_, from, p_220069_6_);
        if (world.hasNeighborSignal(blockPos)) {
            state = state.cycle(active);
            world.setBlockAndUpdate(blockPos, state);
        }
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
        return true; //isn't called
    }
}
