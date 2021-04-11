package dev.buildtool.scp;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

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
        return world.getDataStorage().get(SCPWorldData::new, SCP.ID);
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
}
