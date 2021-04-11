package dev.buildtool.scp.human;

import dev.buildtool.satako.blocks.Block2;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public class HumanHireBlock extends Block2 {
    public HumanHireBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        int goldIngots = 0;
        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
            ItemStack next = player.inventory.getItem(i);
            if (next.getItem() == Items.GOLD_INGOT)
                goldIngots += next.getCount();
            if (goldIngots > 4) {
                if (worldIn.isClientSide) {
                    openScreen(pos);
                }
                return ActionResultType.SUCCESS;
            }
        }
        if (worldIn.isClientSide)
            player.sendMessage(new TranslationTextComponent("scp.not.enough.money"), UUID.randomUUID());
        return ActionResultType.FAIL;
    }

    @OnlyIn(Dist.CLIENT)
    public void openScreen(BlockPos blockPos) {
        Minecraft.getInstance().setScreen(new HumanHireScreen(new StringTextComponent(""), blockPos));
    }
}
