package dev.buildtool.scp.scps.wallofflesh;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SCP2059 extends EntityModel<WallOfFlesh> {
    private final ModelRenderer bb_main;

    public SCP2059() {
        texWidth = 128;
        texHeight = 128;

        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);
        bb_main.texOffs(0, 0).addBox(-8.0F, -32.0F, -16.0F, 16.0F, 32.0F, 32.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(WallOfFlesh entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}