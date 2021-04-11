package dev.buildtool.scp.monsterpot;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.buildtool.satako.Functions;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PotMonsterModel1 extends PotMonsterModel {
    private final ModelRenderer bb_main;
    private final ModelRenderer head;
    private final ModelRenderer leftLeg;
    private final ModelRenderer rightLeg;
    private final ModelRenderer rightArm;
    private final ModelRenderer leftArm;

    public PotMonsterModel1() {
        texHeight = 16;
        texWidth = 16;
        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);
        bb_main.texOffs(0, 10).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, -3.5F, -1.0F);
        bb_main.addChild(head);
        head.texOffs(2, 0).addBox(-2.0F, -1.5F, -3.0F, 4.0F, 3.0F, 3.0F, 0.0F, false);

        leftLeg = new ModelRenderer(this);
        leftLeg.setPos(1.5F, -3.0F, 2.5F);
        bb_main.addChild(leftLeg);
        leftLeg.texOffs(8, 7).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        rightLeg = new ModelRenderer(this);
        rightLeg.setPos(1.5F, -3.0F, 2.5F);
        bb_main.addChild(rightLeg);
        rightLeg.texOffs(8, 7).addBox(-3.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        rightArm = new ModelRenderer(this);
        rightArm.setPos(-1.5F, -3.0F, -0.5F);
        bb_main.addChild(rightArm);
        rightArm.texOffs(8, 7).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        leftArm = new ModelRenderer(this);
        leftArm.setPos(-1.5F, -3.0F, -0.5F);
        bb_main.addChild(leftArm);
        leftArm.texOffs(8, 7).addBox(2.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(PotMonster entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        leftArm.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount);
        rightArm.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount);
        leftLeg.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount);
        rightLeg.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount);
        head.xRot = Functions.getDefaultHeadPitch(headPitch);
        head.yRot = Functions.getDefaultHeadYaw(netHeadYaw);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
    }


}