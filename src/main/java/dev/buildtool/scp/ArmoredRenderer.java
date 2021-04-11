package dev.buildtool.scp;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Renders armor, elytra and held items
 *
 * @param <T>
 * @param <M>
 */
public class ArmoredRenderer<T extends MobEntity, M extends BipedModel<T>> extends BipedRenderer<T, M> {
    private final ResourceLocation texture;

    public ArmoredRenderer(EntityRendererManager renderManagerIn, M modelBipedIn, String textureName, float shadowSize) {
        super(renderManagerIn, modelBipedIn, shadowSize);
        texture = new ResourceLocation(SCP.ID, "textures/entity/" + textureName + ".png");
        addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5f), new BipedModel<>(1f)));
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull T entity) {
        return texture;
    }
}
