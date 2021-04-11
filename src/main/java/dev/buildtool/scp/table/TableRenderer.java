package dev.buildtool.scp.table;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class TableRenderer extends TileEntityRenderer<TableEntity> {
    public TableRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TableEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.translate(0.5,20/16f,0.5);
        matrixStackIn.scale(0.5f,0.5f,0.5f);
        Minecraft.getInstance().getItemRenderer().renderStatic(tileEntityIn.itemHandler.getStackInSlot(0), ItemCameraTransforms.TransformType.NONE,combinedLightIn,combinedOverlayIn,matrixStackIn,bufferIn);
    }
}
