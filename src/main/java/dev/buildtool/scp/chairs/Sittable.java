package dev.buildtool.scp.chairs;

import dev.buildtool.satako.InanimateEntity;
import dev.buildtool.scp.registration.Entities;
import net.minecraft.world.World;

public class Sittable extends InanimateEntity {
    public Sittable(World worldIn) {
        super(Entities.sittableEntityType, worldIn);
    }

//    @Override
//    public IPacket<?> createSpawnPacket() {
//        return new SSpawnObjectPacket(this, Entities.sittableEntityType, 0, getPosition());
//    }
//
//    @Override
//    public void dismount() {
//        super.dismount();
//        setDead();
//    }


}
