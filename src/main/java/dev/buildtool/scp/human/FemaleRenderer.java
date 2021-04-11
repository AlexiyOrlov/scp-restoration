package dev.buildtool.scp.human;

import dev.buildtool.scp.ArmoredRenderer;
import dev.buildtool.scp.BipedModel2;
import dev.buildtool.scp.SCP;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class FemaleRenderer extends ArmoredRenderer<FemaleCommoner, BipedModel2<FemaleCommoner>> {
    private ResourceLocation[] skins;

    public FemaleRenderer(EntityRendererManager renderManagerIn, BipedModel2<FemaleCommoner> modelBipedIn, float shadowSize, String... textureNames) {
        super(renderManagerIn, modelBipedIn, textureNames[0], shadowSize);
        skins = Arrays.stream(textureNames).map(s -> new ResourceLocation(SCP.ID, "textures/entity/" + s + ".png")).distinct().toArray(ResourceLocation[]::new);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull FemaleCommoner entity) {
        return skins[entity.getVariant()];
    }
}
