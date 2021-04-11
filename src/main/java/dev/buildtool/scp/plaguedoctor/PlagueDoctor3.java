package dev.buildtool.scp.plaguedoctor;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class PlagueDoctor3 extends BipedModel<PlagueDoctor> {
	private final ModelRenderer Head;
	private final ModelRenderer Body;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;

	public PlagueDoctor3() {
		super(1);
		texWidth = 64;
		texHeight = 64;

		Head = new ModelRenderer(this);
		Head.setPos(0.0F, 0.0F, 0.0F);
		Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);
		Head.texOffs(52, 0).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 4.0F, 2.0F, 0.0F, false);
		Head.texOffs(26, 0).addBox(-2.0F, -3.0F, -8.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);
		Head.texOffs(0, 0).addBox(-1.0F, -2.0F, -10.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		Head.texOffs(16, 34).addBox(-6.0F, -8.25F, -6.0F, 12.0F, 1.0F, 12.0F, 0.0F, false);
		Head.texOffs(28, 35).addBox(-3.0F, -14.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
		Head.texOffs(0, 0).addBox(3.0F, -6.0F, -5.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		Head.texOffs(0, 0).addBox(-4.0F, -6.0F, -5.0F, 1.0F, 3.0F, 1.0F, 0.0F, true);
		Head.texOffs(37, 0).addBox(-3.0F, -6.0F, -5.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);
		Head.texOffs(38, 0).addBox(-1.0F, -5.0F, -5.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		Head.texOffs(0, 0).addBox(2.0F, -4.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		Head.texOffs(0, 0).addBox(-3.0F, -4.0F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, true);

		Body = new ModelRenderer(this);
		Body.setPos(0.0F, 0.0F, 0.0F);
		Body.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);

		RightArm = new ModelRenderer(this);
		RightArm.setPos(-5.0F, 2.0F, 0.0F);
		RightArm.texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		LeftArm = new ModelRenderer(this);
		LeftArm.setPos(5.0F, 2.0F, 0.0F);
		LeftArm.texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		RightLeg = new ModelRenderer(this);
		RightLeg.setPos(-1.9F, 12.0F, 0.0F);
		RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		LeftLeg = new ModelRenderer(this);
		LeftLeg.setPos(1.9F, 12.0F, 0.0F);
		LeftLeg.texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(PlagueDoctor entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		setRotationAngle(Head,head.xRot,head.yRot,head.zRot);
		setRotationAngle(RightArm,rightArm.xRot,rightArm.yRot,rightArm.zRot);
		setRotationAngle(LeftArm,leftArm.xRot,leftArm.yRot,leftArm.zRot);
		setRotationAngle(RightLeg,rightLeg.xRot,rightLeg.yRot,rightLeg.zRot);
		setRotationAngle(LeftLeg,leftLeg.xRot,leftLeg.yRot,leftLeg.zRot);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		Head.render(matrixStack, buffer, packedLight, packedOverlay);
		Body.render(matrixStack, buffer, packedLight, packedOverlay);
		RightArm.render(matrixStack, buffer, packedLight, packedOverlay);
		LeftArm.render(matrixStack, buffer, packedLight, packedOverlay);
		RightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
		LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

}