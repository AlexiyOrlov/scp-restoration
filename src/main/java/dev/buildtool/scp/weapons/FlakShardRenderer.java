package dev.buildtool.scp.weapons;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.buildtool.scp.SCP;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class FlakShardRenderer extends EntityRenderer<FlakShard> {
    private final FlakShardModel flakShardModel = new FlakShardModel();
    private final ResourceLocation texture = new ResourceLocation(SCP.ID, "textures/entity/flak_shard.png");

    public FlakShardRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(FlakShard p_110775_1_) {
        return texture;
    }

    @Override
    public void render(FlakShard p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack matrixStack, IRenderTypeBuffer p_225623_5_, int light) {
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, matrixStack, p_225623_5_, light);
        matrixStack.pushPose();
        matrixStack.translate(0, -1.35, 0);
        flakShardModel.renderToBuffer(matrixStack, p_225623_5_.getBuffer(flakShardModel.renderType(getTextureLocation(p_225623_1_))), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        matrixStack.popPose();
    }
}
