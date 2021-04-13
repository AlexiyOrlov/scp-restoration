package dev.buildtool.scp.table;

import dev.buildtool.satako.ItemHandler;
import dev.buildtool.scp.events.SCPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Table4 extends Table {
    public Table4(Properties properties) {
        super(properties);
    }

    @Override
    public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        if (player.isCrouching()) {
            ItemStack main = player.getMainHandItem();
            TableEntity4 tableEntity4 = (TableEntity4) worldIn.getBlockEntity(pos);
            Vector3d vector3d = player.pick(3, 1, false).getLocation();
            Vector3d diff = vector3d.subtract(Vector3d.atCenterOf(pos));
            if (diff.x > 0.5) {
                if (diff.z > 0.5) {
                    if (tableEntity4.itemHandler.insertItem(2, main, true).getCount() < main.getCount()) {
                        tableEntity4.itemHandler.insertItem(2, main.split(1), false);
                    }
                } else {
                    if (tableEntity4.itemHandler.insertItem(3, main, true).getCount() < main.getCount()) {
                        tableEntity4.itemHandler.insertItem(3, main.split(1), false);
                    }
                }
            } else if (diff.x < 0.5) {
                if (diff.z < 0.5) {
                    if (tableEntity4.itemHandler.insertItem(0, main, true).getCount() < main.getCount()) {
                        tableEntity4.itemHandler.insertItem(0, main.split(1), false);
                    }
                } else {
                    if (tableEntity4.itemHandler.insertItem(1, main, true).getCount() < main.getCount()) {
                        tableEntity4.itemHandler.insertItem(1, main.split(1), false);
                    }
                }
            }
        }
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (hit.getDirection() == Direction.UP) {
            Vector3d vector3d = hit.getLocation();
            Vector3d d = vector3d.subtract(Vector3d.atLowerCornerOf(pos));
            TableEntity4 tableEntity4 = (TableEntity4) worldIn.getBlockEntity(pos);
            ItemHandler itemHandler = tableEntity4.itemHandler;
            if (d.x < 0.5) {
                if (d.z < 0.5) {
                    if(itemHandler.getStackInSlot(0).isEmpty()) {
                        itemHandler.setStackInSlot(0,player.getItemInHand(handIn).copy());
                        player.getItemInHand(handIn).shrink(64);
                    } else{
                        if(player.inventory.add(itemHandler.getStackInSlot(0))) {
                            itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                        }
                    }
                } else {
                    if(itemHandler.getStackInSlot(1).isEmpty()) {
                        itemHandler.setStackInSlot(1,player.getItemInHand(handIn).copy());
                        player.getItemInHand(handIn).shrink(64);
                    } else{
                        if(player.inventory.add(itemHandler.getStackInSlot(1))) {
                            itemHandler.setStackInSlot(1, ItemStack.EMPTY);
                        }
                    }
                }
            } else{
                if(d.x>0.5) {
                    if(d.z>0.5) {
                        if(itemHandler.getStackInSlot(2).isEmpty()) {
                            itemHandler.setStackInSlot(2,player.getItemInHand(handIn).copy());
                            player.getItemInHand(handIn).shrink(64);
                        } else{
                            if(player.inventory.add(itemHandler.getStackInSlot(2))) {
                                itemHandler.setStackInSlot(2, ItemStack.EMPTY);
                            }
                        }
                    } else {
                        if (itemHandler.getStackInSlot(3).isEmpty()) {
                            itemHandler.setStackInSlot(3, player.getItemInHand(handIn).copy());
                            player.getItemInHand(handIn).shrink(64);
                        } else {
                            if (player.inventory.add(itemHandler.getStackInSlot(3))) {
                                itemHandler.setStackInSlot(3, ItemStack.EMPTY);
                            }
                        }
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.tableEntity4.create();
    }
}
