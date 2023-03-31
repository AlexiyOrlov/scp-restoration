package dev.buildtool.scp.itemscps;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCPObject;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.world.server.ServerWorld;

@SCPObject(name = "Home Run Bat", number = "2398", classification = SCPObject.Classification.SAFE)
public class HomeRunBat extends Item {
    public HomeRunBat(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }


    @Override
    public ActionResultType interactLivingEntity(ItemStack bat, PlayerEntity playerEntity, LivingEntity livingEntity, Hand p_111207_4_) {
        if (playerEntity.getCooldowns().isOnCooldown(this))
            return ActionResultType.PASS;
        if (livingEntity.isAttackable()) {
            livingEntity.hurt(DamageSource.playerAttack(playerEntity).bypassArmor(), livingEntity.getMaxHealth());
            if (livingEntity.isDeadOrDying()) {
                EntitySize entitySize = livingEntity.getDimensions(Pose.STANDING);
                float biggest = Math.max(entitySize.height, entitySize.width);
                if (playerEntity.level instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld) playerEntity.level;
                    for (int i = 0; i < biggest * 200; i += 1) {
                        int dirx = random.nextBoolean() ? 1 : -1;
                        int dirz = random.nextBoolean() ? 1 : -1;
                        int ry = random.nextInt(4);
                        serverWorld.sendParticles(new BlockParticleData(ParticleTypes.BLOCK, Blocks.REDSTONE_WIRE.defaultBlockState()), livingEntity.getX(), livingEntity.getY() + ry, livingEntity.getZ(), 0, random.nextDouble() * dirx, random.nextDouble(), random.nextDouble() * dirz, 0);
                    }
                }
                playerEntity.level.playSound(null, livingEntity.blockPosition(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundCategory.PLAYERS, 1, 1);

            }
            playerEntity.getCooldowns().addCooldown(this, Functions.minutesToTicks(1));
            return ActionResultType.sidedSuccess(playerEntity.level.isClientSide);
        }
        return super.interactLivingEntity(bat, playerEntity, livingEntity, p_111207_4_);
    }
}
