package dev.buildtool.scp.human;

import dev.buildtool.scp.ArmoredRenderer;
import dev.buildtool.scp.BipedModel2;
import dev.buildtool.scp.SCP;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class MaleRenderer extends ArmoredRenderer<MaleCommoner, BipedModel2<MaleCommoner>> {
    private final ResourceLocation[] skins;

    public MaleRenderer(EntityRendererManager renderManagerIn, BipedModel2<MaleCommoner> modelBipedIn, float shadowSize, String... textureNames) {
        super(renderManagerIn, modelBipedIn, textureNames[0], shadowSize);
        skins = Arrays.stream(textureNames).map(s -> new ResourceLocation(SCP.ID, "textures/entity/" + s + ".png")).distinct().toArray(ResourceLocation[]::new);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull MaleCommoner entity) {

        return skins[entity.getVariant()];
    }
}
