package dev.buildtool.scp.weapons;

import dev.buildtool.satako.Functions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FlameThrower extends Firearm {
    public FlameThrower(Properties properties, int cooldown) {
        super(properties, cooldown);
    }

    @Override
    public SoundEvent fireSound() {
        return null;
    }

    @Override
    public void shoot(World world, LivingEntity shooter, Hand hand, @Nullable LivingEntity target) {
        for (LivingEntity livingentity : shooter.level.getEntitiesOfClass(LivingEntity.class, shooter.getBoundingBox().inflate(5.0D, 0.25D, 5.0D))) {
            if (livingentity != shooter && Functions.isInSightOf(livingentity, shooter, 45f) && !shooter.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity) livingentity).isMarker()) && shooter.distanceToSqr(livingentity) < 25.0D) {
                livingentity.knockback(0.4F, MathHelper.sin(shooter.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(shooter.yRot * ((float) Math.PI / 180F)));
                livingentity.hurt(DamageSource.IN_FIRE, 2);
            }
        }
        shooter.level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.FIRE_AMBIENT, shooter.getSoundSource(), 1.0F, 1.0F);
//        double d0 = -MathHelper.sin(shooter.yRot * ((float)Math.PI / 180F));
//        double d1 = MathHelper.cos(shooter.yRot * ((float)Math.PI / 180F));
//        if (shooter.level instanceof ServerWorld) {
//            {
//                ServerWorld serverWorld= (ServerWorld) shooter.level;
//                double signx=Math.signum(d0);
//                double signz=Math.signum(d1);
//                //TODO
//                for (int i = 0; i < 20; i++) {
//                    serverWorld.sendParticles(ParticleTypes.FLAME, shooter.getX() + d0, shooter.getY(0.5D), shooter.getZ() + d1, 0, signx*random.nextInt(5),0, signz*random.nextInt(5), random.nextDouble());
//                }
//            }
//        }
    }
}
