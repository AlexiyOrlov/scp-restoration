package dev.buildtool.scp.blocks;

import dev.buildtool.scp.SCPObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

/**
 * The soil makes the plants and ageable mobs grow quickly (it ticks randomly).
 */
@SCPObject(name = "Fertile Soil",number = "124",classification = SCPObject.Classification.EUCLID)
public class FertileSoil extends Block {
    public FertileSoil(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return true;
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        BlockPos above=pos.above();
        Block block=worldIn.getBlockState(above).getBlock();
        if(block instanceof IGrowable)
        {
            ((IGrowable) block).performBonemeal(worldIn, rand, above, worldIn.getBlockState(above));
            block = worldIn.getBlockState(above).getBlock();
            if (block instanceof IGrowable)
                ((IGrowable) block).performBonemeal(worldIn, rand, above, worldIn.getBlockState(above));
        }
    }


    @Override
    public void stepOn(World worldIn, BlockPos pos, Entity entityIn) {
        super.stepOn(worldIn, pos, entityIn);
        if(entityIn instanceof AgeableEntity)
        {
            ((AgeableEntity) entityIn).ageUp(1);
        }
    }
}
