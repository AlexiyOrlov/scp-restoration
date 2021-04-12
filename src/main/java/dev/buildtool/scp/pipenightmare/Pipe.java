package dev.buildtool.scp.pipenightmare;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.infiniteikea.SupportBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class Pipe extends SupportBlock {
    /**
     * @param apothem    width
     * @param properties
     */
    public Pipe(float apothem, Properties properties) {
        super(apothem, properties);
    }

    @Override
    public void onPlace(BlockState p_220082_1_, World p_220082_2_, BlockPos p_220082_3_, BlockState p_220082_4_, boolean p_220082_5_) {
        super.onPlace(p_220082_1_, p_220082_2_, p_220082_3_, p_220082_4_, p_220082_5_);
        p_220082_2_.getBlockTicks().scheduleTick(p_220082_3_, this, Functions.minutesToTicks(p_220082_2_.random.nextInt(2) + 1));
    }

    @Override
    public void neighborChanged(BlockState p_220069_1_, World p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_, BlockPos from, boolean p_220069_6_) {
        super.neighborChanged(p_220069_1_, p_220069_2_, p_220069_3_, p_220069_4_, from, p_220069_6_);
        if (!p_220069_1_.isAir() && p_220069_4_ != this)
            p_220069_2_.getBlockTicks().scheduleTick(p_220069_3_, this, Functions.minutesToTicks(p_220069_2_.random.nextInt(2) + 1));
    }

    @Override
    public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
        super.tick(p_225534_1_, p_225534_2_, p_225534_3_, p_225534_4_);
        Direction to = spreadTo(p_225534_2_, p_225534_3_);
        if (to != null) {
            p_225534_2_.setBlockAndUpdate(p_225534_3_.relative(to), defaultBlockState());
        }
        p_225534_2_.getBlockTicks().scheduleTick(p_225534_3_, this, Functions.minutesToTicks(p_225534_4_.nextInt(2) + 1));
    }

    private Direction spreadTo(World world, BlockPos ownPos) {
        for (int i = 0; i < Direction.values().length; i++) {
            Direction direction = Direction.getRandom(world.random);
            BlockPos newpos = ownPos.relative(direction);
            if (world.isEmptyBlock(newpos)) {
                boolean further = world.random.nextBoolean();
                if (further) {
                    newpos = newpos.relative(direction);
                }
            }
            BlockState blockState = world.getBlockState(newpos);
            if (!(blockState.getBlock() instanceof Pipe)) {
                Material material = blockState.getMaterial();
                if (material == Material.BUILDABLE_GLASS || material == Material.HEAVY_METAL || material == Material.STONE ||
                        material == Material.METAL || material == Material.PISTON) {
                    return direction;
                }
            }
        }
        return null;
    }
}
