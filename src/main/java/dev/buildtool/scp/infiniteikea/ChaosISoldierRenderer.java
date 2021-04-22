package dev.buildtool.scp.infiniteikea;

import dev.buildtool.scp.ArmoredRenderer;
import dev.buildtool.scp.BipedModel2;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.human.ChaosInsurgencySoldier;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ChaosISoldierRenderer extends ArmoredRenderer<ChaosInsurgencySoldier, BipedModel2<ChaosInsurgencySoldier>> {
    protected ResourceLocation[] skinArray;

    public ChaosISoldierRenderer(EntityRendererManager renderManagerIn, BipedModel2<ChaosInsurgencySoldier> modelBipedIn, float shadowSize, String... textureNames) {
        super(renderManagerIn, modelBipedIn, textureNames[0], shadowSize);
        skinArray = Arrays.stream(textureNames).map(s -> new ResourceLocation(SCP.ID, "textures/entity/" + s + ".png")).distinct().toArray(ResourceLocation[]::new);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull ChaosInsurgencySoldier entity) {
        return skinArray[entity.getVariant()];
    }
}
