package dev.buildtool.scp.shelf;

import dev.buildtool.satako.blocks.BlockHorizontal;
import dev.buildtool.scp.registration.SCPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ShelfBlock extends BlockHorizontal {
    static VoxelShape ONE= VoxelShapes.box(0,0,0,0.5,1,1);
    static VoxelShape TWO= VoxelShapes.box(0,0,0,1,1,0.5);
    static VoxelShape THREE= VoxelShapes.box(0.5,0,0,1,1,1);
    static VoxelShape FOUR= VoxelShapes.box(0.0,0,0.5,1,1,1);
    public ShelfBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.getValue(FACING))
        {
            case WEST:return ONE;
            case EAST:return THREE;
            case NORTH:return TWO;
            case SOUTH:return FOUR;
        }
        return super.getShape(state, worldIn, pos, context);
    }

    public enum Corner{
        UPPER_LEFT(1),
        UPPER_RIGHT(3),
        LOWER_LEFT(0),
        LOWER_RIGHT(2);

        public int itemNumber;

        Corner(int itemNumber_)
        {
            itemNumber=itemNumber_;
        }

        static Corner getCorner(Direction.Axis axis, double X, double Y, double Z)
        {
            boolean upper=Y>0.5f;
            switch (axis)
            {
                case X:
                    if(Z>0.5f)
                    {
                        if(upper)
                            return UPPER_RIGHT;
                        else return LOWER_RIGHT;
                    }
                    else{
                        if(upper)
                            return UPPER_LEFT;
                        else return LOWER_LEFT;
                    }
                case Z:
                    if(X>0.5f)
                    {
                        if(upper)
                        {
                            return UPPER_LEFT;
                        } else return LOWER_LEFT;
                    } else {
                        if (upper)
                            return UPPER_RIGHT;
                        else return LOWER_RIGHT;
                    }
                default:
                    return null;
            }
        }

        static Corner getCorner(RayTraceResult rayTraceResult, BlockPos pos, Direction direction) {

            Vector3d vector3d = rayTraceResult.getLocation().subtract(Vector3d.atCenterOf(pos));
            switch (direction) {
                case NORTH:
                    if (vector3d.y > 0.5) {
                        if (vector3d.x < 0.5) {
                            return UPPER_LEFT;
                        } else {
                            return UPPER_RIGHT;
                        }
                    } else {
                        if (vector3d.x > 0.5) {
                            return LOWER_RIGHT;
                        }
                        return LOWER_LEFT;
                    }
                case WEST:
                    if (vector3d.y > 0.5) {
                        if (vector3d.z > 0.5)
                            return UPPER_RIGHT;
                        return UPPER_LEFT;
                    } else {
                        if (vector3d.z > 0.5)
                            return LOWER_RIGHT;
                        return LOWER_LEFT;
                    }
                case SOUTH:
                    if (vector3d.y > 0.5) {
                        if (vector3d.x > 0.5)
                            return UPPER_LEFT;
                        return UPPER_RIGHT;
                    } else if (vector3d.x > 0.5)
                        return LOWER_LEFT;
                    return LOWER_RIGHT;
                case EAST:
                    if (vector3d.y > 0.5) {
                        if (vector3d.z > 0.5)
                            return UPPER_RIGHT;
                        return UPPER_LEFT;
                    } else if (vector3d.z > 0.5)
                        return LOWER_RIGHT;
                    return LOWER_LEFT;
                default:
                    return null;
            }
        }
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        Vector3d vec3d=hit.getLocation().subtract(pos.getX(),pos.getY(),pos.getZ());
        double x=vec3d.x;
        double y=vec3d.y;
        double z=vec3d.z;
        Direction direction=hit.getDirection();
        Direction.Axis axis=direction.getAxis();
        Corner corner=Corner.getCorner(axis,x,y,z);
        ItemStack heldstack=player.getItemInHand(handIn);
        ShelfEntity shelfEntity= (ShelfEntity) worldIn.getBlockEntity(pos);
        assert shelfEntity != null;
        if (corner != null) {
            int item = corner.itemNumber;
            if (shelfEntity.itemHandler.getStackInSlot(item).isEmpty()) {
                shelfEntity.itemHandler.insertItem(item, heldstack.copy(), false);
                heldstack.shrink(heldstack.getCount());
            } else {
                if (player.inventory.add(shelfEntity.itemHandler.getStackInSlot(item))) {
                    shelfEntity.itemHandler.setStackInSlot(item, ItemStack.EMPTY);
                }
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        if (player.isCrouching()) {
            ItemStack stack = player.getMainHandItem();
            RayTraceResult vector3d = player.pick(3, 1, false);
            Corner corner = Corner.getCorner(vector3d, pos, state.getValue(FACING));
            int item = corner.itemNumber;
            ShelfEntity shelfEntity = (ShelfEntity) worldIn.getBlockEntity(pos);
            if (shelfEntity.itemHandler.insertItem(item, stack, true).getCount() < stack.getCount()) {
                shelfEntity.itemHandler.insertItem(item, stack.split(1), false);
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext itemUseContext) {
        if (!itemUseContext.getPlayer().isCrouching())
            return defaultBlockState().setValue(FACING, itemUseContext.getHorizontalDirection());
        else
            return super.getStateForPlacement(itemUseContext);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.shelfEntity.create();
    }
}
