package dev.buildtool.scp.itemscps;

import dev.buildtool.scp.SCPObject;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

@SCPObject(number = "403", classification = SCPObject.Classification.SAFE, name = "Escalating Lighter")
public class Lighter extends Item {
    public Lighter(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity playerEntity, Hand hand) {
        playerEntity.startUsingItem(hand);
        return ActionResult.success(playerEntity.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 200;
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.BLOCK;
    }

    @Override
    public void releaseUsing(ItemStack itemStack, World world, LivingEntity livingEntity, int left) {
        int duration = getUseDuration(itemStack) - left;
        if (duration > 9) {
            if (!world.isClientSide)
                world.explode(livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), duration / 10f, Explosion.Mode.DESTROY);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, World world, LivingEntity livingEntity) {
        if (!world.isClientSide)
            world.explode(livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), getUseDuration(itemStack) / 10f, Explosion.Mode.DESTROY);
        return itemStack;
    }
}
