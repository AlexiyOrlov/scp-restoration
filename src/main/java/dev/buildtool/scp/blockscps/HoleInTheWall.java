package dev.buildtool.scp.blockscps;

import dev.buildtool.satako.blocks.BlockDirectional;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.capability.ThrownItemController;
import dev.buildtool.scp.capability.ThrownItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SCPObject(name = "A Hole in the Wall", number = "1162", classification = SCPObject.Classification.EUCLID)
public class HoleInTheWall extends BlockDirectional {
    public HoleInTheWall(Properties properties) {
        super(properties);
    }


    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        if (state.getValue(FACING) == hit.getDirection()) {
            if (!worldIn.isClientSide) {
                player.getCapability(ThrownItems.THROWNITEMS).ifPresent(thrownItemMemory -> {
                    if (thrownItemMemory.thrownItems().isEmpty()) {
                        player.sendMessage(new TranslationTextComponent("scp.found.nothing"), UUID.randomUUID());
                    } else {
                        ItemStack itemStack = thrownItemMemory.thrownItems().remove(worldIn.random.nextInt(thrownItemMemory.thrownItems().size()));
                        worldIn.addFreshEntity(new ItemEntity(worldIn, player.getX(), player.getY(), player.getZ(), itemStack.copy()));
                        if (player.inventory.isEmpty()) {
                            player.hurt(new DamageSource("scp.lost.organ"), worldIn.random.nextInt((int) player.getMaxHealth()));
                        } else {
                            List<Integer> nonempty = new ArrayList<>(45);
                            for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                                ItemStack next = player.inventory.getItem(i);
                                if (!next.isEmpty())
                                    nonempty.add(i);
                            }
                            player.inventory.removeItemNoUpdate(nonempty.get(worldIn.random.nextInt(nonempty.size())));
                        }
                        player.sendMessage(new TranslationTextComponent("scp.found").append(" ").append(itemStack.getDisplayName()), UUID.randomUUID());
                        Integer id = ThrownItemController.worldItems.get(itemStack);
                        if (id != null) {
                            Entity entity = worldIn.getEntity(id);
                            if (entity != null) {
                                ItemEntity itemEntity = (ItemEntity) entity;
                                itemEntity.getItem().shrink(itemStack.getCount());
                            }
                        }
                    }
                });
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }


    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }
}
