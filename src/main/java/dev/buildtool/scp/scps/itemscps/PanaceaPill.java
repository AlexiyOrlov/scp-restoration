package dev.buildtool.scp.scps.itemscps;

import dev.buildtool.scp.SCPObject;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.HashSet;

@SCPObject(name = "Panacea",number = "500",classification = SCPObject.Classification.SAFE)
public class PanaceaPill extends Item {
    public PanaceaPill(Properties properties) {
        super(properties);
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 15;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerEntity, Hand handIn) {
        if(playerEntity.getHealth()<playerEntity.getMaxHealth() || playerEntity.getActiveEffects().stream().anyMatch(effectInstance -> effectInstance.getEffect().getCategory()== EffectType.HARMFUL))
        {
            playerEntity.startUsingItem(handIn);
            return ActionResult.success(playerEntity.getItemInHand(handIn));
        }
        return super.use(worldIn, playerEntity, handIn);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if(heal(entityLiving))
        {
            stack.shrink(1);
        }
        return stack;
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if(heal(target))
        {
            stack.shrink(1);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private boolean heal(LivingEntity entityLiving) {
        boolean decreaseStack = false;
        HashSet<EffectInstance> toRemove = new HashSet<>();
        for (EffectInstance activePotionEffect : entityLiving.getActiveEffects()) {
            if (activePotionEffect.getEffect().getCategory() == EffectType.HARMFUL) {
                decreaseStack = true;
                toRemove.add(activePotionEffect.getEffectInstance());
            }
        }
        toRemove.forEach(effectInstance -> entityLiving.removeEffect(effectInstance.getEffect()));
        if (entityLiving.getHealth() < entityLiving.getMaxHealth()) {
            entityLiving.heal(entityLiving.getMaxHealth());
            decreaseStack = true;
        }
        return decreaseStack;
    }
}
