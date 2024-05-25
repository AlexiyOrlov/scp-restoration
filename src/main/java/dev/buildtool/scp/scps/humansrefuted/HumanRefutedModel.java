package dev.buildtool.scp.scps.humansrefuted;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.buildtool.satako.Functions;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class HumanRefutedModel extends EntityModel<HumanRefuted> {
    private final ModelRenderer leftLeg;
    private final ModelRenderer leftCalf_r1;
    private final ModelRenderer leftHip_r1;
    private final ModelRenderer rightLeg2;
    private final ModelRenderer rightCalf_r1;
    private final ModelRenderer rightHip_r1;
    private final ModelRenderer leftArm;
    private final ModelRenderer leftFinger2_r1;
    private final ModelRenderer leftForearm_r1;
    private final ModelRenderer rightArm;
    private final ModelRenderer rightFinger3_r1;
    private final ModelRenderer rightForearm_r1;
    private final ModelRenderer bb_main;
    private final ModelRenderer neckHead;
    private float scale;

    public HumanRefutedModel(float scale) {
        this.scale = scale;
        this.texWidth = 64;
        texHeight = 64;

        leftLeg = new ModelRenderer(this);
        leftLeg.setPos(6.0F, 6.8333F, -0.5F);
        leftLeg.texOffs(1, 46).addBox(-2.0F, 14.1667F, -2.5F, 4.0F, 3.0F, 5.0F, 0.0F, false);

        leftCalf_r1 = new ModelRenderer(this);
        leftCalf_r1.setPos(1.0F, 15.1667F, 0.5F);
        leftLeg.addChild(leftCalf_r1);
        setRotationAngle(leftCalf_r1, 0.3491F, 0.0F, 0.0F);
        leftCalf_r1.texOffs(0, 12).addBox(-2.0F, -8.0F, 0.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        leftHip_r1 = new ModelRenderer(this);
        leftHip_r1.setPos(2.0F, 7.1667F, -3.5F);
        leftLeg.addChild(leftHip_r1);
        setRotationAngle(leftHip_r1, -0.3491F, 0.0F, 0.0F);
        leftHip_r1.texOffs(10, 0).addBox(-4.0F, -8.0F, 0.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

        rightLeg2 = new ModelRenderer(this);
        rightLeg2.setPos(-6.0F, 7.1418F, -0.0842F);
        rightLeg2.texOffs(27, 5).addBox(-2.0F, 13.8582F, -2.9158F, 4.0F, 3.0F, 5.0F, 0.0F, false);

        rightCalf_r1 = new ModelRenderer(this);
        rightCalf_r1.setPos(1.0F, 14.8582F, 0.0842F);
        rightLeg2.addChild(rightCalf_r1);
        setRotationAngle(rightCalf_r1, 0.3491F, 0.0F, 0.0F);
        rightCalf_r1.texOffs(38, 26).addBox(-2.0F, -8.0F, 0.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        rightHip_r1 = new ModelRenderer(this);
        rightHip_r1.setPos(0.0F, 3.7835F, -0.6684F);
        rightLeg2.addChild(rightHip_r1);
        setRotationAngle(rightHip_r1, -0.3491F, 0.0F, 0.0F);
        rightHip_r1.texOffs(48, 28).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);

        leftArm = new ModelRenderer(this);
        leftArm.setPos(7.0F, -8.0F, -2.0F);
        setRotationAngle(leftArm, 0.0F, 0.0F, -0.48F);
        leftArm.texOffs(56, 14).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        leftArm.texOffs(36, 14).addBox(-2.5312F, 10.6041F, -10.0F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        leftFinger2_r1 = new ModelRenderer(this);
        leftFinger2_r1.setPos(-0.2572F, 12.0276F, -6.5F);
        leftArm.addChild(leftFinger2_r1);
        setRotationAngle(leftFinger2_r1, 0.3054F, 0.0F, 0.0F);
        leftFinger2_r1.texOffs(36, 20).addBox(-0.5F, -0.3496F, -3.0231F, 1.0F, 1.0F, 3.0F, 0.0F, false);

        leftForearm_r1 = new ModelRenderer(this);
        leftForearm_r1.setPos(-1.0F, 8.5F, 2.0F);
        leftArm.addChild(leftForearm_r1);
        setRotationAngle(leftForearm_r1, -1.2217F, 0.0F, 0.0F);
        leftForearm_r1.texOffs(46, 14).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);

        rightArm = new ModelRenderer(this);
        rightArm.setPos(-7.0F, -8.0F, -2.0F);
        setRotationAngle(rightArm, 0.0F, 0.0F, 0.48F);
        rightArm.texOffs(46, 0).addBox(0.0F, 0.0F, 0.0F, 2.0F, 9.0F, 2.0F, 0.0F, true);
        rightArm.texOffs(28, 0).addBox(1.5312F, 10.6041F, -10.0F, 1.0F, 1.0F, 3.0F, 0.0F, true);

        rightFinger3_r1 = new ModelRenderer(this);
        rightFinger3_r1.setPos(0.2572F, 12.0276F, -6.5F);
        rightArm.addChild(rightFinger3_r1);
        setRotationAngle(rightFinger3_r1, 0.3054F, 0.0F, 0.0F);
        rightFinger3_r1.texOffs(37, 0).addBox(-0.5F, -0.3496F, -3.0231F, 1.0F, 1.0F, 3.0F, 0.0F, true);

        rightForearm_r1 = new ModelRenderer(this);
        rightForearm_r1.setPos(1.0F, 8.5F, 2.0F);
        rightArm.addChild(rightForearm_r1);
        setRotationAngle(rightForearm_r1, -1.2217F, 0.0F, 0.0F);
        rightForearm_r1.texOffs(56, 0).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, true);

        neckHead = new ModelRenderer(this);
        neckHead.setPos(0.0F, -12.25F, -3.0F);
        neckHead.texOffs(50, 56).addBox(-2.0F, -4.75F, -1.0F, 4.0F, 5.0F, 3.0F, 0.0F, false);
        neckHead.texOffs(42, 43).addBox(-3.0F, -10.75F, -3.0F, 6.0F, 6.0F, 5.0F, 0.0F, false);

        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);
        bb_main.texOffs(0, 46).addBox(-7.0F, -24.0F, -5.0F, 14.0F, 8.0F, 10.0F, 0.0F, false);
        bb_main.texOffs(0, 29).addBox(-6.0F, -32.0F, -4.0F, 12.0F, 8.0F, 8.0F, 0.0F, false);
        bb_main.texOffs(0, 17).addBox(-5.0F, -36.0F, -4.0F, 10.0F, 4.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(HumanRefuted entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        leftArm.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount);
        rightArm.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount);
        leftLeg.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount);
        rightLeg2.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount);
        neckHead.xRot = Functions.getDefaultHeadPitch(headPitch);
        neckHead.yRot = Functions.getDefaultHeadYaw(netHeadYaw);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (scale != 1) {
            matrixStack.scale(scale, scale, scale);
            matrixStack.translate(0, 1.5, 0);
        }
        leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        rightLeg2.render(matrixStack, buffer, packedLight, packedOverlay);
        leftArm.render(matrixStack, buffer, packedLight, packedOverlay);
        rightArm.render(matrixStack, buffer, packedLight, packedOverlay);
        neckHead.render(matrixStack, buffer, packedLight, packedOverlay);
        bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}