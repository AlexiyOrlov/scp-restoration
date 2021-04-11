package dev.buildtool.scp.blocks;

import dev.buildtool.satako.blocks.BlockHorizontal;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.events.SCPItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * Pizza box gives pizza as long as the player is hungry.
 */
@SCPObject(number = "458", classification = SCPObject.Classification.SAFE, name = "The Never-Ending Pizza Box")
public class PizzaBox extends BlockHorizontal {
    private final static VoxelShape BOX = VoxelShapes.box(0, 0, 0, 1, 2 / 16d, 1);

    public PizzaBox(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(player.getFoodData().needsFood())
        {
            if(!player.inventory.add(new ItemStack(SCPItems.pizzaSlice)))
            {
                if (worldIn.isClientSide)
                    player.sendMessage(new TranslationTextComponent("scp.full.inventory"), UUID.randomUUID());
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BOX;
    }
}
