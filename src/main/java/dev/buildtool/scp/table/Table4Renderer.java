package dev.buildtool.scp.table;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class Table4Renderer extends TileEntityRenderer<TableEntity4> {
    public Table4Renderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TableEntity4 tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.translate(0.5,18/16d,0.5);
        matrixStackIn.scale(0.25f,0.25f,0.25f);
        ItemRenderer itemRenderer= Minecraft.getInstance().getItemRenderer();
        matrixStackIn.translate(-0.9, 0, -0.9);
        itemRenderer.renderStatic(tileEntityIn.itemHandler.getStackInSlot(0), ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
        matrixStackIn.translate(1.8, 0, 0);
        itemRenderer.renderStatic(tileEntityIn.itemHandler.getStackInSlot(3), ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
        matrixStackIn.translate(0, 0, 1.8);
        itemRenderer.renderStatic(tileEntityIn.itemHandler.getStackInSlot(2), ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
        matrixStackIn.translate(-1.8, 0, 0);
        itemRenderer.renderStatic(tileEntityIn.itemHandler.getStackInSlot(1), ItemCameraTransforms.TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
    }
}
