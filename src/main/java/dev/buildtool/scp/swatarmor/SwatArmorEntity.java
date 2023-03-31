package dev.buildtool.scp.swatarmor;

import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.registration.SCPItems;
import dev.buildtool.scp.goals.BetterMeleeAttackGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.world.World;

@SCPObject(number = "912", name = "Autonomous SWAT Armor", classification = SCPObject.Classification.SAFE)
public class SwatArmorEntity extends SCPEntity {
    public SwatArmorEntity(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
        setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(SCPItems.policeBaton));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(4, new HurtByTargetGoal(this));
        goalSelector.addGoal(5, new BetterMeleeAttackGoal(this, 1, true, 10));
        targetSelector.addGoal(1, new TargetGoal());
    }

    private class TargetGoal extends NearestAttackableTargetGoal<LivingEntity> {
        public TargetGoal() {
            super(SwatArmorEntity.this, LivingEntity.class, 1, true, true, livingEntity -> {
                Item item = livingEntity.getMainHandItem().getItem();
                return item instanceof SwordItem || item instanceof AxeItem || item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem;
            });
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && this.targetConditions.test(this.mob, this.target) && super.canContinueToUse();
        }
    }
}
