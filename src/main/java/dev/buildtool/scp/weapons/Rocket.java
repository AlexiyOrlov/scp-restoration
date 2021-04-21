package dev.buildtool.scp.weapons;

import dev.buildtool.scp.human.Human;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class Rocket extends Projectile {
    /**
     * @param p_i231584_1_
     * @param p_i231584_2_
     * @param damage_
     * @param lightness    0 to 1
     */
    public Rocket(EntityType<? extends Projectile> p_i231584_1_, World p_i231584_2_, int damage_, double lightness) {
        super(p_i231584_1_, p_i231584_2_, damage_, lightness);
    }

    void explode()
    {
        level.explode(getOwner(),getX(),getY(),getZ(),3, Explosion.Mode.BREAK);
        remove();
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
