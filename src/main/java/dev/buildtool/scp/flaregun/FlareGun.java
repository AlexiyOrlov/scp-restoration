package dev.buildtool.scp.flaregun;

import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.events.Entities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

@SCPObject(name = "A Flare Gun", number = "1577", classification = SCPObject.Classification.SAFE)
public class FlareGun extends Item {
    public FlareGun(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        Flare flare = Entities.flare.create(world);
        Vector3d lookAngles = playerEntity.getLookAngle();
        flare.setOwner(playerEntity);
        flare.setPos(playerEntity.getX() - lookAngles.x, playerEntity.getEyeY(), playerEntity.getZ() - lookAngles.z);
        flare.shootFromRotation(playerEntity, playerEntity.xRot, playerEntity.yRot, 0, 1, 1);
        world.addFreshEntity(flare);
        world.playSound(playerEntity, playerEntity.blockPosition(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 1, 1);
//        playerEntity.getCooldowns().addCooldown(this, Functions.minutesToTicks(10));
        return ActionResult.success(playerEntity.getItemInHand(hand));
    }
}
