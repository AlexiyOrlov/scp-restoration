package dev.buildtool.scp.scps.itemscps;

import dev.buildtool.scp.SCPObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

@SCPObject(classification = SCPObject.Classification.SAFE,number = "109",name = "Infinite Canteeen")
public class InfiniteCanteen extends Item {
    public InfiniteCanteen(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        BlockPos blockPos=context.getClickedPos();
        World world=context.getLevel();
        BlockState blockState=world.getBlockState(blockPos);
        Block block=blockState.getBlock();
        if(block instanceof IGrowable)
        {
            IGrowable growable= (IGrowable) block;
            if(growable.isValidBonemealTarget(world,blockPos,blockState,world.isClientSide))
            {
                if(world instanceof ServerWorld)
                    growable.performBonemeal((ServerWorld) world,world.random,blockPos,blockState);
                context.getPlayer().getCooldowns().addCooldown(this,10);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 40;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
            playerIn.startUsingItem(handIn);
        return ActionResult.success(playerIn.getItemInHand(handIn));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        entityLiving.removeEffect(Effects.MOVEMENT_SLOWDOWN);
        entityLiving.removeEffect(Effects.CONFUSION);
        entityLiving.removeEffect(Effects.WEAKNESS);
        entityLiving.removeEffect(Effects.POISON);
        entityLiving.removeEffect(Effects.WITHER);
        return stack;
    }
}
