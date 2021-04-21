package dev.buildtool.scp.weapons;

import dev.buildtool.scp.RangedWeapon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public abstract class Firearm extends Item implements RangedWeapon {
    protected int cooldown;

    public Firearm(Properties properties, int cooldown) {
        super(properties);
        this.cooldown = cooldown;
    }

    @Override
    public int cooldown() {
        return cooldown;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        shoot(worldIn, playerIn, handIn, null);
        worldIn.playSound(playerIn, playerIn.blockPosition(), fireSound(), SoundCategory.PLAYERS, soundVolume(), 1);
        playerIn.getCooldowns().addCooldown(this, cooldown());
        ItemStack weapon = playerIn.getItemInHand(handIn);
        weapon.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(handIn));
        return ActionResult.consume(weapon);
    }
}
