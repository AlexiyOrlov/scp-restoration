package dev.buildtool.scp.patchworkbear;

import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.world.World;

@SCPObject(name = "The Bear with a Heart of Patchwork", number = "2295", classification = SCPObject.Classification.SAFE)
public class PatchworkBear extends SCPEntity {
    public PatchworkBear(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(3, new LookRandomlyGoal(this));
        goalSelector.addGoal(4, new LookAtGoal(this, LivingEntity.class, 16));
    }
}
