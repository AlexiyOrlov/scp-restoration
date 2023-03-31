package dev.buildtool.scp.itemscps;

import dev.buildtool.scp.SCP;
import dev.buildtool.scp.SCPObject;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

@SCPObject(classification = SCPObject.Classification.SAFE,number = "063",name = "The World's Best Tothbrush")
public class Tothbrush extends Item {
    public Tothbrush(Properties properties) {
        super(properties);
    }


    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        if (blockState.getDestroySpeed(context.getLevel(), context.getClickedPos()) != -1 || SCP.toothBrushCanBreakUnbreakable.get()) {
            if (!context.getLevel().isClientSide)
                context.getLevel().destroyBlock(context.getClickedPos(), false);
            if (context.getPlayer() != null)
                context.getPlayer().getCooldowns().addCooldown(this, 4);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
