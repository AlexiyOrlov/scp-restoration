package dev.buildtool.scp.items;

import dev.buildtool.satako.InanimateEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;

public class FlakShard extends ProjectileEntity {

    public FlakShard(EntityType<? extends ProjectileEntity> p_i231584_1_, World p_i231584_2_) {
        super(p_i231584_1_, p_i231584_2_);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new SSpawnObjectPacket(this, entity == null ? 0 : entity.getId());
    }
}
