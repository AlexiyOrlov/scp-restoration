package dev.buildtool.scp;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class PreviewBlock extends Block {
    public PreviewBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public boolean isAir(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }

    @Override
    public void tick(BlockState p_225534_1_, ServerWorld serverWorld, BlockPos p_225534_3_, Random p_225534_4_) {
        serverWorld.removeBlock(p_225534_3_, true);
    }

    public boolean skipRendering(BlockState p_200122_1_, BlockState p_200122_2_, Direction p_200122_3_) {
        return p_200122_2_.is(this) || super.skipRendering(p_200122_1_, p_200122_2_, p_200122_3_);
    }
}
