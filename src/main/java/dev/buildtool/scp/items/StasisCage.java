package dev.buildtool.scp.items;

import dev.buildtool.scp.SCPEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class StasisCage extends Item {
    private static final String ENTITY = "Contained";

    public StasisCage(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerEntity, LivingEntity livingEntity, Hand hand) {
        if (!stack.hasTag() && livingEntity instanceof SCPEntity) {
            CompoundNBT compoundNBT = livingEntity.serializeNBT();
            stack.getOrCreateTag().put(ENTITY, compoundNBT);
            stack.getTag().putString("Name", livingEntity.getName().getString());
            livingEntity.remove();
            return ActionResultType.SUCCESS;
        }
        return super.interactLivingEntity(stack, playerEntity, livingEntity, hand);
    }

    @Override
    public ActionResultType useOn(ItemUseContext p_195939_1_) {
        BlockPos target = p_195939_1_.getClickedPos();
        Direction face = p_195939_1_.getClickedFace();
        target = target.relative(face);
        ItemStack item = p_195939_1_.getItemInHand();
        if (item.hasTag()) {
            CompoundNBT compoundNBT = item.getTag().getCompound(ENTITY);
            Optional<Entity> optionalEntity = EntityType.create(compoundNBT, p_195939_1_.getLevel());
            if (optionalEntity.isPresent()) {
                Entity entity = optionalEntity.get();
                entity.setPos(target.getX() + 0.5, target.getY(), target.getZ());
                p_195939_1_.getLevel().addFreshEntity(entity);
                item.shrink(1);
                return ActionResultType.SUCCESS;
            }
        }
        return super.useOn(p_195939_1_);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable World p_77624_2_, List<ITextComponent> components, ITooltipFlag p_77624_4_) {
        super.appendHoverText(itemStack, p_77624_2_, components, p_77624_4_);
        if (itemStack.hasTag()) {
            CompoundNBT compoundNBT = itemStack.getTag();
            components.add(new StringTextComponent(compoundNBT.getString("Name")));
        }
    }
}
