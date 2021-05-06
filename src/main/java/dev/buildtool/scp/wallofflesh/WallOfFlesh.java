package dev.buildtool.scp.wallofflesh;

import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.human.Human;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@SCPObject(number = "2059", name = "Wall of Flesh", classification = SCPObject.Classification.EUCLID)
public class WallOfFlesh extends SCPEntity {
    public WallOfFlesh(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true, false));
        targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Human.class, true, false));
        targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AgeableEntity.class, true, false));
        targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, WaterMobEntity.class, true, false));
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(3, new MeleeAttackGoal(this, 1, false));
    }


    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean hurt = super.doHurtTarget(entityIn);
        if (entityIn instanceof LivingEntity) {
            if (!entityIn.isAlive()) {
                heal(((LivingEntity) entityIn).getMaxHealth());
            }
        }
        return hurt;
    }
}
