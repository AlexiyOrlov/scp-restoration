package dev.buildtool.scp.shelf;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class ShelfRenderer extends TileEntityRenderer<ShelfEntity> {
    public ShelfRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(ShelfEntity blockEntity, float partialTicks, MatrixStack matrices, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrices.pushPose();
        World world = blockEntity.getLevel();
        BlockState blockState = world.getBlockState(blockEntity.getBlockPos());
        if (blockState.getBlock() instanceof ShelfBlock) {
            Direction facing = blockState.getValue(ShelfBlock.FACING);
            Minecraft mc = Minecraft.getInstance();
            matrices.translate(0.5, 0, 0.5);
            if (facing.getAxis() == Direction.Axis.X)
                matrices.mulPose(Vector3f.YP.rotationDegrees(90));
            switch (facing)
            {
                case NORTH:
                case WEST:
                    matrices.translate(0, 0, -0.25);
                    break;
                case SOUTH:
                case EAST:
                    matrices.translate(0, 0, 0.25);
                    break;
            }
            matrices.scale(0.3f, 0.3f, 0.3f);
            ItemStack one = blockEntity.itemHandler.getStackInSlot(0);
            ItemStack two = blockEntity.itemHandler.getStackInSlot(1);
            ItemStack three = blockEntity.itemHandler.getStackInSlot(2);
            ItemStack four = blockEntity.itemHandler.getStackInSlot(3);
            matrices.translate(0.8, 0.7, 0);
            mc.getItemRenderer().renderStatic(one, ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, matrices, bufferIn);
            matrices.translate(-1.6, 0, 0);
            mc.getItemRenderer().renderStatic(three, ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, matrices, bufferIn);
            matrices.translate(0, 1.5, 0);
            mc.getItemRenderer().renderStatic(four, ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, matrices, bufferIn);
            matrices.translate(1.6, 0, 0);
            mc.getItemRenderer().renderStatic(two, ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, matrices, bufferIn);

        }
        matrices.popPose();
    }
}
