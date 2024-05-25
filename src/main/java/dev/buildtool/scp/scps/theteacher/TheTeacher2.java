package dev.buildtool.scp.scps.theteacher;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class TheTeacher2 extends EntityModel<TheTeacherEntity> {
    private final ModelRenderer all;
    private final ModelRenderer side;
    private final ModelRenderer side2;
    private final ModelRenderer side3;
    private final ModelRenderer side4;
    private final ModelRenderer side5;
    private final ModelRenderer side6;

    public TheTeacher2() {
        texHeight = 16;
        texWidth = 16;

        all = new ModelRenderer(this);
        all.setPos(0.0F, 16.0F, 0.0F);
        all.texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        side = new ModelRenderer(this);
        side.setPos(0.0F, 0.0F, 0.0F);
        all.addChild(side);
        side.texOffs(0, 0).addBox(4.0F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);
        side.texOffs(0, 0).addBox(5.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);

        side2 = new ModelRenderer(this);
        side2.setPos(0.0F, 0.0F, 0.0F);
        all.addChild(side2);
        side2.texOffs(0, 0).addBox(-5.0F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, true);
        side2.texOffs(0, 0).addBox(-6.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, true);

        side3 = new ModelRenderer(this);
        side3.setPos(0.0F, 0.0F, 0.0F);
        all.addChild(side3);
        setRotationAngle(side3, 0.0F, 0.0F, 1.5708F);
        side3.texOffs(0, 0).addBox(-5.0F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, true);
        side3.texOffs(0, 0).addBox(-6.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, true);

        side4 = new ModelRenderer(this);
        side4.setPos(0.0F, 0.0F, 0.0F);
        all.addChild(side4);
        setRotationAngle(side4, 0.0F, 0.0F, -1.5708F);
        side4.texOffs(0, 0).addBox(-5.0F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, true);
        side4.texOffs(0, 0).addBox(-6.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, true);

        side5 = new ModelRenderer(this);
        side5.setPos(0.0F, 0.0F, 0.0F);
        all.addChild(side5);
        setRotationAngle(side5, 0.0F, 1.5708F, 0.0F);
        side5.texOffs(0, 0).addBox(4.0F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);
        side5.texOffs(0, 0).addBox(5.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);

        side6 = new ModelRenderer(this);
        side6.setPos(0.0F, 0.0F, 0.0F);
        all.addChild(side6);
        setRotationAngle(side6, 0.0F, -1.5708F, 0.0F);
        side6.texOffs(0, 0).addBox(4.0F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);
        side6.texOffs(0, 0).addBox(5.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(TheTeacherEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.level.getNearestPlayer(entity, 5) != null) {
            all.zRot = ageInTicks / 16;
            all.xRot = ageInTicks / 16;
        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        all.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}