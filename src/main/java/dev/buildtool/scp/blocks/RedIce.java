package dev.buildtool.scp.blocks;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCPObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Red ice spreads by replacing water in a random direction.
 */
@SCPObject(name = "Red Ice",classification = SCPObject.Classification.EUCLID,number = "009")
public class RedIce extends Block {
    public RedIce(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(World worldIn, BlockPos pos, Entity entityIn) {
        super.stepOn(worldIn, pos, entityIn);
        if(entityIn instanceof LivingEntity)
        {
            entityIn.hurt(DamageSource.GENERIC,1);
            if(!entityIn.isAlive())
            {
                List<BlockPos> posList= Functions.boundingBoxToPositions(entityIn.getBoundingBox());
                posList.forEach(blockPos -> worldIn.setBlockAndUpdate(blockPos,defaultBlockState()));
            }
        }
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return state==adjacentBlockState;
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return false;
    }

    @Override
    public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {

        worldIn.getBlockTicks().scheduleTick(pos,this,Functions.secondsToTicks(worldIn.random.nextInt(4)+58));
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {

        Stream.of(pos.above(),pos.below(),pos.east(),pos.west(),pos.north(),pos.south()).reduce((blockPos, blockPos2) -> worldIn.random.nextBoolean() ? blockPos : blockPos2).ifPresent(blockPos -> {
            FluidState fluidState=worldIn.getFluidState(blockPos);
            if(fluidState.is(FluidTags.WATER))
            {
                worldIn.setBlockAndUpdate(blockPos,defaultBlockState());
            }
        });
        worldIn.getBlockTicks().scheduleTick(pos,this,Functions.secondsToTicks(worldIn.random.nextInt(4)+58));
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if(!state.isAir(worldIn,fromPos) && blockIn!=this && canSpread(worldIn,pos))
        {
            worldIn.getBlockTicks().scheduleTick(pos,this,Functions.secondsToTicks(worldIn.random.nextInt(58)+4));
        }
    }

    private boolean canSpread(World world,BlockPos pos)
    {
        Biome biome=world.getBiome(pos);
        if(!biome.shouldSnow(world,pos))
        {
            return Stream.of(pos.above(),pos.below(),pos.west(),pos.west(),pos.south(),pos.north()).anyMatch(blockPos -> world.getFluidState(blockPos).is(FluidTags.WATER));
        }
        return false;
    }
}
