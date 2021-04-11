package dev.buildtool.scp.items;

import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.events.Sounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

@SCPObject(number = "1356", classification = SCPObject.Classification.SAFE, name = "Rubber Ducky")
public class RubberDucky extends Item {
    public RubberDucky(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        worldIn.playLocalSound(playerIn.getX(), playerIn.getY(), playerIn.getZ(), Sounds.rubberDucky, SoundCategory.PLAYERS, 1, 1, false);
        return ActionResult.success(playerIn.getItemInHand(handIn));
    }
}
