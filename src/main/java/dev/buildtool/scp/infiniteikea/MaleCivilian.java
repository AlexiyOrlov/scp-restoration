package dev.buildtool.scp.infiniteikea;

import dev.buildtool.scp.SCPEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class MaleCivilian extends IkeaCivilian {
    public MaleCivilian(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
    }
}
