package dev.buildtool.scp.weapons;

import dev.buildtool.scp.registration.Entities;
import dev.buildtool.scp.registration.Sounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class FlakCannon extends Firearm{
    public FlakCannon(Properties properties, int cooldown) {
        super(properties, cooldown);
    }

    @Override
    public SoundEvent fireSound() {
        return Sounds.flakCannonFire;
    }

    @Override
    public void shoot(World world, LivingEntity shooter, Hand hand, LivingEntity target) {
        Vector3d vector3d = shooter.getLookAngle();
        for (int i = 0; i < 6; i++) {
            FlakShard flakShard = Entities.flakShard.create(world);
            flakShard.setPos(shooter.getX() - vector3d.x, shooter.getEyeY(), shooter.getZ() - vector3d.z);
            flakShard.shootFromRotation(shooter, shooter.xRot + random.nextInt(8) - 10, shooter.yRot + random.nextInt(16) - 8, 0, 2, 1);
            flakShard.setOwner(shooter);
            world.addFreshEntity(flakShard);
        }
    }
}
