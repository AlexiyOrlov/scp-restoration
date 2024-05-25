package dev.buildtool.scp.scps.infiniteikea;

import dev.buildtool.scp.SCPObject;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

@SCPObject(number = "3008", classification = SCPObject.Classification.EUCLID, name = "A Perfectly Normal, Regular Old IKEA")
public class EntranceBlock extends TeleportBlock {
    public EntranceBlock(Properties properties) {
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
            if (entityIn.isCrouching() || !(entityIn instanceof PlayerEntity)) {
                teleportBlockEntity.cooldown++;
            }
            if (teleportBlockEntity.cooldown > 60) {
                teleportBlockEntity.cooldown = 0;
                RegistryKey<World> dimension = worldIn.dimension();
                //going to Ikea
                if (dimension == World.OVERWORLD) {
                    ServerWorld ikea = ((ServerWorld) worldIn).getServer().getLevel(ModEvents.ikeaDimension);
                    entityIn.changeDimension(ikea, new ITeleporter() {
                        @Override
                        public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                            return repositionEntity.apply(false);
                        }

                        @Nonnull
                        @Override
                        public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
                            BlockPos exit = entity instanceof PlayerEntity ? ((PlayerEntity) entity).getSleepingPos().orElseGet(() -> new BlockPos(1, 6, 1)) : new BlockPos(1, 6, 1);
                            return new PortalInfo(Vector3d.atCenterOf(exit), Vector3d.ZERO, entity.xRot, entity.yRot);
                        }
                    });
                }
            }
        }
    }
}
