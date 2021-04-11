package dev.buildtool.scp.radioactbananas;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.events.SCPEffects;
import dev.buildtool.scp.events.SCPItems;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;

import java.util.Collections;

public class BananaDeathEffect extends Effect {
    public BananaDeathEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration < Functions.minutesToTicks(1);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn instanceof PlayerEntity && ((PlayerEntity) entityLivingBaseIn).isCreative())
            return;
        entityLivingBaseIn.hurt(new DamageSource("scp.banana.overdose").bypassInvul().bypassArmor(), 10);
        entityLivingBaseIn.spawnAtLocation(new ItemStack(SCPItems.banana, entityLivingBaseIn.getRandom().nextInt(33)));
        AreaEffectCloudEntity effectCloudEntity = new AreaEffectCloudEntity(entityLivingBaseIn.level, entityLivingBaseIn.getX(), entityLivingBaseIn.getY(), entityLivingBaseIn.getZ());
        effectCloudEntity.setRadius(3);
        EffectInstance effectInstance = new EffectInstance(SCPEffects.radiation, Functions.minutesToTicks(entityLivingBaseIn.getRandom().nextInt(6) + 12));
        effectInstance.setCurativeItems(Collections.emptyList());
        effectCloudEntity.setPotion(new Potion(effectInstance));
        effectCloudEntity.setDuration(effectInstance.getDuration());
        entityLivingBaseIn.level.addFreshEntity(effectCloudEntity);
    }
}
