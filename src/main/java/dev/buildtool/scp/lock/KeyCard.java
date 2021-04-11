package dev.buildtool.scp.lock;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class KeyCard extends Item {
    public KeyCard(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        BlockPos blockPos = context.getClickedPos();
        PlayerEntity playerEntity = context.getPlayer();
        World world = context.getLevel();
        TileEntity tileEntity = world.getBlockEntity(blockPos);
        if (tileEntity instanceof LockEntity) {
            LockEntity lockEntity = (LockEntity) tileEntity;
            CompoundNBT nbt = stack.getOrCreateTag();
            if (playerEntity.isCrouching()) {
                if (lockEntity.owner.equals(playerEntity.getUUID())) {
                    nbt.putString("Password", lockEntity.password);
                    nbt.putInt("x", lockEntity.getX());
                    nbt.putInt("y", lockEntity.getY());
                    nbt.putInt("z", lockEntity.getZ());
                    if (world.isClientSide) {
                        playerEntity.sendMessage(new TranslationTextComponent("scp.saved.password.on.card"), UUID.randomUUID());
                    }
                    return ActionResultType.SUCCESS;
                } else {
                    if (world.isClientSide) {
                        playerEntity.sendMessage(new TranslationTextComponent("scp.lock.is.not.yours"), UUID.randomUUID());
                        return ActionResultType.FAIL;
                    }
                }
            } else {
                String password = nbt.getString("Password");
                if (password.equals(lockEntity.password)) {
                    lockEntity.open();
                    return ActionResultType.SUCCESS;
                } else {
                    if (world.isClientSide)
                        playerEntity.sendMessage(new TranslationTextComponent("scp.wrong.password"), UUID.randomUUID());
                    return ActionResultType.FAIL;
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        CompoundNBT compoundNBT = stack.getTag();
        if (compoundNBT != null && compoundNBT.contains("Password")) {
            tooltip.add(new TranslationTextComponent("scp.lock.password").append(": " + compoundNBT.getString("Password")));
            tooltip.add(new TranslationTextComponent("scp.lock.location").append(": " + compoundNBT.getInt("x") + " " + compoundNBT.getInt("y") + " " + compoundNBT.getInt("z")));
        }
    }
}
