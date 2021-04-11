package dev.buildtool.scp.infiniteikea;

import dev.buildtool.satako.RandomizedList;
import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * SCP-3008-2
 */
@SCPObject(number = "3008", classification = SCPObject.Classification.EUCLID, name = "A Perfectly Normal, Regular Old IKEA")
public class IkeaMonster extends SCPEntity {
    public IkeaMonster(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    private static final RandomizedList<String> warnings = new RandomizedList<>(Arrays.asList("scp.you.should.leave", "scp.leave.the.store", "scp.all.customers.should.leave"));
    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(4, new OpenDoorGoal(this, false));
        goalSelector.addGoal(5, new RandomWalkingGoal(this, 1));
        goalSelector.addGoal(6, new HurtByTargetGoal(this));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1, false));
        targetSelector.addGoal(5, new TargetGoal(this, PlayerEntity.class, false));
        targetSelector.addGoal(6, new TargetGoal(this, MobEntity.class, false));
    }

    @Override
    public void setTarget(@Nullable LivingEntity entitylivingbaseIn) {
        super.setTarget(entitylivingbaseIn);
        if (entitylivingbaseIn instanceof PlayerEntity && level.isClientSide && tickCount % 100 == 0) {
            entitylivingbaseIn.sendMessage(new TranslationTextComponent(warnings.getRandom()), getUUID());
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return true;
    }

    class TargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public TargetGoal(MobEntity goalOwnerIn, Class<? extends LivingEntity> targetClass, boolean nearbyOnlyIn) {
            super(goalOwnerIn, (Class<LivingEntity>) targetClass, 1, true, nearbyOnlyIn, livingEntity -> livingEntity.attackable() && livingEntity.getClass() != IkeaMonster.class);
        }

        @Override
        public boolean canUse() {
            return level.isNight() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return level.isNight() && super.canContinueToUse();
        }
    }
}
