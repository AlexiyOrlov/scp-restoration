package dev.buildtool.scp.table;

import dev.buildtool.satako.blocks.Block2;
import dev.buildtool.scp.registration.SCPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Table extends Block2 {
    public Table(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        TableEntity tableEntity = (TableEntity) worldIn.getBlockEntity(pos);
        if (tableEntity.itemHandler.isEmpty()) {
            tableEntity.itemHandler.setStackInSlot(0, player.getItemInHand(handIn).copy());
            player.getItemInHand(handIn).shrink(64);
        } else {
            if (player.inventory.add(tableEntity.itemHandler.extractItem(0, 64, true))) {
                tableEntity.itemHandler.extractItem(0, 64, false);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        if (player.isCrouching()) {
            TableEntity tableEntity = (TableEntity) worldIn.getBlockEntity(pos);
            ItemStack heldItem = player.getItemInHand(Hand.MAIN_HAND);
            if (tableEntity.itemHandler.insertItem(0, heldItem, true).getCount() < heldItem.getCount()) {
                ItemStack stack = heldItem.split(1);
                tableEntity.itemHandler.insertItem(0, stack, false);
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.tableEntity.create();
    }
}
