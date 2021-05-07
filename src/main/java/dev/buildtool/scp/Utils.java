package dev.buildtool.scp;

import dev.buildtool.scp.human.Human;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Queued for {@link dev.buildtool.satako.Functions}
 */
public class Utils {

    public static BlockPos findEmptySpace(IWorld world, boolean down, BlockPos start, BlockPos... extra) {
        if (start.getY() < 2 || start.getY() > world.getHeight() - 1)
            return start;
        boolean allClear = true;
        for (BlockPos blockPos : extra) {
            if (!world.isEmptyBlock(start) || !world.isEmptyBlock(blockPos)) {
                allClear = false;
                break;
            }
        }
        if (allClear)
            return Arrays.stream(extra).min(Comparator.comparingInt(Vector3i::getY)).get();
        return findEmptySpace(world, down, down ? start.below() : start.above(), extra);
    }

    public static SCPWorldData getData(ServerWorld world) {
        return world.getDataStorage().computeIfAbsent(SCPWorldData::new, SCP.ID);
    }

    /**
     * Raytraces entities
     *
     * @param entity from
     * @return traced
     */
    public static Entity traceEntities(Entity entity) {
        float d = 100;
        Vector3d vec3d = entity.getEyePosition(1);
        Vector3d vec3d2 = entity.getViewVector(1.0F);
        Vector3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
        AxisAlignedBB box = entity.getBoundingBox().expandTowards(vec3d2.multiply(d, d, d)).expandTowards(1.0D, 1.0D, 1.0D);
        double e = d;
        e *= e;
        EntityRayTraceResult entityHitResult = rayTraceEntities(entity, vec3d, vec3d3, box, (entityx) -> !entityx.isSpectator() && entityx.canBeCollidedWith(), e);
        if (entityHitResult != null) {
            Entity hit = entityHitResult.getEntity();
            if (entity instanceof LivingEntity && ((LivingEntity) entity).canSee(hit)) {
                Vector3d vec3d4 = entityHitResult.getLocation();
                double g = vec3d.distanceToSqr(vec3d4);
                if (g < e) {
                    return hit;
                }
            }
        }
        return null;
    }

    /**
     * Copy of {@link ProjectileHelper#getEntityHitResult(Entity, Vector3d, Vector3d, AxisAlignedBB, Predicate, double)}
     */
    public static EntityRayTraceResult rayTraceEntities(Entity shooter, Vector3d startVec, Vector3d endVec, AxisAlignedBB boundingBox, Predicate<Entity> filter, double distance) {
        World world = shooter.level;
        double d0 = distance;
        Entity entity = null;
        Vector3d vector3d = null;

        for (Entity entity1 : world.getEntities(shooter, boundingBox, filter)) {
            AxisAlignedBB axisalignedbb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius());
            Optional<Vector3d> optional = axisalignedbb.clip(startVec, endVec);
            if (axisalignedbb.contains(startVec)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    vector3d = optional.orElse(startVec);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vector3d vector3d1 = optional.get();
                double d1 = startVec.distanceToSqr(vector3d1);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getRootVehicle() == shooter.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            vector3d = vector3d1;
                        }
                    } else {
                        entity = entity1;
                        vector3d = vector3d1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity == null ? null : new EntityRayTraceResult(entity, vector3d);
    }

    /**
     * @return list of scp annotations
     */
    public static List<Map<String, Object>> getAllSCPs() {
        Optional<ModFileScanData> optional = ModList.get().getAllScanData().stream().filter(modFileScanData -> modFileScanData.getIModInfoData().get(0).getMods().get(0).getModId().equals(dev.buildtool.scp.SCP.ID)).findFirst();
        if (optional.isPresent()) {
            ModFileScanData modFileScanData = optional.get();
            List<ModFileScanData.AnnotationData> annotationData = modFileScanData.getAnnotations().stream().filter(data -> data.getAnnotationType().getClassName().equals(SCPObject.class.getName())).collect(Collectors.toList());
            List<Map<String, Object>> list = new ArrayList<>(annotationData.size());
            annotationData.forEach(annotationData1 -> list.add(annotationData1.getAnnotationData()));
            return list;
        }
        throw new IllegalStateException("No SCP annotations found");
    }

    /**
     * Checks whether an entity is ally to given human
     *
     * @param livingEntity checked
     */
    public static boolean isAlly(LivingEntity livingEntity, Human to) {
        if (livingEntity instanceof Human) {
            if (to.hasOwner()) {
                return to.getOwner().equals(((Human) livingEntity).getOwner());
            }
        }
        if (livingEntity instanceof TameableEntity) {
            if (to.hasOwner()) {
                TameableEntity tameableEntity = (TameableEntity) livingEntity;
                return to.getOwner().equals(tameableEntity.getOwnerUUID());
            }
        }
        return false;
    }

    /**
     * From {@link World#setBlock(BlockPos, BlockState, int)}
     */
    public static boolean setBlockstateSilently(World world, BlockState blockState, BlockPos target) {
        if (World.isOutsideBuildHeight(target)) {
            return false;
        } else if (!world.isClientSide && world.isDebug()) {
            return false;
        } else {
            int flag = 2;
            Chunk chunk = world.getChunkAt(target);

            target = target.immutable(); // prevent mutable BlockPos leaks
            BlockSnapshot blockSnapshot = null;
            if (world.captureBlockSnapshots && !world.isClientSide) {
                blockSnapshot = BlockSnapshot.create(world.dimension(), world, target, flag);
                world.capturedBlockSnapshots.add(blockSnapshot);
            }

            BlockState old = world.getBlockState(target);
            int oldLight = old.getLightValue(world, target);
            int oldOpacity = old.getLightBlock(world, target);

            BlockState blockstateChunk = setBlockstate(chunk, target, blockState, false);//chunk.setBlockState(target, blockState, (flag & 64) != 0);
            if (blockstateChunk == null) {
                if (blockSnapshot != null) world.capturedBlockSnapshots.remove(blockSnapshot);
                return false;
            } else {
                BlockState blockstate1 = world.getBlockState(target);
                if (blockstate1 != blockstateChunk && (blockstate1.getLightBlock(world, target) != oldOpacity || blockstate1.getLightValue(world, target) != oldLight || blockstate1.useShapeForLightOcclusion() || blockstateChunk.useShapeForLightOcclusion())) {
                    world.getProfiler().push("queueCheckLight");
                    world.getChunkSource().getLightEngine().checkBlock(target);
                    world.getProfiler().pop();
                }

                if (blockSnapshot == null) {

//                  world.markAndNotifyBlock(target, chunk, blockstateChunk, blockState, flag, flag);
                    Block block = blockState.getBlock();
                    BlockState blockstateWorld = world.getBlockState(target);
                    {
                        {
                            int p_241211_4_ = 1;
                            if (blockstateWorld == blockState) {
                                if (blockstateChunk != blockstateWorld) {
                                    world.setBlocksDirty(target, blockstateChunk, blockstateWorld);
                                }

                                if (world.isClientSide || chunk.getFullStatus().isOrAfter(ChunkHolder.LocationType.TICKING)) {
                                    world.sendBlockUpdated(target, blockstateChunk, blockState, flag);
                                }

                                int i = flag & -34;
                                blockstateChunk.updateIndirectNeighbourShapes(world, target, i, 0);
                                blockState.updateIndirectNeighbourShapes(world, target, i, 0);

                                world.onBlockStateChange(target, blockstateChunk, blockstateWorld);
                            }
                        }
                    }
                }
                return true;
            }
        }
    }

    /**
     * From {@link Chunk#setBlockState(BlockPos, BlockState, boolean)}
     */
    public static BlockState setBlockstate(Chunk chunk, BlockPos target, BlockState blockState, boolean p_177436_3_) {
        int i = target.getX() & 15;
        int j = target.getY();
        int k = target.getZ() & 15;
        ChunkSection chunksection = chunk.getSections()[j >> 4];
        if (chunksection == Chunk.EMPTY_SECTION) {
            if (blockState.isAir()) {
                return null;
            }

            chunksection = new ChunkSection(j >> 4 << 4);
            chunk.getSections()[j >> 4] = chunksection;
        }

        boolean flag = chunksection.isEmpty();
        BlockState blockstate = chunksection.setBlockState(i, j & 15, k, blockState);
        if (blockstate == blockState) {
            return null;
        } else {
            Block block = blockState.getBlock();
            Block block1 = blockstate.getBlock();
            //TODO?
//            chunk.heightmaps.get(Heightmap.Type.MOTION_BLOCKING).update(i, j, k, blockState);
//            chunk.heightmaps.get(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).update(i, j, k, blockState);
//            chunk.heightmaps.get(Heightmap.Type.OCEAN_FLOOR).update(i, j, k, blockState);
//            chunk.heightmaps.get(Heightmap.Type.WORLD_SURFACE).update(i, j, k, blockState);
            boolean flag1 = chunksection.isEmpty();
            if (flag != flag1) {
                chunk.getLevel().getChunkSource().getLightEngine().updateSectionStatus(target, flag1);
            }

            if (!chunk.getLevel().isClientSide) {
                blockstate.onRemove(chunk.getLevel(), target, blockState, p_177436_3_);
            } else if ((block1 != block || !blockState.hasTileEntity()) && blockstate.hasTileEntity()) {
                chunk.getLevel().removeBlockEntity(target);
            }

            if (!chunksection.getBlockState(i, j & 15, k).is(block)) {
                return null;
            } else {
                if (blockstate.hasTileEntity()) {
                    TileEntity tileentity = chunk.getBlockEntity(target, Chunk.CreateEntityType.CHECK);
                    if (tileentity != null) {
                        tileentity.clearCache();
                    }
                }

                if (blockState.hasTileEntity()) {
                    TileEntity tileentity1 = chunk.getBlockEntity(target, Chunk.CreateEntityType.CHECK);
                    if (tileentity1 == null) {
                        tileentity1 = blockState.createTileEntity(chunk.getLevel());
                        chunk.getLevel().setBlockEntity(target, tileentity1);
                    } else {
                        tileentity1.clearCache();
                    }
                }

                chunk.setUnsaved(true);
                return blockstate;
            }
        }
    }
}
