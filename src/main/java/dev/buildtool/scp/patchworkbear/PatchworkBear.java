package dev.buildtool.scp.patchworkbear;

import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@SCPObject(name = "The Bear with a Heart of Patchwork", number = "2295", classification = SCPObject.Classification.SAFE)
public class PatchworkBear extends SCPEntity {
    public PatchworkBear(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }
}
