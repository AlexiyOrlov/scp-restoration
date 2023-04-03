package dev.buildtool.scp.shelflife;

import dev.buildtool.satako.blocks.BlockHorizontal;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.registration.SCPTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

@SCPObject(name = "Shelf Life", number = "1811", classification = SCPObject.Classification.SAFE)
public class ShelfBlock extends BlockHorizontal {

    public static final VoxelShape FIRST = Block.box(6, 0, 0, 10, 15, 16);
    public static final VoxelShape SECOND = Block.box(0, 0, 6, 16, 15, 10);

    public ShelfBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(FACING)) {
            case WEST:
            case EAST:
                return FIRST;
            case NORTH:
            case SOUTH:
                return SECOND;
        }
        return super.getShape(state, worldIn, pos, context);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) worldIn.getBlockEntity(pos), packetBuffer -> packetBuffer.writeBlockPos(pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.shelfLifeEntity.create();
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        ShelfEntity shelfEntity = (ShelfEntity) worldIn.getBlockEntity(pos);
        shelfEntity.books.forEach(itemHandler -> {
            ItemStack itemStack = itemHandler.getStackInSlot(0);
            if (!itemStack.isEmpty()) {
                InventoryHelper.dropItemStack(worldIn, shelfEntity.getX() + 0.5, shelfEntity.getY(), shelfEntity.getZ() + 0.5, itemStack);
            }
        });
        super.onRemove(state, worldIn, pos, newState, isMoving);

    }
}
