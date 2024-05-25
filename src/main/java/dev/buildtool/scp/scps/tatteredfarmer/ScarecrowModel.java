package dev.buildtool.scp.scps.tatteredfarmer;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ScarecrowModel extends EntityModel<TatteredFarmer> {
    private final ModelRenderer bb_main;

    public ScarecrowModel() {
        texWidth = 64;
        texHeight = 64;

        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);
        bb_main.texOffs(56, 6).addBox(-1.0F, -32.0F, -1.0F, 2.0F, 32.0F, 2.0F, 0.0F, false);
        bb_main.texOffs(0, 0).addBox(-17.0F, -34.0F, -1.0F, 34.0F, 2.0F, 2.0F, 0.0F, false);
        bb_main.texOffs(35, 59).addBox(-1.0F, -37.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
        bb_main.texOffs(35, 48).addBox(-2.0F, -42.0F, -2.0F, 4.0F, 5.0F, 4.0F, 0.0F, false);
        bb_main.texOffs(37, 16).addBox(-2.0F, -46.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);
        bb_main.texOffs(22, 29).addBox(-4.0F, -43.0F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);
        bb_main.texOffs(0, 40).addBox(-5.0F, -34.5F, -3.0F, 10.0F, 18.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(TatteredFarmer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}