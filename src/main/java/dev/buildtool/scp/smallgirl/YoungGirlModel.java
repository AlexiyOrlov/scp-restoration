package dev.buildtool.scp.smallgirl;// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.buildtool.satako.Functions;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class YoungGirlModel extends EntityModel<YoungGirl> {
	private final ModelRenderer hairHead;
	private final ModelRenderer rightArm;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightLeg;
	private final ModelRenderer leftLeg;
	private final ModelRenderer bb_main;

	public YoungGirlModel() {
		texHeight = 64;
		texWidth	 = 64;

		hairHead = new ModelRenderer(this);
		hairHead.setPos(0.0F, 11.8462F, 0.5769F);
		hairHead.texOffs(58, 62).addBox(2.0F, -4.8462F, -0.5769F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(58, 62).addBox(2.0F, -3.8462F, 0.4231F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(58, 62).addBox(-3.0F, -3.8462F, 0.4231F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(60, 61).addBox(2.0F, -4.8462F, -1.5769F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(58, 61).addBox(2.0F, -4.8462F, 0.4231F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		hairHead.texOffs(58, 62).addBox(-3.0F, -4.8462F, -0.5769F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(60, 55).addBox(0.0F, -3.8462F, 1.4231F, 1.0F, 8.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(60, 56).addBox(2.0F, -3.8462F, 1.4231F, 1.0F, 7.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(60, 59).addBox(1.0F, -3.8462F, 1.4231F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(60, 59).addBox(-2.0F, -3.8462F, 1.4231F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(59, 56).addBox(-1.0F, -3.8462F, 1.4231F, 1.0F, 7.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(58, 61).addBox(-3.0F, -4.8462F, -1.5769F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(58, 61).addBox(-3.0F, -4.8462F, 0.4231F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		hairHead.texOffs(60, 60).addBox(2.0F, -3.8462F, -2.5769F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(60, 60).addBox(-3.0F, -3.8462F, -0.5769F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(60, 60).addBox(-3.0F, -3.8462F, -2.5769F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(60, 60).addBox(2.0F, -3.8462F, -0.5769F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(58, 62).addBox(-1.0F, -4.8462F, -2.5769F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(54, 61).addBox(-2.0F, -4.8462F, -3.5769F, 4.0F, 2.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(58, 62).addBox(-3.0F, -4.8462F, -2.5769F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(58, 62).addBox(1.0F, -4.8462F, -2.5769F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(54, 62).addBox(-2.0F, -4.8462F, -1.5769F, 4.0F, 1.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(52, 61).addBox(-2.0F, -4.8462F, 0.4231F, 4.0F, 1.0F, 2.0F, 0.0F, false);
		hairHead.texOffs(54, 62).addBox(-2.0F, -4.8462F, -0.5769F, 4.0F, 1.0F, 1.0F, 0.0F, false);
		hairHead.texOffs(0, 0).addBox(-2.0F, -3.8462F, -2.5769F, 4.0F, 4.0F, 4.0F, 0.0F, false);
		hairHead.texOffs(60, 55).addBox(-3.0F, -3.8462F, 1.4231F, 1.0F, 8.0F, 1.0F, 0.0F, false);

		rightArm = new ModelRenderer(this);
		rightArm.setPos(-3.0F, 12.25F, 0.0F);
		rightArm.texOffs(22, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		leftArm = new ModelRenderer(this);
		leftArm.setPos(3.0F, 12.25F, 0.0F);
		leftArm.texOffs(33, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, true);

		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(-1.0F, 18.0F, 0.0F);
		rightLeg.texOffs(22, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(1.0F, 18.0F, 0.0F);
		leftLeg.texOffs(33, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		bb_main = new ModelRenderer(this);
		bb_main.setPos(0.0F, 24.0F, 0.0F);
		bb_main.texOffs(0, 10).addBox(-2.0F, -12.0F, -1.0F, 4.0F, 6.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(YoungGirl entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		hairHead.xRot = Functions.getDefaultHeadPitch(headPitch);
		hairHead.yRot = Functions.getDefaultHeadYaw(netHeadYaw);
		rightArm.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount);
		leftArm.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount);
		rightLeg.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount);
		leftLeg.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hairHead.render(matrixStack, buffer, packedLight, packedOverlay);
		rightArm.render(matrixStack, buffer, packedLight, packedOverlay);
		leftArm.render(matrixStack, buffer, packedLight, packedOverlay);
		rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
		leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}