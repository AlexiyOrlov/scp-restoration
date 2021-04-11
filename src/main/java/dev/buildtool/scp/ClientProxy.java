package dev.buildtool.scp;

import dev.buildtool.scp.capability.Packet;
import dev.buildtool.scp.capability.SCPKnowledge;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Client packet handler
 *
 * @param <MSG> packet
 */
public class ClientProxy<MSG> implements BiConsumer<MSG, Supplier<NetworkEvent.Context>> {
    @Override
    public void accept(MSG msg, Supplier<NetworkEvent.Context> contextSupplier) {
        if (msg instanceof Packet) {
            Packet packet = (Packet) msg;
            PlayerEntity playerEntity = Minecraft.getInstance().player;
            playerEntity.getCapability(SCPKnowledge.KNOWLEDGE).ifPresent(knowledge -> knowledge.knownSCPData().putAll(packet.knowledge.knownSCPData()));
            contextSupplier.get().setPacketHandled(true);
        }
    }
}
