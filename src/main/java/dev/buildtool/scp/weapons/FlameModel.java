package dev.buildtool.scp.weapons;// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.particles.ParticleTypes;

public class FlameModel extends EntityModel<Flame> {
    private final ModelRenderer bb_main;

    public FlameModel() {
        texWidth = 16;
        texHeight = 16;

        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);


        ModelRenderer cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0F, -0.5F, 0.0F);
        bb_main.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.7854F, -0.7854F, 0.0F);
        cube_r1.texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Flame entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        entity.level.addParticle(ParticleTypes.FLAME, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

	}

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}