package dev.buildtool.scp.capability;

import dev.buildtool.scp.SCP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class CapabilityController {

    @SubscribeEvent
    public static void addCapability(AttachCapabilitiesEvent<Entity> attachCapabilitiesEvent) {
        Entity entity = attachCapabilitiesEvent.getObject();
        if (entity instanceof PlayerEntity) {
            attachCapabilitiesEvent.addCapability(new ResourceLocation(SCP.ID, "knowledge"), new SCPKnowledge.Provider());
            attachCapabilitiesEvent.addCapability(new ResourceLocation(SCP.ID, "thrown_items"), new ThrownItems.Provider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone cloneEvent) {
        PlayerEntity previous = cloneEvent.getOriginal();
        PlayerEntity newPlayer = cloneEvent.getPlayer();
        newPlayer.getCapability(SCPKnowledge.KNOWLEDGE).ifPresent(knowledge -> {
            previous.getCapability(SCPKnowledge.KNOWLEDGE).ifPresent(knowledge1 -> {
                knowledge.knownSCPData().putAll(knowledge1.knownSCPData());
            });
        });
        newPlayer.getCapability(ThrownItems.THROWNITEMS).ifPresent(thrownItemMemory -> {
            previous.getCapability(ThrownItems.THROWNITEMS).ifPresent(thrownItemMemory1 -> thrownItemMemory.thrownItems().addAll(thrownItemMemory1.thrownItems()));
        });
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent deathEvent) {
        PlayerEntity livingEntity = deathEvent.getPlayer();
        if (livingEntity != null) {
            syncKnowledge(livingEntity.level, livingEntity);
        }
    }

    public static SCPKnowledge.Knowledge getKnowledge(PlayerEntity player) {
        return player.getCapability(SCPKnowledge.KNOWLEDGE).orElseThrow(() -> new IllegalStateException("SCP knowledge is absent"));
    }

    public static void syncKnowledge(IWorldReader world, PlayerEntity playerEntity) {
        if (!world.isClientSide()) {
            SCP.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerEntity),new Packet(getKnowledge(playerEntity)));
        }
    }

}
