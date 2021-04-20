package dev.buildtool.scp.items;

import dev.buildtool.scp.events.Sounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
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

    }
}
