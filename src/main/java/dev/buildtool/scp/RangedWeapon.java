package dev.buildtool.scp;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface RangedWeapon {

    /**
     * Shoot sound
     */
    SoundEvent fireSound();

    /**
     * @return volume of shoot sound
     */
    default float soundVolume() {
        return 0.5f;
    }

    /**
     * @param target used by mobs
     */
    void shoot(World world, LivingEntity shooter, Hand hand,@Nullable LivingEntity target);

    /**
     * @return time between shots
     */
    default int cooldown() {
        return 10;
    }
}
