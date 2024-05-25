package dev.buildtool.scp.scps.infiniteikea;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.scp.SCPWorldData;
import dev.buildtool.scp.Utils;
import dev.buildtool.scp.events.ModEvents;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TeleportBlockEntity extends BlockEntity2 {
    short cooldown;
    public TeleportBlockEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void onLoad() {
        if (level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) level;
            RegistryKey<World> dimension = level.dimension();
            if (dimension == World.OVERWORLD) {
                SCPWorldData worldData = Utils.getData(serverWorld);
                worldData.ikeaTeleporters.add(worldPosition);
                worldData.setDirty(true);
            } else if (dimension == ModEvents.ikeaDimension) {
                SCPWorldData worldData = Utils.getData(serverWorld);
                worldData.ikeaTeleporters.add(worldPosition);
                worldData.setDirty(true);
            }
        }
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) level;
            SCPWorldData worldData = Utils.getData(serverWorld);
            worldData.ikeaTeleporters.remove(getBlockPos());
            worldData.setDirty(true);
        }
        super.setRemoved();
    }
}
