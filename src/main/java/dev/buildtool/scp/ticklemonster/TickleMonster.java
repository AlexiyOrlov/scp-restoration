package dev.buildtool.scp.ticklemonster;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
public class TickleMonster extends EntityModel<TickleMonsterEntity> {
	private final ModelRenderer bb_main;

	public TickleMonster() {
		texWidth = 128;
		texHeight = 256;

		bb_main = new ModelRenderer(this);
		bb_main.setPos(0.0F, 24.0F, 0.0F);
		bb_main.texOffs(0, 0).addBox(-16.0F, -3.0F, -16.0F, 32.0F, 3.0F, 32.0F, 0.0F, false);
		bb_main.texOffs(0, 40).addBox(-14.0F, -7.0F, -14.0F, 28.0F, 3.0F, 28.0F, 0.0F, false);
		bb_main.texOffs(0, 105).addBox(-13.0F, -10.0F, -13.0F, 26.0F, 3.0F, 26.0F, 0.0F, false);
		bb_main.texOffs(0, 75).addBox(-11.0F, -13.0F, -11.0F, 22.0F, 3.0F, 22.0F, 0.0F, false);
		bb_main.texOffs(0, 139).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 3.0F, 16.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(TickleMonsterEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
	}
}