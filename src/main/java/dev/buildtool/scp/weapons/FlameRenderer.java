package dev.buildtool.scp.weapons;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class FlameRenderer extends EntityRenderer<Flame> {
    private final FlameModel flameModel = new FlameModel();

    public FlameRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(Flame p_110775_1_) {
        return null;
    }

    @Override
    public void render(Flame p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack matrixStack, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, matrixStack, p_225623_5_, p_225623_6_);
        matrixStack.pushPose();
        matrixStack.translate(0, -1.35, 0);
        flameModel.setupAnim(p_225623_1_, p_225623_2_, p_225623_3_, 0, 0, 0);
        matrixStack.popPose();
    }
}
