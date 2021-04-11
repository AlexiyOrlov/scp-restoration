package dev.buildtool.scp.shyguy;// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.buildtool.satako.Functions;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ShyGuyModelActive extends EntityModel<ShyguyEntity> {
    private final ModelRenderer rightLeg;
    private final ModelRenderer body;
    private final ModelRenderer leftLeg;
    private final ModelRenderer rightArm;
    private final ModelRenderer forearm_r1;
    private final ModelRenderer head;
    private final ModelRenderer bone;
    private final ModelRenderer cube_r1;
    private final ModelRenderer head_r1;
    private final ModelRenderer leftArm;
    private final ModelRenderer forearm_r2;
    private final ModelRenderer bb_main;

    public ShyGuyModelActive() {
        texHeight = 64;
        texWidth = 64;

        rightLeg = new ModelRenderer(this);
        rightLeg.setPos(-2.0F, 7.25F, -0.5F);
        rightLeg.texOffs(0, 0).addBox(-1.0F, -0.25F, -0.5F, 2.0F, 16.0F, 2.0F, 0.0F, false);
        rightLeg.texOffs(0, 0).addBox(-1.0F, 15.75F, -2.5F, 2.0F, 1.0F, 4.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setPos(0.0F, 6.0F, 0.0F);
        body.texOffs(44, 15).addBox(-3.0F, -15.0F, -2.0F, 6.0F, 16.0F, 4.0F, 0.0F, false);

        leftLeg = new ModelRenderer(this);
        leftLeg.setPos(2.0F, 7.25F, -0.5F);
        leftLeg.texOffs(0, 0).addBox(-1.0F, -0.25F, -0.5F, 2.0F, 16.0F, 2.0F, 0.0F, false);
        leftLeg.texOffs(0, 0).addBox(-1.0F, 15.75F, -2.5F, 2.0F, 1.0F, 4.0F, 0.0F, false);

        rightArm = new ModelRenderer(this);
        rightArm.setPos(-4.8333F, -8.0F, 0.0F);
        rightArm.texOffs(0, 0).addBox(-1.1667F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        rightArm.texOffs(0, 0).addBox(-1.1667F, 1.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        forearm_r1 = new ModelRenderer(this);
        forearm_r1.setPos(-0.1667F, 9.0F, 0.0F);
        rightArm.addChild(forearm_r1);
        setRotationAngle(forearm_r1, -1.1345F, 0.0F, 0.0F);
        forearm_r1.texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, -9.0F, 0.0F);
        setRotationAngle(head, 0.829F, 0.0F, 0.0F);
        head.texOffs(0, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, -3.75F, -0.75F);
        head.addChild(bone);
        setRotationAngle(bone, -0.7418F, 0.0F, 0.0F);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0F, 2.25F, -0.75F);
        bone.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.0436F, 0.0F, 0.0F);
        cube_r1.texOffs(39, 7).addBox(-1.0F, -1.75F, -0.5F, 2.0F, 3.0F, 1.0F, 0.0F, false);

        head_r1 = new ModelRenderer(this);
        head_r1.setPos(0.0F, 0.75F, 0.75F);
        bone.addChild(head_r1);
        setRotationAngle(head_r1, -0.0436F, 0.0F, 0.0F);
        head_r1.texOffs(48, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        leftArm = new ModelRenderer(this);
        leftArm.setPos(4.8333F, -8.0F, 0.0F);
        leftArm.texOffs(0, 0).addBox(-1.8333F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, 0.0F, true);
        leftArm.texOffs(0, 0).addBox(-0.8333F, 1.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);

        forearm_r2 = new ModelRenderer(this);
        forearm_r2.setPos(0.1667F, 9.0F, 0.0F);
        leftArm.addChild(forearm_r2);
        setRotationAngle(forearm_r2, -1.1345F, 0.0F, 0.0F);
        forearm_r2.texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, true);

        bb_main = new ModelRenderer(this);
//        bb_main.setPos(0.0F, 24.0F, 0.0F);
    }

    @Override
    public void setupAnim(ShyguyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
        head.xRot = Functions.getDefaultHeadPitch(headPitch) + Functions.degreesToRadians(45);
        head.yRot = Functions.getDefaultHeadYaw(netHeadYaw);
        leftArm.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount);
        rightArm.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount);
        leftLeg.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount);
        rightLeg.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        rightArm.render(matrixStack, buffer, packedLight, packedOverlay);
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        leftArm.render(matrixStack, buffer, packedLight, packedOverlay);
        bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}