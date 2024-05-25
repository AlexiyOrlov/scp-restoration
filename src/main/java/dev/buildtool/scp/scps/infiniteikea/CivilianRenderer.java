package dev.buildtool.scp.scps.infiniteikea;

import dev.buildtool.scp.ArmoredRenderer;
import dev.buildtool.scp.BipedModel2;
import dev.buildtool.scp.SCP;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class CivilianRenderer extends ArmoredRenderer<IkeaCivilian, BipedModel2<IkeaCivilian>> {
    protected ResourceLocation[] skinArray;

    public CivilianRenderer(EntityRendererManager renderManagerIn, BipedModel2<IkeaCivilian> modelBipedIn, float shadowSize, String... textureNames) {
        super(renderManagerIn, modelBipedIn, textureNames[0], shadowSize);
        skinArray = Arrays.stream(textureNames).map(s -> new ResourceLocation(SCP.ID, "textures/entity/" + s + ".png")).distinct().toArray(ResourceLocation[]::new);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull IkeaCivilian entity) {
        return skinArray[entity.getVariant()];
    }
}
