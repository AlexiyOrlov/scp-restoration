package dev.buildtool.scp.scps.monsterpot;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.buildtool.satako.Functions;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PotMonsterModel3 extends PotMonsterModel {
    private final ModelRenderer leftArm;
    private final ModelRenderer leftArm_r1;
    private final ModelRenderer leftLeg;
    private final ModelRenderer leftLeg_r1;
    private final ModelRenderer rightArm;
    private final ModelRenderer rightArm_r1;
    private final ModelRenderer rightLeg;
    private final ModelRenderer rightLeg_r1;
    private final ModelRenderer head;
    private final ModelRenderer bb_main;

    public PotMonsterModel3() {
        texHeight = 16;
        texWidth = 16;
        leftArm = new ModelRenderer(this);
        leftArm.setPos(2.0F, 19.0F, -4.0F);


        leftArm_r1 = new ModelRenderer(this);
        leftArm_r1.setPos(-0.75F, 0.0F, 2.5F);
        leftArm.addChild(leftArm_r1);
        setRotationAngle(leftArm_r1, -0.1745F, 0.0F, -0.5672F);
        leftArm_r1.texOffs(0, 0).addBox(-0.5F, 1.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        leftLeg = new ModelRenderer(this);
        leftLeg.setPos(1.5F, 20.0F, 1.5F);


        leftLeg_r1 = new ModelRenderer(this);
        leftLeg_r1.setPos(0.0F, -1.0F, 0.0F);
        leftLeg.addChild(leftLeg_r1);
        setRotationAngle(leftLeg_r1, 0.4363F, 0.0F, 0.0F);
        leftLeg_r1.texOffs(0, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);

        rightArm = new ModelRenderer(this);
        rightArm.setPos(2.0F, 19.0F, -4.0F);


        rightArm_r1 = new ModelRenderer(this);
        rightArm_r1.setPos(-3.25F, 0.0F, 2.5F);
        rightArm.addChild(rightArm_r1);
        setRotationAngle(rightArm_r1, -0.1745F, 0.0F, 0.5672F);
        rightArm_r1.texOffs(0, 0).addBox(-0.5F, 1.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);

        rightLeg = new ModelRenderer(this);
        rightLeg.setPos(-1.5F, 19.0F, 1.5F);


        rightLeg_r1 = new ModelRenderer(this);
        rightLeg_r1.setPos(0.0F, 0.0F, 0.0F);
        rightLeg.addChild(rightLeg_r1);
        setRotationAngle(rightLeg_r1, 0.4363F, 0.0F, 0.0F);
        rightLeg_r1.texOffs(0, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 18.0F, -2.0F);
        head.texOffs(3, 2).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);
        bb_main.texOffs(0, 9).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);

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
        leftArm.render(matrixStack, buffer, packedLight, packedOverlay);
        leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        rightArm.render(matrixStack, buffer, packedLight, packedOverlay);
        rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}