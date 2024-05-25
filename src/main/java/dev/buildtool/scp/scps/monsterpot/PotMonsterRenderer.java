package dev.buildtool.scp.scps.monsterpot;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.buildtool.scp.SCP;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class PotMonsterRenderer extends LivingRenderer<PotMonster, PotMonsterModel> {
    private final ResourceLocation location = new ResourceLocation(SCP.ID, "textures/entity/pot_monster1.png");
    private final ResourceLocation location1 = new ResourceLocation(SCP.ID, "textures/entity/pot_monster2.png");
    private final ResourceLocation location2 = new ResourceLocation(SCP.ID, "textures/entity/pot_monster3.png");
    PotMonsterModel1 model1 = new PotMonsterModel1();
    PotMonsterModel2 model2 = new PotMonsterModel2();
    PotMonsterModel3 model3 = new PotMonsterModel3();

    public PotMonsterRenderer(EntityRendererManager rendererManager, PotMonsterModel entityModelIn, float shadowSizeIn) {
        super(rendererManager, entityModelIn, shadowSizeIn);
    }

    @Override
    public void render(PotMonster entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        switch (entityIn.getVar()) {
            case 0:
                this.model = model1;
                break;
            case 1:
                model = model2;
                break;
            case 2:
                model = model3;
                break;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(PotMonster entity) {
        switch (entity.getVar()) {
            case 0:
                return location;
            case 1:
                return location1;
            case 2:
                return location2;
        }
        return new ResourceLocation(SCP.ID, "");
    }

    @Override
    protected boolean shouldShowName(PotMonster p_177070_1_) {
        return false;
    }
}
