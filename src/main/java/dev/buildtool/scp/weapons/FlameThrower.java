package dev.buildtool.scp.weapons;

import dev.buildtool.scp.events.Entities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FlameThrower extends Firearm {
    public FlameThrower(Properties properties, int cooldown) {
        super(properties, cooldown);
    }

    @Override
    public SoundEvent fireSound() {
        return SoundEvents.FIRE_AMBIENT;
    }

    @Override
    public void shoot(World world, LivingEntity shooter, Hand hand, @Nullable LivingEntity target) {
        Vector3d vector3d = shooter.getLookAngle();
        for (int i = 0; i < 12; i++) {
            Flame flame = Entities.flame.create(world);
            flame.setPos(shooter.getX() - vector3d.x, shooter.getEyeY() - 2 / 16d, shooter.getZ() - vector3d.z);
            flame.shootFromRotation(shooter, shooter.xRot, shooter.yRot + random.nextInt(32) - 16, 0, 2, 1);
            flame.setOwner(shooter);
            world.addFreshEntity(flame);
        }
    }
}
