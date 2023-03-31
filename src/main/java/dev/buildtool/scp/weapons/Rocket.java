package dev.buildtool.scp.weapons;

import dev.buildtool.scp.human.Human;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class Rocket extends Projectile2 {
    public Rocket(EntityType<? extends DamagingProjectileEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public Rocket(EntityType<? extends DamagingProjectileEntity> p_i50174_1_, double p_i50174_2_, double p_i50174_4_, double p_i50174_6_, double p_i50174_8_, double p_i50174_10_, double p_i50174_12_, World p_i50174_14_) {
        super(p_i50174_1_, p_i50174_2_, p_i50174_4_, p_i50174_6_, p_i50174_8_, p_i50174_10_, p_i50174_12_, p_i50174_14_);
    }

    public Rocket(EntityType<? extends DamagingProjectileEntity> p_i50175_1_, LivingEntity p_i50175_2_, double p_i50175_3_, double p_i50175_5_, double p_i50175_7_, World p_i50175_9_) {
        super(p_i50175_1_, p_i50175_2_, p_i50175_3_, p_i50175_5_, p_i50175_7_, p_i50175_9_);
    }
//    /**
//     * @param lightness    0 to 1
//     */
//    public Rocket(EntityType<? extends Projectile> p_i231584_1_, World p_i231584_2_, int damage_, double lightness) {
//        super(p_i231584_1_, p_i231584_2_, damage_, lightness, 0);
//    }

    void explode() {
        if (!level.isClientSide) {
            level.explode(getOwner(), getX(), getY(), getZ(), 3, Explosion.Mode.BREAK);
            remove();
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
        Entity entity= entityRayTraceResult.getEntity();
        if(entity instanceof PlayerEntity)
        {
            PlayerEntity playerEntity= (PlayerEntity) entity;
            if(getOwner()!=null)
            {
                if(getOwner() instanceof Human)
                {
                    Human human= (Human) getOwner();
                    if(human.getOwner()!=null && human.getOwner().equals(playerEntity.getUUID()))
                        remove();
                    else
                        explode();
                }
                else if(getOwner() instanceof PlayerEntity)
                {
                    PlayerEntity playerEntity1= (PlayerEntity) getOwner();
                    if(playerEntity1!=playerEntity)
                    {
                        explode();
                    }
                }
                else
                    explode();
            }
            else
                explode();
        }
        else explode();
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        explode();
    }
}
