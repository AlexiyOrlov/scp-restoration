package dev.buildtool.scp.scps.plaguedoctor;

import dev.buildtool.scp.BipedModel2;
import dev.buildtool.scp.SCP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;
import java.util.UUID;

public class CorpseRenderer extends LivingRenderer<Corpse, BipedModel2<Corpse>> {

    ResourceLocation resourceLocation;

    public CorpseRenderer(EntityRendererManager manager) {
        super(manager, new BipedModel2<>(), 0.4f);
        resourceLocation = new ResourceLocation(SCP.ID, "");
    }

    @Override
    public ResourceLocation getTextureLocation(Corpse entity) {
        Optional<UUID> uuid = entity.getKilled();
        if (uuid.isPresent()) {
            ClientPlayNetHandler netHandler = Minecraft.getInstance().getConnection();
            NetworkPlayerInfo playerInfo = netHandler.getPlayerInfo(uuid.get());
            return playerInfo != null ? playerInfo.getSkinLocation() : DefaultPlayerSkin.getDefaultSkin(uuid.get());
        }
        return resourceLocation;
    }

    @Override
    protected boolean shouldShowName(Corpse entity) {
        return false;
    }
}
