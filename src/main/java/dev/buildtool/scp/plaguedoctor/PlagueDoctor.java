package dev.buildtool.scp.plaguedoctor;

import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.registration.Entities;
import dev.buildtool.scp.registration.SCPItems;
import dev.buildtool.scp.human.Human;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

@SCPObject(number = "049", classification = SCPObject.Classification.EUCLID, name = "Plague Doctor")
public class PlagueDoctor extends SCPEntity {
    public PlagueDoctor(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
        setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(SCPItems.scalpel));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0,new SwimGoal(this));
        goalSelector.addGoal(9, new MeleeAttackGoal(this, 1, true));
        goalSelector.addGoal(8, new HurtByTargetGoal(this));
        goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1));
        targetSelector.addGoal(12, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true, false));
        targetSelector.addGoal(13, new NearestAttackableTargetGoal<>(this, Human.class, true, false));
    }


    @Override
    public void awardKillScore(Entity killed, int scoreValue, DamageSource damageSource) {
        super.awardKillScore(killed, scoreValue, damageSource);
        if (killed instanceof PlayerEntity || killed instanceof Human) {
            Corpse corpse = Entities.corpseEntityType.create(level);
            corpse.setKilled(killed.getUUID());
            corpse.moveTo(killed.getX(), killed.getY(), killed.getZ(),killed.xRot,killed.yRot);
            level.addFreshEntity(corpse);
        }
    }
}

