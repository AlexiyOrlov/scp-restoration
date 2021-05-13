package dev.buildtool.scp.flaregun;

import dev.buildtool.scp.RandomLoot;
import dev.buildtool.scp.events.SCPBlocks;
import dev.buildtool.scp.weapons.Projectile;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class Flare extends Projectile {
    static {
        ITagCollectionSupplier tagCollectionManager = TagCollectionManager.getInstance();
        ITagCollection<Item> tagCollection = tagCollectionManager.getItems();
        RANDOM_LOOT = new RandomLoot().addItemTag(tagCollection.getTag(new ResourceLocation("swords")), 1)
                .addItemTag(tagCollection.getTag(new ResourceLocation("armor")), 1)
                .addItem(Items.NETHERITE_CHESTPLATE, 1).addItem(Items.NETHERITE_HELMET, 1)
                .addItem(Items.NETHERITE_LEGGINGS, 1).addItem(Items.NETHERITE_BOOTS, 1)
                .addItem(Blocks.TNT, 64).addItem(Items.TURTLE_HELMET, 1).addItem(Items.TRIDENT, 1)
                .addItem(Items.CROSSBOW, 1).addItem(Items.BOW, 1).addItem(Items.ARROW, 64)
                .addItem(Items.SHIELD, 1).addItemTag(tagCollection.getTag(new ResourceLocation("axes")), 1)
                .addItem(Items.DIAMOND_HORSE_ARMOR, 1).addItem(Items.GOLDEN_HORSE_ARMOR, 1)
                .addItem(Items.IRON_HORSE_ARMOR, 1).addItem(Items.LEATHER_HORSE_ARMOR, 1).build();
    }

    static final RandomLoot RANDOM_LOOT;
    BlockPos lightPosition = BlockPos.ZERO;

    /**
     * @param lightness    0 to 1
     */
    public Flare(EntityType<? extends Projectile> p_i231584_1_, World p_i231584_2_, int damage_, double lightness) {
        super(p_i231584_1_, p_i231584_2_, damage_, lightness);
        noCulling = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide && level.isEmptyBlock(blockPosition().relative(getDirection().getOpposite()))) {
            level.setBlockAndUpdate(blockPosition().relative(getDirection().getOpposite()), SCPBlocks.invisibleLight.defaultBlockState());
            if (!lightPosition.equals(BlockPos.ZERO))
                level.removeBlock(lightPosition, false);
            lightPosition = blockPosition().relative(getDirection().getOpposite());
        }
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
        BlockPos blockPos = blockRayTraceResult.getBlockPos();
        Direction facing = blockRayTraceResult.getDirection();
        if (!level.isClientSide) {
            level.setBlockAndUpdate(blockPos.relative(facing), SCPBlocks.crate.defaultBlockState());
            CrateEntity crateEntity = (CrateEntity) level.getBlockEntity(blockPos.relative(facing));
            crateEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> itemHandler.insertItem(0, RANDOM_LOOT.getRandomItem(), false));
            crateEntity.setChanged();
        }
    }
}
