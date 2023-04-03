package dev.buildtool.scp.chairs;

import dev.buildtool.satako.InanimateEntity;
import dev.buildtool.scp.registration.Entities;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class Sittable extends InanimateEntity {
    public Sittable(World worldIn) {
        super(Entities.sittableEntityType, worldIn);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (getPassengers().isEmpty())
            remove();
    }

    @Override
    public double getPassengersRidingOffset() {
        return -0.75;
    }
}
