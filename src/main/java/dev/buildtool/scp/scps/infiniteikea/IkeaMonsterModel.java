package dev.buildtool.scp.scps.infiniteikea;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.buildtool.satako.Functions;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class IkeaMonsterModel extends EntityModel<IkeaMonster> {
	private final ModelRenderer Head;
	private final ModelRenderer Body;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;

	public IkeaMonsterModel() {
		texWidth = 64;
		texHeight = 64;

		Head = new ModelRenderer(this);
		Head.setPos(0.0F, -13.0F, 0.0F);
		Head.texOffs(12, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);

		Body = new ModelRenderer(this);
		Body.setPos(0.0F, 0.0F, 0.0F);
		Body.texOffs(0, 38).addBox(-5.0F, -13.0F, -2.0F, 10.0F, 22.0F, 4.0F, 0.0F, false);

		RightArm = new ModelRenderer(this);
		RightArm.setPos(-6.5F, -12.25F, 0.0F);
		RightArm.texOffs(48, 41).addBox(-2.5F, 0.25F, -2.0F, 4.0F, 19.0F, 4.0F, 0.0F, false);

		LeftArm = new ModelRenderer(this);
		LeftArm.setPos(6.5F, -12.25F, 0.0F);
		LeftArm.texOffs(48, 41).addBox(-1.5F, 0.25F, -2.0F, 4.0F, 19.0F, 4.0F, 0.0F, false);

		RightLeg = new ModelRenderer(this);
		RightLeg.setPos(-2.4F, 9.25F, 0.0F);
		RightLeg.texOffs(0, 17).addBox(-2.5F, -0.25F, -2.0F, 4.0F, 15.0F, 4.0F, 0.0F, false);

		LeftLeg = new ModelRenderer(this);
		LeftLeg.setPos(2.4F, 9.25F, 0.0F);
		LeftLeg.texOffs(0, 17).addBox(-1.5F, -0.25F, -2.0F, 4.0F, 15.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(IkeaMonster entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		Head.xRot = Functions.getDefaultHeadPitch(headPitch);
		Head.yRot = Functions.getDefaultHeadYaw(netHeadYaw);
		RightLeg.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount / 2);
		LeftLeg.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount / 2);
		RightArm.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount / 2);
		LeftArm.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount / 2);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
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