package dev.buildtool.scp.weapons;

import dev.buildtool.scp.Utils;
import dev.buildtool.scp.events.Sounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class AutoRifle extends Firearm {
    public AutoRifle(Properties properties, int cooldown) {
        super(properties, cooldown);
    }

    @Override
    public SoundEvent fireSound() {
        return Sounds.rifleShot;
    }

    @Override
    public void shoot(World world, LivingEntity shooter, Hand hand, LivingEntity target) {
        Entity entity = Utils.traceEntities(shooter);
        if (entity != null) {
            entity.hurt(DamageSource.mobAttack(shooter), 2);
            entity.invulnerableTime = 0;
        }
    }
}
