package dev.buildtool.scp.patchworkbear;

import com.google.common.collect.Sets;
import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.goals.SeekItems;
import dev.buildtool.scp.human.Human;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

@SCPObject(name = "The Bear with a Heart of Patchwork", number = "2295", classification = SCPObject.Classification.SAFE)
public class PatchworkBear extends SCPEntity {
    final static DataParameter<Boolean> HEALING = EntityDataManager.defineId(PatchworkBear.class, DataSerializers.BOOLEAN);

    public PatchworkBear(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack candidate, ItemStack current) {
        if (current.isEmpty() && (candidate.getItem() == Items.STRING || candidate.getItem().is(ItemTags.WOOL))) {
            return true;
        }
        return ItemStack.isSame(candidate, current) && current.getCount() < current.getMaxStackSize();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(3, new LookRandomlyGoal(this));
        goalSelector.addGoal(4, new LookAtGoal(this, LivingEntity.class, 16));
        goalSelector.addGoal(5, new HealHumanGoal());
        goalSelector.addGoal(6, new SeekItems(this, Sets.newHashSet(Items.STRING), Sets.newHashSet(ItemTags.WOOL)));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(HEALING, false);
    }

    private void setHealing(boolean b) {
        entityData.set(HEALING, b);
    }

    public boolean isHealing() {
        return entityData.get(HEALING);
    }

    private class HealHumanGoal extends Goal {
        private LivingEntity target;

        @Override
        public boolean canUse() {
            double sense = getRange();
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPosition()).inflate(sense);
            List<PlayerEntity> playerEntities = level.getEntitiesOfClass(PlayerEntity.class, axisAlignedBB, playerEntity -> playerEntity.getHealth() < playerEntity.getMaxHealth() * 0.7);
            List<Human> humans = level.getEntitiesOfClass(Human.class, axisAlignedBB, human -> human.getHealth() < human.getMaxHealth() * 0.7);
            return (!getMainHandItem().isEmpty() || !getOffhandItem().isEmpty()) && (!humans.isEmpty() || !playerEntities.isEmpty());
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && target.getHealth() < target.getMaxHealth() && (!getOffhandItem().isEmpty() || !getMainHandItem().isEmpty());
        }

        @Override
        public void tick() {
            super.tick();
            double sense = getRange();
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPosition()).inflate(sense);
            List<PlayerEntity> playerEntities = level.getEntitiesOfClass(PlayerEntity.class, axisAlignedBB, playerEntity -> playerEntity.getHealth() < playerEntity.getMaxHealth());
            if (!playerEntities.isEmpty()) {
                playerEntities.stream().reduce((playerEntity, playerEntity2) -> distanceToSqr(playerEntity) < distanceToSqr(playerEntity2) ? playerEntity : playerEntity2).ifPresent(playerEntity -> target = playerEntity);
            }
            if (target == null) {
                List<Human> humans = level.getEntitiesOfClass(Human.class, axisAlignedBB, human -> human.getHealth() < human.getMaxHealth());
                if (!humans.isEmpty()) {
                    humans.stream().reduce((human, human2) -> distanceToSqr(human) < distanceToSqr(human2) ? human : human2).ifPresent(human -> target = human);
                }
            }
            if (target != null) {
                if (distanceToSqr(target) > 2) {
                    getNavigation().moveTo(target, 1);
                } else {
                    getNavigation().stop();
                    target.heal(0.5f);
                    if (!getMainHandItem().isEmpty()) {
                        getMainHandItem().shrink(1);
                    } else if (!getOffhandItem().isEmpty()) {
                        getOffhandItem().shrink(1);
                    }
                    setHealing(true);
                }
            } else
                setHealing(false);
        }

        @Override
        public void stop() {
            super.stop();
            target = null;
            setHealing(false);
        }
    }
}
