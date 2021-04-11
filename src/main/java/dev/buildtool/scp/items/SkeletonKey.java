package dev.buildtool.scp.items;

import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.events.Sounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SCPObject(name = "Skeleton Key",number = "005",classification = SCPObject.Classification.SAFE)
public class SkeletonKey extends Item {
    public SkeletonKey(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        BlockPos blockPos=context.getClickedPos();
        World world=context.getLevel();
        BlockState blockState=world.getBlockState(blockPos);
        Block block=blockState.getBlock();
        if(block instanceof DoorBlock)
        {
            DoorBlock doorBlock= (DoorBlock) block;
            if(blockState.getValue(DoorBlock.HALF)== DoubleBlockHalf.UPPER)
            {
                blockPos=blockPos.below();
                blockState=world.getBlockState(blockPos);
            }

            doorBlock.setOpen(world, blockState, blockPos, !blockState.getValue(DoorBlock.OPEN));
            world.playSound(context.getPlayer(), blockPos, Sounds.skeletonKeyUnlock, SoundCategory.BLOCKS, 1, 1);
            return ActionResultType.SUCCESS;
        }
        if(blockState.getProperties().contains(BlockStateProperties.OPEN))
        {
            world.setBlockAndUpdate(blockPos, blockState.cycle(BlockStateProperties.OPEN));
            world.playSound(context.getPlayer(), blockPos, Sounds.skeletonKeyUnlock, SoundCategory.BLOCKS, 1, 1);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
