package dev.buildtool.scp.scps.infiniteikea;

import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.SCPWorldData;
import dev.buildtool.scp.Utils;
import dev.buildtool.scp.events.ModEvents;
import dev.buildtool.scp.registration.SCPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

@SCPObject(number = "3008", classification = SCPObject.Classification.EUCLID, name = "A Perfectly Normal, Regular Old IKEA")
public class ExitBlock extends TeleportBlock {

    public ExitBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.ikeaTeleporter.create();
    }

    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn instanceof ServerWorld) {
            TeleportBlockEntity teleportBlockEntity = (TeleportBlockEntity) worldIn.getBlockEntity(pos);
            teleportBlockEntity.cooldown++;
            if (teleportBlockEntity.cooldown > 40) {
                teleportBlockEntity.cooldown = 0;
                RegistryKey<World> dimension = worldIn.dimension();

                if (dimension == ModEvents.ikeaDimension) {
                    //returning from Ikea
                    ServerWorld overworld = worldIn.getServer().getLevel(World.OVERWORLD);
                    entityIn.changeDimension(overworld, new ITeleporter() {
                        /**
                         * Called after {@link #getPortalInfo(Entity, ServerWorld, Function)}
                         */
                        @Override
                        public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                            return repositionEntity.apply(false);
                        }

                        @Nonnull
                        @Override
                        public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
                            BlockPos exit;
                            SCPWorldData scpWorldData = Utils.getData(destWorld);
                            entity.xRot += 180;
                            if (scpWorldData.ikeaTeleporters.stream().findAny().isPresent())
                                exit = scpWorldData.ikeaTeleporters.get(0);
                            else
                                exit = destWorld.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, entity instanceof PlayerEntity ? ((PlayerEntity) entity).getSleepingPos().orElseGet(destWorld::getSharedSpawnPos): destWorld.getSharedSpawnPos());

                            return new PortalInfo(Vector3d.atCenterOf(exit), Vector3d.ZERO, entity.xRot, entity.yRot);
                        }
                    });
                }
            }
        }
    }
}
