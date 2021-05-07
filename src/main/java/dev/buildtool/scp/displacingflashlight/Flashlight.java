package dev.buildtool.scp.displacingflashlight;

import com.google.common.collect.Sets;
import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCPObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Removes blocks in path when on and places them back when off
 */
@SCPObject(number = "1188", name = "Matter-Displacing Flashlight", classification = SCPObject.Classification.SAFE)
public class Flashlight extends Item {
    private static final String STATE = "Turned on";
    public static final String DISPLACED_POSITIONS = "Displaced positions", DISPLACED_BLOCKS = "DIsplaced blocks";

    public Flashlight(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stack = playerEntity.getItemInHand(hand);
        boolean state = stack.getOrCreateTag().getBoolean(STATE);
        stack.getTag().putBoolean(STATE, !state);
        if (state) {
            long[] previosu = stack.getTag().getLongArray(DISPLACED_POSITIONS);
            int[] previousblock = stack.getTag().getIntArray(DISPLACED_BLOCKS);
            assert previosu.length == previousblock.length;
            List<BlockPos> blockPosSet = Arrays.stream(previosu).mapToObj(BlockPos::of).collect(Collectors.toList());
            List<BlockState> blockStateList = Arrays.stream(previousblock).mapToObj(Block::stateById).collect(Collectors.toList());
            for (int i = 0; i < blockPosSet.size(); i++) {
                world.setBlock(blockPosSet.get(i), blockStateList.get(i), 0);
//                Methods.setBlockStateSilently(world,blockPosSet.get(i),blockStateList.get(i));
            }
            stack.getTag().remove(DISPLACED_POSITIONS);
            stack.getTag().remove(DISPLACED_BLOCKS);
        }
        return ActionResult.consume(stack);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity holder, int p_77663_4_, boolean selected) {
        if (itemStack.hasTag() && holder instanceof PlayerEntity && selected && itemStack.getTag().getBoolean(STATE)) {
            if (!world.isClientSide) {
                PlayerEntity playerEntity = (PlayerEntity) holder;
                Vector3d eyePosition = playerEntity.getEyePosition(1);
                Vector3d viewvector = playerEntity.getViewVector(1);
                Vector3d center = eyePosition.add(viewvector.x * 20, viewvector.y * 20, viewvector.z * 20);
                Vector3d top = center.add(0, 2, 0);
                Vector3d bottom = center.subtract(0, 2, 0);
                double signumZ = Math.signum(eyePosition.z);
                double signumX = Math.signum(eyePosition.x);
                Vector3d left = center.add(signumX * -2, 0, signumZ * -2);
                Vector3d right = center.add(signumX * 2, 0, signumZ * 2);
                Vector3d upLeft = left.add(0, 2, 0);
                Vector3d upRight = right.add(0, 2, 0);
                Vector3d downLeft = left.subtract(0, 2, 0);
                Vector3d downRIght = left.subtract(0, 2, 0);
                HashSet<Vector3d> vector3ds = Sets.newHashSet(center, top, bottom, left, right, upLeft, upRight, downLeft, downRIght);
                long[] previosu = itemStack.getTag().getLongArray(DISPLACED_POSITIONS);
                int[] previousblock = itemStack.getTag().getIntArray(DISPLACED_BLOCKS);
                List<BlockPos> toDisplace = Arrays.stream(previosu).mapToObj(BlockPos::of).collect(Collectors.toList());
                List<BlockState> toDisplace2 = Arrays.stream(previousblock).mapToObj(Block::stateById).collect(Collectors.toList());
                vector3ds.forEach(vector3d -> {
                    BlockRayTraceResult blockRayTraceResult = world.clip(new RayTraceContext(eyePosition, vector3d, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, playerEntity));
                    BlockPos resultBlockPos = blockRayTraceResult.getBlockPos();
                    BlockState blockState = world.getBlockState(resultBlockPos);
                    if (Functions.canReplaceBlock(resultBlockPos, world) && !world.isEmptyBlock(resultBlockPos)) {
                        world.destroyBlock(resultBlockPos, false);
//                            Methods.setBlockStateSilently(world,resultBlockPos, Blocks.AIR.defaultBlockState());
                        toDisplace.add(resultBlockPos);
                        toDisplace2.add(blockState);

                    }
                });

                Long[] longs = toDisplace.stream().map(BlockPos::asLong).toArray(Long[]::new);
                itemStack.getTag().putLongArray(DISPLACED_POSITIONS, toDisplace.stream().map(BlockPos::asLong).collect(Collectors.toList()));
                Integer[] intArray = toDisplace2.stream().map(Block::getId).toArray(Integer[]::new);
                itemStack.getTag().putIntArray(DISPLACED_BLOCKS, toDisplace2.stream().map(Block::getId).collect(Collectors.toList()));
                assert longs.length == intArray.length;
            }
        }

    }
}
