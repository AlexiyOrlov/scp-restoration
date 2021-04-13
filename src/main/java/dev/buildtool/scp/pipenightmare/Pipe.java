package dev.buildtool.scp.pipenightmare;

import dev.buildtool.satako.Constants;
import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.infiniteikea.SupportBlock;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;

@SCPObject(name = "Pipe Nightmare", number = "015", classification = SCPObject.Classification.EUCLID)
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
        p_220082_2_.getBlockTicks().scheduleTick(p_220082_3_, this, Functions.minutesToTicks(p_220082_2_.random.nextInt(3) + 1));
    }

    @Override
    public void neighborChanged(BlockState p_220069_1_, World p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_, BlockPos from, boolean p_220069_6_) {
        super.neighborChanged(p_220069_1_, p_220069_2_, p_220069_3_, p_220069_4_, from, p_220069_6_);
        if (!(p_220069_4_ instanceof AirBlock) && p_220069_4_ != this)
            p_220069_2_.getBlockTicks().scheduleTick(p_220069_3_, this, Functions.minutesToTicks(p_220069_2_.random.nextInt(3) + 1));
    }

    @Override
    public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
        super.tick(p_225534_1_, p_225534_2_, p_225534_3_, p_225534_4_);
        BlockPos to = spreadTo(p_225534_2_, p_225534_3_);
        if (to != null) {
            p_225534_2_.setBlockAndUpdate(to, defaultBlockState());
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        boolean flag = facingState.isFaceSturdy(worldIn, facingPos, facing) || facingState.getBlock() instanceof Pipe;
        return stateIn.setValue(PROPERTY_BY_DIRECTION.get(facing), flag);
    }

    /**
     * Algorithm
     */
    private BlockPos spreadTo(World world, BlockPos ownPos) {
        for (int i = 0; i < 4; i++) {
            Direction direction = Functions.randomHorizontalFacing();
            if (world.isEmptyBlock(ownPos.relative(direction)))
                return ownPos.relative(direction);
        }
        if (world.isEmptyBlock(ownPos.below()))
            return ownPos.below();
        if (world.isEmptyBlock(ownPos.above())) {
            for (Direction horizontal : Constants.HORIZONTALS) {
                if (world.isEmptyBlock(ownPos.relative(horizontal))) {
                    return ownPos.relative(horizontal);
                }
            }
            if (world.getBlockState(ownPos.below()).getBlock() != this)
                return ownPos.above();
        }
        return null;
    }

    @Override
    public void playerDestroy(World p_180657_1_, PlayerEntity p_180657_2_, BlockPos p_180657_3_, BlockState p_180657_4_, @Nullable TileEntity p_180657_5_, ItemStack p_180657_6_) {
        super.playerDestroy(p_180657_1_, p_180657_2_, p_180657_3_, p_180657_4_, p_180657_5_, p_180657_6_);
        p_180657_1_.explode(null, p_180657_3_.getX() + 0.5, p_180657_3_.getY(), p_180657_3_.getZ() + 0.5, 1, Explosion.Mode.NONE);
    }

    @Override
    public void attack(BlockState p_196270_1_, World p_196270_2_, BlockPos p_196270_3_, PlayerEntity p_196270_4_) {
        ItemStack stack = p_196270_4_.getMainHandItem();
        if (stack.getToolTypes().contains(ToolType.PICKAXE)) {
            if (p_196270_2_.random.nextBoolean())
                p_196270_4_.hurt(DamageSource.LAVA, 1);
            else if (p_196270_2_.random.nextBoolean())
                p_196270_4_.hurt(DamageSource.ON_FIRE, 1);
            else
                p_196270_4_.hurt(DamageSource.GENERIC, 1);
        }
    }
}
