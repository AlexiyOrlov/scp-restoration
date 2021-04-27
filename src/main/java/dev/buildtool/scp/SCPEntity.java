package dev.buildtool.scp;

import dev.buildtool.satako.Functions;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class SCPEntity extends CreatureEntity {
    public SCPEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public double getRange() {
        return getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    protected boolean shouldDropLoot() {
        return true;
    }

    public ItemStack getProjectile(ItemStack shootable) {
        if (shootable.getItem() instanceof ShootableItem) {
            Predicate<ItemStack> predicate = ((ShootableItem) shootable.getItem()).getAllSupportedProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(this, predicate);
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.getDirectEntity() instanceof PlayerEntity)
            if (!Functions.isPlayerInSurvival((PlayerEntity) source.getDirectEntity()))
                return false;
        return super.isInvulnerableTo(source);
    }

    @Override
    public void tick() {
        updateSwingTime();
        super.tick();
    }



    @Override
    protected int getExperienceReward(PlayerEntity player) {
        return (int) (getAttributeValue(Attributes.ATTACK_DAMAGE) + getAttributeValue(Attributes.MAX_HEALTH));
    }

    protected SoundEvent attackSound() {
        return SoundEvents.PLAYER_ATTACK_STRONG;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (attackSound() != null)
            playSound(attackSound(), 1, random.nextBoolean() ? 1 : 0.9f);
        return super.doHurtTarget(entityIn);
    }

    @Override
    public boolean checkSpawnRules(IWorld worldIn, SpawnReason spawnReasonIn) {
        return true;
    }
}
