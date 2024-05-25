package dev.buildtool.scp.scps.sculpture;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
@Deprecated
public class SculptureModel extends EntityModel<Sculpture> {
    private final ModelRenderer bb_main;

    public SculptureModel() {
        texWidth = 64;
        texHeight = 64;

        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);
        bb_main.texOffs(0, 0).addBox(2.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        bb_main.texOffs(44, 46).addBox(-3.0F, -23.0F, -2.0F, 6.0F, 14.0F, 4.0F, 0.0F, false);
        bb_main.texOffs(36, 0).addBox(-4.0F, -31.0F, -3.0F, 8.0F, 8.0F, 6.0F, 0.0F, false);
        bb_main.texOffs(22, 54).addBox(3.0F, -21.0F, -8.0F, 2.0F, 2.0F, 8.0F, 0.0F, false);
        bb_main.texOffs(0, 54).addBox(-5.0F, -21.0F, -8.0F, 2.0F, 2.0F, 8.0F, 0.0F, true);
        bb_main.texOffs(10, 0).addBox(-4.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, true);
    }

    @Override
    public void setupAnim(Sculpture entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
    }


}