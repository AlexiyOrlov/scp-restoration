package dev.buildtool.scp.lock;

import dev.buildtool.satako.Constants;
import dev.buildtool.satako.blocks.BlockDirectional;
import dev.buildtool.scp.registration.SCPItems;
import dev.buildtool.scp.registration.SCPTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.UUID;

public class ElectronicLock extends BlockDirectional {
    public static final VoxelShape WEST = Block.box(14, 4, 4, 16, 12, 12);
    public static final VoxelShape NORTH = Block.box(4, 4, 14, 12, 12, 16);
    public static final VoxelShape EAST = Block.box  (0, 4, 4, 2, 12, 12);
    public static final VoxelShape SOUTH = Block.box(4, 4, 0, 12, 12, 2);
    public static final VoxelShape TOP = Block.box(4, 0, 4, 12, 2, 12);
    public static final VoxelShape BOTTOM = Block.box(4, 14, 4, 12, 16, 12);
    static public BooleanProperty OPEN = BlockStateProperties.OPEN;

    public ElectronicLock(Properties builder) {
        super(builder);
        registerDefaultState(getStateDefinition().any().setValue(OPEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(FACING)) {
            case WEST:
                return WEST;
            case NORTH:
                return NORTH;
            case EAST:
                return EAST;
            case SOUTH:
                return SOUTH;
            case UP:
                return TOP;
            case DOWN:
                return BOTTOM;
        }
        return super.getShape(state, worldIn, pos, context);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        LockEntity lockEntity = (LockEntity) worldIn.getBlockEntity(pos);
        if (lockEntity.owner.equals(Constants.NULL_UUID)) {
            lockEntity.owner = player.getUUID();
            if (worldIn.isClientSide)
                player.sendMessage(new TranslationTextComponent("scp.lock.claimed"), UUID.randomUUID());
            lockEntity.setChanged();
            return ActionResultType.SUCCESS;
        } else {
            ItemStack itemStack = player.getItemInHand(handIn);
            if (itemStack.getItem() == SCPItems.keyCard) {
                return ActionResultType.PASS;
            }
            else {
                if (worldIn.isClientSide)
                    openGUI(lockEntity);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    private void openGUI(LockEntity lockEntity) {
        Minecraft.getInstance().setScreen(new LockScreen(new StringTextComponent("L"), lockEntity));
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(OPEN) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return getSignal(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.lockEntity.create();
    }
}
