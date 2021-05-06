package dev.buildtool.scp.patchworkbear;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SCP2295 extends EntityModel<PatchworkBear> {
	private final ModelRenderer head;
	private final ModelRenderer leg;
	private final ModelRenderer leg2;
	private final ModelRenderer arm2;
	private final ModelRenderer cube_r1;
	private final ModelRenderer arm1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer bb_main;

	public SCP2295() {
		texWidth = 64;
		texHeight = 64;

		head = new ModelRenderer(this);
		head.setPos(0.0F, 13.0F, -0.125F);
		head.texOffs(0, 51).addBox(-1.0F, -2.0F, -3.375F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		head.texOffs(0, 57).addBox(-2.0F, -4.0F, -1.375F, 4.0F, 4.0F, 3.0F, 0.0F, false);
		head.texOffs(56, 0).addBox(1.0F, -6.0F, -0.375F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		head.texOffs(56, 0).addBox(-4.0F, -6.0F, -0.375F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		leg = new ModelRenderer(this);
		leg.setPos(1.0F, 19.0F, 0.0F);
		setRotationAngle(leg, 0.0F, 0.0F, -0.0873F);
		leg.texOffs(6, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

		leg2 = new ModelRenderer(this);
		leg2.setPos(-1.0F, 19.0F, 0.0F);
		setRotationAngle(leg2, 0.0F, 0.0F, 0.0873F);
		leg2.texOffs(0, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

		arm2 = new ModelRenderer(this);
		arm2.setPos(1.5F, 14.0F, 0.0F);


		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(1.0F, 0.0F, 0.0F);
		arm2.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, -0.6109F);
		cube_r1.texOffs(54, 58).addBox(-1.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, true);

		arm1 = new ModelRenderer(this);
		arm1.setPos(-1.5F, 14.0F, 0.0F);


		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(-1.0F, 0.0F, 0.0F);
		arm1.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.6109F);
		cube_r2.texOffs(54, 58).addBox(0.5F, 0.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

		bb_main = new ModelRenderer(this);
		bb_main.setPos(0.0F, 24.0F, 0.0F);
		bb_main.texOffs(0, 37).addBox(-2.0F, -10.0F, -1.0F, 4.0F, 5.0F, 2.0F, 0.0F, false);
		bb_main.texOffs(0, 46).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(PatchworkBear entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(matrixStack, buffer, packedLight, packedOverlay);
		leg.render(matrixStack, buffer, packedLight, packedOverlay);
		leg2.render(matrixStack, buffer, packedLight, packedOverlay);
		arm2.render(matrixStack, buffer, packedLight, packedOverlay);
		arm1.render(matrixStack, buffer, packedLight, packedOverlay);
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}