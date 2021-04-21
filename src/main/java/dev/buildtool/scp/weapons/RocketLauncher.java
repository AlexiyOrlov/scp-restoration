package dev.buildtool.scp.weapons;

import dev.buildtool.scp.events.Entities;
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
        return null;
    }

    @Override
    public void shoot(World world, LivingEntity shooter, Hand hand, @Nullable LivingEntity target) {
        Rocket rocket= Entities.rocket.create(world);
        Vector3d lookAngles=shooter.getLookAngle();
        rocket.setPos(shooter.getX() - lookAngles.x, shooter.getEyeY(), shooter.getZ() - lookAngles.z);
        rocket.shootFromRotation(shooter,shooter.xRot,shooter.yRot,0,1,1);
        world.addFreshEntity(rocket);
    }
}
