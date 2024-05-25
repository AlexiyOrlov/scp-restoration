package dev.buildtool.scp.scps.flaregun;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.buildtool.scp.SCP;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class FlareRenderer extends EntityRenderer<Flare> {
    private final FlareModel flareModel = new FlareModel();
    private final ResourceLocation texture = new ResourceLocation(SCP.ID, "textures/entity/rocket.png");

    public FlareRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(Flare p_110775_1_) {
        return texture;
    }

    @Override
    public void render(Flare p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int light) {
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, light);
        flareModel.renderToBuffer(p_225623_4_, p_225623_5_.getBuffer(flareModel.renderType(getTextureLocation(p_225623_1_))), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
