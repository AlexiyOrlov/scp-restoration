package dev.buildtool.scp.scps.blockscps;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCPObject;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SCPObject(name = "Contagious Crystal", number = "409", classification = SCPObject.Classification.KETER)
public class ContagiousCrystal extends Block {
    public ContagiousCrystal(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(World p_176199_1_, BlockPos p_176199_2_, Entity entityIn) {
        super.stepOn(p_176199_1_, p_176199_2_, entityIn);
        if (entityIn instanceof LivingEntity) {
            entityIn.hurt(DamageSource.GENERIC, 1);
            if (!entityIn.isAlive()) {
                List<BlockPos> posList = Functions.boundingBoxToPositions(entityIn.getBoundingBox());
                posList.forEach(blockPos -> p_176199_1_.setBlockAndUpdate(blockPos, defaultBlockState()));
            }
        }
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return false;
    }

    @Override
    public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        worldIn.getBlockTicks().scheduleTick(pos, this, Functions.minutesToTicks(worldIn.random.nextInt(3) + 1));
    }

    @Override
    public void tick(BlockState p_225534_1_, ServerWorld worldIn, BlockPos pos, Random p_225534_4_) {
        super.tick(p_225534_1_, worldIn, pos, p_225534_4_);
        BlockPos to = spreadTo(worldIn, pos);
        if (to != null) {
            worldIn.setBlockAndUpdate(to, defaultBlockState());
        }
        worldIn.getBlockTicks().scheduleTick(pos, this, Functions.minutesToTicks(worldIn.random.nextInt(3) + 1));
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!(blockIn instanceof AirBlock) && blockIn != this) {
            worldIn.getBlockTicks().scheduleTick(pos, this, Functions.minutesToTicks(worldIn.random.nextInt(3) + 1));
        }
    }

    private BlockPos spreadTo(World world, BlockPos pos) {
        Direction direction = Direction.getRandom(world.random);
        final BlockPos relative = pos.relative(direction);
        final BlockState blockState = world.getBlockState(relative);
        final Block block = blockState.getBlock();
        if (!(world.isEmptyBlock(relative) || blockState == defaultBlockState()
                || block == Blocks.GRANITE || block == Blocks.GRANITE_SLAB
                || block == Blocks.GRANITE_STAIRS ||
                block == Blocks.GRANITE_WALL || block == Blocks.POLISHED_GRANITE
                || block == Blocks.POLISHED_GRANITE_SLAB || block == Blocks.POLISHED_GRANITE_STAIRS))
            return relative;
        return null;
    }
}
