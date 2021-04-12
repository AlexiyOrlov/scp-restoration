package dev.buildtool.scp.monsterpot;

import dev.buildtool.satako.blocks.BlockHorizontal;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.events.Entities;
import dev.buildtool.scp.events.SCPTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SCPObject(number = "019", name = "Monster Pot", classification = SCPObject.Classification.KETER)
public class MonsterPot extends BlockHorizontal {
    static EnumProperty<Half> HALF = BlockStateProperties.HALF;
    static final VoxelShape bottom = Block.box(2, 0, 2, 14, 16, 14);
    static final VoxelShape top = Block.box(2, 0, 2, 14, 8, 14);

    public MonsterPot(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(HALF, Half.BOTTOM));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext itemUseContext) {
        if (itemUseContext.getLevel().getBlockState(itemUseContext.getClickedPos().above()).canBeReplaced(itemUseContext))
            return super.getStateForPlacement(itemUseContext);
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(HALF);
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        worldIn.setBlockAndUpdate(pos.above(), state.setValue(HALF, Half.TOP));
    }


    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, worldIn, pos, newState, isMoving);
        if (state.getValue(HALF) == Half.TOP)
            worldIn.destroyBlock(pos.below(), false);
        else
            worldIn.destroyBlock(pos.above(), false);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (state.getValue(HALF) == Half.TOP && hit.getDirection() == Direction.UP) {
            spawnMinions(worldIn, pos);
            return ActionResultType.SUCCESS;
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        super.attack(state, worldIn, pos, player);
        spawnMinions(worldIn, state.getValue(HALF) == Half.BOTTOM ? pos.above() : pos);
    }

    private void spawnMinions(World world, BlockPos pos) {
        for (int i = 0; i < world.random.nextInt(6) + 6; i++) {
            PotMonster potMonster = Entities.potMonster.create(world);
            if (!world.isClientSide)
                potMonster.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(pos), SpawnReason.SPAWNER, null, null);
            potMonster.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            potMonster.push(world.random.nextFloat() * (world.random.nextInt(2) - 1), 0, world.random.nextFloat() * (world.random.nextInt(2) - 1));
            world.addFreshEntity(potMonster);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.getValue(HALF) == Half.TOP;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.monsterPotEntity.create();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.getValue(HALF) == Half.TOP) {
            return top;
        }
        return bottom;
    }
}
