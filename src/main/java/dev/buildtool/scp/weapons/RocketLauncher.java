package dev.buildtool.scp.weapons;

import dev.buildtool.scp.registration.Entities;
import dev.buildtool.scp.registration.Sounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RocketLauncher extends Firearm{
    public RocketLauncher(Properties properties, int cooldown) {
        super(properties, cooldown);
    }

    @Override
    public SoundEvent fireSound() {
        return Sounds.rocketLaunch;
    }

    @Override
    public void shoot(World world, LivingEntity shooter, Hand hand, @Nullable LivingEntity target) {
        Rocket rocket = Entities.rocket.create(world);
        Vector3d lookAngles = shooter.getLookAngle();
        rocket.setOwner(shooter);
        rocket.setPos(shooter.getX() - lookAngles.x, shooter.getEyeY(), shooter.getZ() - lookAngles.z);
        //5th parameter is velocity
        rocket.shootFromRotation(shooter, shooter.xRot, shooter.yRot, 0, 3, 1);
        world.addFreshEntity(rocket);
    }
}
