package dev.buildtool.scp.itemscps;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.buildtool.scp.SCPObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SCPObject(name = "Complete Multitool",number ="117",classification = SCPObject.Classification.SAFE)
public class CompleteMultitool extends Item {
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final int damageChance = 3;

    public CompleteMultitool(Properties p_i48487_1_) {
        super(p_i48487_1_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 8, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (random.nextInt(damageChance) == 0)
            attacker.hurt(DamageSource.GENERIC, 1);
        stack.hurtAndBreak(1, attacker, (entity) -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public ActionResultType useOn(ItemUseContext useContext) {
        PlayerEntity playerentity = useContext.getPlayer();
        World world = useContext.getLevel();
        BlockPos blockpos = useContext.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (CampfireBlock.canLight(blockstate)) {
            world.playSound(playerentity, blockpos, SoundEvents.FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
            world.setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
            if (playerentity != null) {
                useContext.getItemInHand().hurtAndBreak(1, playerentity, (p_219999_1_) -> {
                    p_219999_1_.broadcastBreakEvent(useContext.getHand());
                });
                if (random.nextInt(damageChance) == 0)
                    playerentity.hurt(DamageSource.GENERIC, 1);
            }

            return ActionResultType.sidedSuccess(world.isClientSide());
        } else {
            BlockPos blockpos1 = blockpos.relative(useContext.getClickedFace());
            if (AbstractFireBlock.canBePlacedAt(world, blockpos1, useContext.getHorizontalDirection())) {
                world.playSound(playerentity, blockpos1, SoundEvents.FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
                BlockState blockstate1 = AbstractFireBlock.getState(world, blockpos1);
                world.setBlock(blockpos1, blockstate1, 11);
                ItemStack itemstack = useContext.getItemInHand();
                if (playerentity instanceof ServerPlayerEntity) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerentity, blockpos1, itemstack);
                    itemstack.hurtAndBreak(1, playerentity, (p_219998_1_) -> {
                        p_219998_1_.broadcastBreakEvent(useContext.getHand());
                    });
                    if (random.nextInt(damageChance) == 0)
                        playerentity.hurt(DamageSource.GENERIC, 1);
                }

                return ActionResultType.sidedSuccess(world.isClientSide());
            } else {
                return ActionResultType.FAIL;
            }
        }
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState blockState) {
        if (getToolTypes(stack).stream().anyMatch(blockState::isToolEffective))
            return ItemTier.NETHERITE.getSpeed();
        return 1;
    }

    @Override
    public boolean mineBlock(ItemStack p_179218_1_, World p_179218_2_, BlockState p_179218_3_, BlockPos p_179218_4_, LivingEntity user) {
        if (random.nextInt(damageChance) == 0)
            user.hurt(DamageSource.GENERIC, 1);
        return super.mineBlock(p_179218_1_, p_179218_2_, p_179218_3_, p_179218_4_, user);
    }

    @Override
    public net.minecraft.util.ActionResultType interactLivingEntity(ItemStack stack, net.minecraft.entity.player.PlayerEntity playerIn, LivingEntity entity, net.minecraft.util.Hand hand) {
        if (entity.level.isClientSide) return net.minecraft.util.ActionResultType.PASS;
        if (entity instanceof net.minecraftforge.common.IForgeShearable) {
            net.minecraftforge.common.IForgeShearable target = (net.minecraftforge.common.IForgeShearable) entity;
            BlockPos pos = new BlockPos(entity.getX(), entity.getY(), entity.getZ());
            if (target.isShearable(stack, entity.level, pos)) {
                java.util.List<ItemStack> drops = target.onSheared(playerIn, stack, entity.level, pos,
                        net.minecraft.enchantment.EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.enchantment.Enchantments.BLOCK_FORTUNE, stack));
                java.util.Random rand = new java.util.Random();
                drops.forEach(d -> {
                    net.minecraft.entity.item.ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
                    ent.setDeltaMovement(ent.getDeltaMovement().add((rand.nextFloat() - rand.nextFloat()) * 0.1F, rand.nextFloat() * 0.05F, (rand.nextFloat() - rand.nextFloat()) * 0.1F));
                });
                stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(hand));
            }
            return net.minecraft.util.ActionResultType.SUCCESS;
        }
        return net.minecraft.util.ActionResultType.PASS;
    }
}
