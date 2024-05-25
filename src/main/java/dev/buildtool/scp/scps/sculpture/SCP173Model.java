package dev.buildtool.scp.scps.sculpture;// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SCP173Model extends EntityModel<Sculpture> {
    private final ModelRenderer bb_main;

    public SCP173Model() {
        texWidth = 64;
        texHeight = 64;

        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);
        bb_main.texOffs(0, 0).addBox(-4.0F, -25.0F, -1.0F, 8.0F, 8.0F, 6.0F, 0.0F, false);
        bb_main.texOffs(28, 27).addBox(-3.0F, -9.0F, 1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        bb_main.texOffs(0, 27).addBox(-3.0F, -17.0F, 0.0F, 6.0F, 8.0F, 4.0F, 0.0F, false);
        bb_main.texOffs(20, 27).addBox(1.0F, -9.0F, 1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        bb_main.texOffs(24, 15).addBox(4.0F, -23.0F, -8.0F, 2.0F, 2.0F, 10.0F, 0.0F, false);
        bb_main.texOffs(0, 15).addBox(-6.0F, -23.0F, -8.0F, 2.0F, 2.0F, 10.0F, 0.0F, false);
        bb_main.texOffs(28, 0).addBox(-3.0F, -35.0F, -2.0F, 6.0F, 10.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Sculpture entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bb_main.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}