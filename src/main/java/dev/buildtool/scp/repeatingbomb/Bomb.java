package dev.buildtool.scp.repeatingbomb;

import dev.buildtool.scp.SCPObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

@SCPObject(number = "2948", name = "Repeating Bomb", classification = SCPObject.Classification.SAFE)
public class Bomb extends FallingBlock {
    static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public Bomb(Properties p_i48401_1_) {
        super(p_i48401_1_);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        super.createBlockStateDefinition(p_206840_1_);
        p_206840_1_.add(FACING);
    }

    @Override
    public void onLand(World world, BlockPos blockPos, BlockState p_176502_3_, BlockState p_176502_4_, FallingBlockEntity fallingBlockEntity) {
        if (!world.isClientSide) {
            if (fallingBlockEntity.time > 0) {
                world.explode(null, blockPos.getX(), blockPos.getY() - 1, blockPos.getZ(), fallingBlockEntity.time, Explosion.Mode.DESTROY);
            }
        }
    }
}
