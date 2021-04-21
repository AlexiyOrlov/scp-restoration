package dev.buildtool.scp.weapons;

import net.minecraft.entity.EntityType;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnMobPacket;
import net.minecraft.world.World;

public class FlakShard extends Projectile {

    public FlakShard(EntityType<? extends Projectile> p_i231584_1_, World p_i231584_2_) {
        super(p_i231584_1_, p_i231584_2_,4,0.5);
        noCulling=true;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return new SSpawnMobPacket(this);
    }

    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return true;
    }
}
