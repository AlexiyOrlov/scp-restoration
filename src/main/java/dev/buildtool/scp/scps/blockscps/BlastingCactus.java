package dev.buildtool.scp.scps.blockscps;

import dev.buildtool.scp.SCPObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

/**
 * Cacti explode when there is a living entity within 3 blocks range, even if not visible, and applies poison for
 * 20 seconds.
 */
@SCPObject(name = "Landmine Cacti",number = "822",classification = SCPObject.Classification.EUCLID)
public class BlastingCactus extends Block {
    protected VoxelShape SHAPE = VoxelShapes.box(5 / 16d, 0, 5 / 16d, 11 / 16d, 2 / 16d, 11 / 16d);

    public BlastingCactus(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        worldIn.getBlockTicks().scheduleTick(pos,this,40);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(pos).inflate(3);
        List<LivingEntity> livingEntities = worldIn.getEntitiesOfClass(LivingEntity.class, axisAlignedBB, livingEntity -> livingEntity.attackable() && EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(livingEntity));
        if(!livingEntities.isEmpty())
        {
            worldIn.removeBlock(pos,false);
            worldIn.explode(null,pos.getX(),pos.getY(), pos.getZ(),3.5f,false, Explosion.Mode.DESTROY);
            livingEntities.forEach(livingEntity -> livingEntity.addEffect(new EffectInstance(Effects.POISON,20*20)));
        }
        else
            worldIn.getBlockTicks().scheduleTick(pos,this,20);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        BlockState state1=worldIn.getBlockState(pos.below());
        if(!state1.isSolidRender(worldIn,pos.below()))
            worldIn.destroyBlock(pos,true);
    }
}
