package dev.buildtool.scp.shyguy;// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.buildtool.satako.Functions;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SCP096 extends EntityModel<ShyguyEntity> {
    private final ModelRenderer root;
    private final ModelRenderer Hips;
    private final ModelRenderer Belly;
    private final ModelRenderer Ribcage;
    private final ModelRenderer Neck;
    private final ModelRenderer Head;
    private final ModelRenderer Cheeks;
    private final ModelRenderer Lowerjaw;
    private final ModelRenderer Leftupperarm;
    private final ModelRenderer Leftlowerarm;
    private final ModelRenderer Lefthand;
    private final ModelRenderer Leftthumbbase;
    private final ModelRenderer Leftthumbtip;
    private final ModelRenderer Leftindexfingerbase;
    private final ModelRenderer Leftindexfingertip;
    private final ModelRenderer Leftmiddlefingerbase;
    private final ModelRenderer Leftmiddlefingertip;
    private final ModelRenderer Leftringfingerbase;
    private final ModelRenderer Leftringfingertip;
    private final ModelRenderer Leftlittlefingerbase;
    private final ModelRenderer Leftlittlefingertip;
    private final ModelRenderer Rightupperarm;
    private final ModelRenderer Rightlowerarm;
    private final ModelRenderer Righthand;
    private final ModelRenderer Rightthumbbase;
    private final ModelRenderer Rightthumbtip;
    private final ModelRenderer Rightindexfingerbase;
    private final ModelRenderer Rightindexfingertip;
    private final ModelRenderer Rightmiddlefingerbase;
    private final ModelRenderer Rightmiddlefingertip;
    private final ModelRenderer Rightringfingerbase;
    private final ModelRenderer Rightringfingertip;
    private final ModelRenderer Rightlittlefingerbase;
    private final ModelRenderer Rightlittlefingertip;
    private final ModelRenderer Leftthigh;
    private final ModelRenderer Leftshin;
    private final ModelRenderer Leftfoot;
    private final ModelRenderer Rightthigh;
    private final ModelRenderer Rightshin;
    private final ModelRenderer Leftfoot2;

    public SCP096() {
        texWidth = 100;
        texHeight = 100;

        root = new ModelRenderer(this);
        root.setPos(0.0F, 0.0F, 0.0F);


        Hips = new ModelRenderer(this);
        Hips.setPos(0.0F, -1.1F, 1.5F);
        root.addChild(Hips);
        Hips.texOffs(33, 78).addBox(-4.5F, -1.5F, -4.0F, 9.0F, 4.0F, 5.0F, 0.0F, false);

        Belly = new ModelRenderer(this);
        Belly.setPos(0.0F, -0.8F, -0.01F);
        Hips.addChild(Belly);
        setRotationAngle(Belly, 0.0637F, 0.0F, 0.0F);
        Belly.texOffs(35, 88).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 7.0F, 5.0F, 0.0F, false);

        Ribcage = new ModelRenderer(this);
        Ribcage.setPos(0.0F, -6.5F, 0.1F);
        Belly.addChild(Ribcage);
        setRotationAngle(Ribcage, -0.0637F, 0.0F, 0.0F);
        Ribcage.texOffs(0, 85).addBox(-5.5F, -8.0F, -5.0F, 11.0F, 9.0F, 6.0F, 0.0F, false);

        Neck = new ModelRenderer(this);
        Neck.setPos(0.0F, -8.0F, -0.2F);
        Ribcage.addChild(Neck);
        setRotationAngle(Neck, 0.0637F, 0.0F, 0.0F);
        Neck.texOffs(62, 91).addBox(-2.0F, -4.0F, -3.5F, 4.0F, 5.0F, 4.0F, 0.0F, false);

        Head = new ModelRenderer(this);
        Head.setPos(0.0F, -3.5F, -2.5F);
        Neck.addChild(Head);
        setRotationAngle(Head, -0.0637F, 0.0F, 0.0F);
        Head.texOffs(0, 69).addBox(-3.5F, -6.0F, -3.5F, 7.0F, 6.0F, 7.0F, 0.0F, false);

        Cheeks = new ModelRenderer(this);
        Cheeks.setPos(0.0F, -2.5F, 1.2F);
        Head.addChild(Cheeks);
        Cheeks.texOffs(62, 81).addBox(-3.0F, 0.0F, -4.0F, 6.0F, 4.0F, 5.0F, 0.0F, false);

        Lowerjaw = new ModelRenderer(this);
        Lowerjaw.setPos(0.0F, 2.5F, -0.7F);
        Cheeks.addChild(Lowerjaw);
        Lowerjaw.texOffs(57, 73).addBox(-2.5F, 0.0F, -4.0F, 5.0F, 2.0F, 5.0F, 0.0F, false);

        Leftupperarm = new ModelRenderer(this);
        Leftupperarm.setPos(-5.8F, -5.6F, -1.5F);
        Ribcage.addChild(Leftupperarm);
        setRotationAngle(Leftupperarm, 0.0637F, 0.0F, 0.0908F);
        Leftupperarm.texOffs(0, 52).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        Leftlowerarm = new ModelRenderer(this);
        Leftlowerarm.setPos(-0.9F, 9.8F, 2.0F);
        Leftupperarm.addChild(Leftlowerarm);
        setRotationAngle(Leftlowerarm, -0.1911F, 0.2122F, -0.1121F);
        Leftlowerarm.texOffs(0, 35).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        Lefthand = new ModelRenderer(this);
        Lefthand.setPos(-1.0F, 10.5F, -3.9F);
        Leftlowerarm.addChild(Lefthand);
        setRotationAngle(Lefthand, 0.1911F, 0.5095F, 0.0F);
        Lefthand.texOffs(0, 27).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 4.0F, 3.0F, 0.0F, false);

        Leftthumbbase = new ModelRenderer(this);
        Leftthumbbase.setPos(2.5F, 1.0F, 2.0F);
        Lefthand.addChild(Leftthumbbase);
        setRotationAngle(Leftthumbbase, 0.0213F, 0.3609F, 0.2122F);
        Leftthumbbase.texOffs(27, 0).addBox(-1.5F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);

        Leftthumbtip = new ModelRenderer(this);
        Leftthumbtip.setPos(-0.01F, 4.5F, 0.0F);
        Leftthumbbase.addChild(Leftthumbtip);
        setRotationAngle(Leftthumbtip, 0.1274F, 0.0F, -0.4671F);
        Leftthumbtip.texOffs(10, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        Leftindexfingerbase = new ModelRenderer(this);
        Leftindexfingerbase.setPos(1.8F, 3.0F, 0.0F);
        Lefthand.addChild(Leftindexfingerbase);
        setRotationAngle(Leftindexfingerbase, -0.0848F, 0.0F, 0.0F);
        Leftindexfingerbase.texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        Leftindexfingertip = new ModelRenderer(this);
        Leftindexfingertip.setPos(-0.01F, 4.5F, 0.0F);
        Leftindexfingerbase.addChild(Leftindexfingertip);
        setRotationAngle(Leftindexfingertip, 0.5943F, 0.0F, 0.0F);
        Leftindexfingertip.texOffs(5, 9).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        Leftmiddlefingerbase = new ModelRenderer(this);
        Leftmiddlefingerbase.setPos(0.6F, 2.8F, 0.0F);
        Lefthand.addChild(Leftmiddlefingerbase);
        setRotationAngle(Leftmiddlefingerbase, -0.1061F, 0.0F, 0.0F);
        Leftmiddlefingerbase.texOffs(10, 7).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        Leftmiddlefingertip = new ModelRenderer(this);
        Leftmiddlefingertip.setPos(-0.01F, 5.5F, 0.0F);
        Leftmiddlefingerbase.addChild(Leftmiddlefingertip);
        setRotationAngle(Leftmiddlefingertip, 0.4033F, 0.0F, 0.0F);
        Leftmiddlefingertip.texOffs(5, 9).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        Leftringfingerbase = new ModelRenderer(this);
        Leftringfingerbase.setPos(-0.6F, 3.0F, 0.0F);
        Lefthand.addChild(Leftringfingerbase);
        setRotationAngle(Leftringfingerbase, -0.1274F, 0.0F, 0.0F);
        Leftringfingerbase.texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        Leftringfingertip = new ModelRenderer(this);
        Leftringfingertip.setPos(-0.01F, 4.5F, 0.0F);
        Leftringfingerbase.addChild(Leftringfingertip);
        setRotationAngle(Leftringfingertip, 0.4882F, 0.0F, 0.0F);
        Leftringfingertip.texOffs(5, 9).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        Leftlittlefingerbase = new ModelRenderer(this);
        Leftlittlefingerbase.setPos(-1.9F, 3.0F, 0.0F);
        Lefthand.addChild(Leftlittlefingerbase);
        setRotationAngle(Leftlittlefingerbase, -0.0637F, 0.0F, 0.0F);
        Leftlittlefingerbase.texOffs(31, 7).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        Leftlittlefingertip = new ModelRenderer(this);
        Leftlittlefingertip.setPos(-0.01F, 3.5F, 0.0F);
        Leftlittlefingerbase.addChild(Leftlittlefingertip);
        setRotationAngle(Leftlittlefingertip, 0.4882F, 0.0F, 0.0F);
        Leftlittlefingertip.texOffs(5, 16).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        Rightupperarm = new ModelRenderer(this);
        Rightupperarm.setPos(5.8F, -5.6F, -1.5F);
        Ribcage.addChild(Rightupperarm);
        setRotationAngle(Rightupperarm, 0.0637F, 0.0F, -0.0908F);
        Rightupperarm.texOffs(17, 52).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        Rightlowerarm = new ModelRenderer(this);
        Rightlowerarm.setPos(0.9F, 9.8F, 2.0F);
        Rightupperarm.addChild(Rightlowerarm);
        setRotationAngle(Rightlowerarm, -0.1911F, -0.2122F, 0.0684F);
        Rightlowerarm.texOffs(17, 35).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        Righthand = new ModelRenderer(this);
        Righthand.setPos(0.0F, 10.5F, -3.9F);
        Rightlowerarm.addChild(Righthand);
        setRotationAngle(Righthand, 0.1911F, -0.5095F, 0.0F);
        Righthand.texOffs(17, 27).addBox(-1.5F, 0.0F, 0.0F, 5.0F, 4.0F, 3.0F, 0.0F, false);

        Rightthumbbase = new ModelRenderer(this);
        Rightthumbbase.setPos(-2.5F, 1.0F, 2.0F);
        Righthand.addChild(Rightthumbbase);
        setRotationAngle(Rightthumbbase, 0.0213F, -0.3609F, -0.2122F);
        Rightthumbbase.texOffs(15, 0).addBox(0.5F, 0.0F, -0.5F, 2.0F, 5.0F, 1.0F, 0.0F, false);

        Rightthumbtip = new ModelRenderer(this);
        Rightthumbtip.setPos(-0.01F, 4.5F, 0.0F);
        Rightthumbbase.addChild(Rightthumbtip);
        setRotationAngle(Rightthumbtip, 0.1274F, 0.0F, 0.4671F);
        Rightthumbtip.texOffs(22, 0).addBox(0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        Rightindexfingerbase = new ModelRenderer(this);
        Rightindexfingerbase.setPos(-1.8F, 3.0F, 0.0F);
        Righthand.addChild(Rightindexfingerbase);
        setRotationAngle(Rightindexfingerbase, -0.0848F, 0.0F, 0.0F);
        Rightindexfingerbase.texOffs(0, 0).addBox(0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        Rightindexfingertip = new ModelRenderer(this);
        Rightindexfingertip.setPos(-0.01F, 4.5F, 0.0F);
        Rightindexfingerbase.addChild(Rightindexfingertip);
        setRotationAngle(Rightindexfingertip, 0.5943F, 0.0F, 0.0F);
        Rightindexfingertip.texOffs(0, 9).addBox(0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        Rightmiddlefingerbase = new ModelRenderer(this);
        Rightmiddlefingerbase.setPos(-0.6F, 2.8F, 0.0F);
        Righthand.addChild(Rightmiddlefingerbase);
        setRotationAngle(Rightmiddlefingerbase, -0.1061F, 0.0F, 0.0F);
        Rightmiddlefingerbase.texOffs(17, 7).addBox(0.5F, 0.0F, 0.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        Rightmiddlefingertip = new ModelRenderer(this);
        Rightmiddlefingertip.setPos(-0.01F, 5.5F, 0.0F);
        Rightmiddlefingerbase.addChild(Rightmiddlefingertip);
        setRotationAngle(Rightmiddlefingertip, 0.4033F, 0.0F, 0.0F);
        Rightmiddlefingertip.texOffs(0, 9).addBox(0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        Rightringfingerbase = new ModelRenderer(this);
        Rightringfingerbase.setPos(0.6F, 3.0F, 0.0F);
        Righthand.addChild(Rightringfingerbase);
        setRotationAngle(Rightringfingerbase, -0.1274F, 0.0F, 0.0F);
        Rightringfingerbase.texOffs(0, 0).addBox(0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 2.0F, 0.0F, false);

        Rightringfingertip = new ModelRenderer(this);
        Rightringfingertip.setPos(-0.01F, 4.5F, 0.0F);
        Rightringfingerbase.addChild(Rightringfingertip);
        setRotationAngle(Rightringfingertip, 0.4882F, 0.0F, 0.0F);
        Rightringfingertip.texOffs(0, 9).addBox(0.5F, 0.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        Rightlittlefingerbase = new ModelRenderer(this);
        Rightlittlefingerbase.setPos(1.9F, 3.0F, 0.0F);
        Righthand.addChild(Rightlittlefingerbase);
        setRotationAngle(Rightlittlefingerbase, -0.0637F, 0.0F, 0.0F);
        Rightlittlefingerbase.texOffs(24, 7).addBox(0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);

        Rightlittlefingertip = new ModelRenderer(this);
        Rightlittlefingertip.setPos(-0.01F, 3.5F, 0.0F);
        Rightlittlefingerbase.addChild(Rightlittlefingertip);
        setRotationAngle(Rightlittlefingertip, 0.4882F, 0.0F, 0.0F);
        Rightlittlefingertip.texOffs(0, 16).addBox(0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);

        Leftthigh = new ModelRenderer(this);
        Leftthigh.setPos(-2.4F, 1.0F, -1.5F);
        Hips.addChild(Leftthigh);
        setRotationAngle(Leftthigh, -0.0424F, 0.0F, 0.0F);
        Leftthigh.texOffs(34, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, false);

        Leftshin = new ModelRenderer(this);
        Leftshin.setPos(0.0F, 10.5F, -1.3F);
        Leftthigh.addChild(Leftshin);
        setRotationAngle(Leftshin, 0.1911F, 0.0F, 0.0F);
        Leftshin.texOffs(34, 61).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 13.0F, 3.0F, 0.0F, false);

        Leftfoot = new ModelRenderer(this);
        Leftfoot.setPos(0.0F, 12.0F, 1.5F);
        Leftshin.addChild(Leftfoot);
        setRotationAngle(Leftfoot, -0.1485F, 0.0F, 0.0F);
        Leftfoot.texOffs(34, 35).addBox(-2.0F, 0.0F, -5.5F, 4.0F, 2.0F, 7.0F, 0.0F, false);

        Rightthigh = new ModelRenderer(this);
        Rightthigh.setPos(2.4F, 1.0F, -1.5F);
        Hips.addChild(Rightthigh);
        setRotationAngle(Rightthigh, -0.0424F, 0.0F, 0.0F);
        Rightthigh.texOffs(51, 45).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, false);

        Rightshin = new ModelRenderer(this);
        Rightshin.setPos(0.0F, 10.5F, -1.3F);
        Rightthigh.addChild(Rightshin);
        setRotationAngle(Rightshin, 0.1911F, 0.0F, 0.0F);
        Rightshin.texOffs(47, 61).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 13.0F, 3.0F, 0.0F, false);

        Leftfoot2 = new ModelRenderer(this);
        Leftfoot2.setPos(0.0F, 12.0F, 1.5F);
        Rightshin.addChild(Leftfoot2);
        setRotationAngle(Leftfoot2, -0.1485F, 0.0F, 0.0F);
        Leftfoot2.texOffs(50, 32).addBox(-2.0F, 0.0F, -5.5F, 4.0F, 2.0F, 7.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(ShyguyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        ShyguyEntity.State state = entity.getState();
        switch (state) {
            case IDLE:
                Neck.xRot = Functions.getDefaultHeadPitch(headPitch);
                Neck.yRot = Functions.getDefaultHeadYaw(netHeadYaw);
                Leftupperarm.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount);
                Rightupperarm.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount);
                Leftthigh.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount);
                Rightthigh.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount);
                Leftlowerarm.xRot = 0;
                Leftlowerarm.yRot = 0;
                Rightlowerarm.xRot = 0;
                Rightlowerarm.yRot = 0;
                Righthand.yRot = Functions.degreesToRadians(-45);
                Lefthand.yRot = Functions.degreesToRadians(45);
                Head.xRot = 0;
                break;
            case CRYING:
                Head.xRot = Functions.degreesToRadians(30);
                Neck.yRot = 0;
                Leftupperarm.xRot = -45;
                Rightupperarm.xRot = -45;
                Leftlowerarm.xRot = Functions.degreesToRadians(-90);
                Rightlowerarm.xRot = Functions.degreesToRadians(-90);
                Righthand.yRot = -90;
                Lefthand.yRot = 90;
                Righthand.xRot = Functions.degreesToRadians(40);
                Lefthand.xRot = Functions.degreesToRadians(40);
                Leftlowerarm.yRot = 0;
                Rightlowerarm.yRot = 0;
                break;

            case ACTIVE:
                Neck.xRot = Functions.getDefaultHeadPitch(headPitch);
                Neck.yRot = Functions.getDefaultHeadYaw(netHeadYaw);
                Rightlowerarm.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount / 2) - Functions.degreesToRadians(45);
                Rightlowerarm.yRot = 0;
                Leftlowerarm.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount / 2) - Functions.degreesToRadians(45);
                Leftlowerarm.yRot = 0;
                Righthand.yRot = 0;
                Lefthand.yRot = 0;
                Leftupperarm.xRot = 0;
                Rightupperarm.xRot = 0;
                Leftthigh.xRot = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount);
                Rightthigh.xRot = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount);
                Righthand.yRot = Functions.degreesToRadians(-45);
                Lefthand.yRot = Functions.degreesToRadians(45);
                Righthand.xRot = Functions.degreesToRadians(-20);
                Lefthand.xRot = Functions.degreesToRadians(-20);
                break;
        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}