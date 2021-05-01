package dev.buildtool.scp.theteacher;

import dev.buildtool.satako.Functions;
import dev.buildtool.satako.RandomizedList;
import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.events.SCPBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SCPObject(name = "The Teacher", number = "606", classification = SCPObject.Classification.EUCLID)
public class TheTeacherEntity extends SCPEntity {
    private BlockPos lightPos = BlockPos.ZERO;

    public TheTeacherEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        setNoGravity(true);
        noPhysics = true;
        setInvulnerable(true);
    }

    @Override
    public void tick() {
        super.tick();
        BlockState inPosition = level.getBlockState(blockPosition());
        if (!blockPosition().equals(lightPos)) {
            level.removeBlock(lightPos, false);
        }
        if (level.isEmptyBlock(blockPosition()) && inPosition != SCPBlocks.invisibleLight.defaultBlockState()) {
            level.setBlockAndUpdate(blockPosition(), SCPBlocks.invisibleLight.defaultBlockState());
            lightPos = blockPosition();
        }
        RandomizedList<PlayerEntity> randomizedList = new RandomizedList(level.getEntities(this, getBoundingBox().inflate(3), entity -> entity instanceof PlayerEntity));
        PlayerEntity randomPlayer = randomizedList.getRandom();
        if (randomPlayer != null && Functions.isPlayerInSurvival(randomPlayer)) {
            randomPlayer.hurt(new DamageSource("scp.knowledge.overload").bypassArmor().bypassInvul(), random.nextInt(2) + 1);
            randomPlayer.giveExperiencePoints(1);
        }

        if (tickCount % 60 == 0) {
            RandomizedList<ItemEntity> itemEntities = new RandomizedList<>(level.getEntities(EntityType.ITEM, new AxisAlignedBB(blockPosition()).inflate(3), itemEntity -> itemEntity.getItem().isEnchantable()));
            ItemEntity radnomEchantable = itemEntities.getRandom();
            if (radnomEchantable != null) {
                EnchantmentHelper.enchantItem(random, radnomEchantable.getItem(), random.nextInt(30) + 1, true);
            }
        }
    }

    @Override
    public boolean attackable() {
        return false;
    }

}
