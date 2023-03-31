package dev.buildtool.scp.itemscps;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.registration.SCPEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Collections;

@SCPObject(number = "207", classification = SCPObject.Classification.SAFE, name = "Cola Bottles")
public class ColaBottle extends Item {
    public ColaBottle(Properties properties) {
        super(properties);
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return DrinkHelper.useDrink(worldIn, playerIn, handIn);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        entityLiving.addEffect(new EffectInstance(Effects.DIG_SPEED, Functions.minutesToTicks(10), 2));
        entityLiving.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Functions.minutesToTicks(10), 2));
        entityLiving.addEffect(new EffectInstance(Effects.JUMP, Functions.minutesToTicks(10), 2));
        entityLiving.addEffect(new EffectInstance(Effects.DOLPHINS_GRACE, Functions.minutesToTicks(10), 2));
        EffectInstance insomnia = new EffectInstance(SCPEffects.insomnia, Functions.minutesToTicks(10));
        insomnia.setCurativeItems(Collections.emptyList());
        entityLiving.addEffect(insomnia);
        stack.shrink(1);
        return stack;
    }
}
