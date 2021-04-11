package dev.buildtool.scp.lamp;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class SwitchableLamp extends SmallLamp {
    public SwitchableLamp(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(on, false));
    }

    public static final BooleanProperty on = BlockStateProperties.LIT;

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(on);
    }

    @Override
    public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        worldIn.getBlockTicks().scheduleTick(pos, this, 20);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (worldIn.isDay()) {
            if (!state.getValue(on)) {
                worldIn.setBlockAndUpdate(pos, state.setValue(on, true));
            }
        } else {
            if (state.getValue(on))
                worldIn.setBlockAndUpdate(pos, state.setValue(on, false));
        }
        worldIn.getBlockTicks().scheduleTick(pos, this, 20);
    }
}
