package dev.buildtool.scp.weapons;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.buildtool.scp.SCP;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RocketRenderer extends EntityRenderer<Rocket> {
    private final RocketModel2 model2 = new RocketModel2();
    private final ResourceLocation texture = new ResourceLocation(SCP.ID, "textures/entity/rocket.png");

    public RocketRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(Rocket p_110775_1_) {
        return texture;
    }

    @Override
    public void render(Rocket p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack matrixStack, IRenderTypeBuffer p_225623_5_, int light) {
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, matrixStack, p_225623_5_, light);
        matrixStack.pushPose();
        matrixStack.translate(0, -1.35, 0);
        model2.renderToBuffer(matrixStack, p_225623_5_.getBuffer(model2.renderType(getTextureLocation(p_225623_1_))), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        matrixStack.popPose();
    }
}
