package dev.buildtool.scp.items;

import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.capability.CapabilityController;
import dev.buildtool.scp.capability.SCPKnowledge;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class Analyzer extends Item {
    public Analyzer(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        Block block= context.getLevel().getBlockState(context.getClickedPos()).getBlock();
        SCPObject object = block.getClass().getAnnotation(SCPObject.class);
        if(object !=null)
        {
            SCPKnowledge.Data res = CapabilityController.getKnowledge(context.getPlayer()).knownSCPData().putIfAbsent(object.number(), new SCPKnowledge.Data(object.number(), object.classification(), object.name()));
           if(res==null) {
               if (context.getLevel().isClientSide)
                   context.getPlayer().sendMessage(new TranslationTextComponent("scp.revealed.data.about").append("SCP-" + object.number()), UUID.randomUUID());
               CapabilityController.syncKnowledge(context.getLevel(), context.getPlayer());
               return ActionResultType.SUCCESS;
           }
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        SCPObject scpObject=target.getClass().getAnnotation(SCPObject.class);
        if(scpObject!=null)
        {
            SCPKnowledge.Data data = CapabilityController.getKnowledge(playerIn).knownSCPData().putIfAbsent(scpObject.number(), new SCPKnowledge.Data(scpObject.number(), scpObject.classification(), scpObject.name()));
            if(data==null) {
                if(playerIn.level.isClientSide)
                    playerIn.sendMessage(new TranslationTextComponent("scp.revealed.data.about").append("SCP-" + scpObject.number()), UUID.randomUUID());
                CapabilityController.syncKnowledge(playerIn.level, playerIn);

                return ActionResultType.SUCCESS;
            }
        }
        return super.interactLivingEntity(stack, playerIn, target, hand);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        BlockRayTraceResult blockRayTraceResult=Item.getPlayerPOVHitResult(worldIn,playerIn, RayTraceContext.FluidMode.NONE);

        List<ItemEntity> itemEntity = worldIn.getEntities(EntityType.ITEM, new AxisAlignedBB(blockRayTraceResult.getBlockPos().relative(blockRayTraceResult.getDirection())), itemEntity1 -> true);
        if (!itemEntity.isEmpty()) {
            Item item = itemEntity.get(0).getItem().getItem();
            SCPObject scpObject = item.getClass().getAnnotation(SCPObject.class);
            if (scpObject != null) {
                SCPKnowledge.Data data = CapabilityController.getKnowledge(playerIn).knownSCPData().putIfAbsent(scpObject.number(), new SCPKnowledge.Data(scpObject.number(), scpObject.classification(), scpObject.name()));
                if(data==null) {
                    if (worldIn.isClientSide)
                        playerIn.sendMessage(new TranslationTextComponent("scp.revealed.data.about").append("SCP-" + scpObject.number()), UUID.randomUUID());
                    CapabilityController.syncKnowledge(worldIn, playerIn);
                    return ActionResult.success(playerIn.getItemInHand(handIn));
                }
            }
        }

        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("scp.analyzer.usage"));
    }
}
