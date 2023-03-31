package dev.buildtool.scp;

import dev.buildtool.scp.capability.Packet;
import dev.buildtool.scp.capability.SCPKnowledge;
import dev.buildtool.scp.harddrivecracker.CrackingProgress;
import dev.buildtool.scp.harddrivecracker.HardDriveCrackerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * Client packet handler
 */
public class ClientProxy {
    public void synchronizeKnowledge(Packet msg, Supplier<NetworkEvent.Context> contextSupplier) {
            PlayerEntity playerEntity = Minecraft.getInstance().player;
            playerEntity.getCapability(SCPKnowledge.KNOWLEDGE).ifPresent(knowledge -> knowledge.knownSCPData().putAll(msg.knowledge.knownSCPData()));
            contextSupplier.get().setPacketHandled(true);
    }

    public void synchronizeCrackerTime(CrackingProgress crackingProgress, NetworkEvent.Context context){
        ClientWorld level=Minecraft.getInstance().level;
        TileEntity tileEntity=level.getBlockEntity(crackingProgress.pos);
        if(tileEntity instanceof HardDriveCrackerEntity){
            HardDriveCrackerEntity crackerEntity= (HardDriveCrackerEntity) tileEntity;
            crackerEntity.time=crackingProgress.timeLeft;
            context.setPacketHandled(true);
        }
    }
}
