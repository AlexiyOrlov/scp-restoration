package dev.buildtool.scp.slidingdoor;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.buildtool.scp.SCP;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class SlidingDoorRenderer extends TileEntityRenderer<SlidingDoorEntity> {
    final SlidingDoorModel doorModel=new SlidingDoorModel();
    static final ResourceLocation texture=new ResourceLocation(SCP.ID,"textures/sliding_door.png");
    public SlidingDoorRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(SlidingDoorEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        Direction direction=tileEntityIn.getBlockState().getValue(SlidingDoorBlock.FACING);
        matrixStackIn.translate(1/2d,8/16d,8/16d);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(direction.toYRot()));
        if(direction==Direction.EAST || direction==Direction.WEST)
        {
            matrixStackIn.mulPose(new Quaternion(0,180,0,true));
        }

        if (tileEntityIn.opening) {
            matrixStackIn.translate(1 - tileEntityIn.openTime / 16d, 0, 0);
        } else if (tileEntityIn.closing)
            matrixStackIn.translate(tileEntityIn.closeTime / 16d, 0, 0);

        doorModel.renderToBuffer(matrixStackIn,bufferIn.getBuffer(RenderType.entityCutout(texture)),0xffffff,combinedOverlayIn,1,1,1,1);
        matrixStackIn.popPose();
    }
}
