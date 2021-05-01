package dev.buildtool.scp;

import com.google.common.collect.HashMultimap;
import dev.buildtool.scp.events.SCPBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class ChamberLootManager {
    public static HashMultimap<String, RandomLoot> identifiedRandomLootHashMultimap = HashMultimap.create();
    private static final Random random = new Random();
    static {
        ITagCollection<Item> itemTags = TagCollectionManager.getInstance().getItems();
        identifiedRandomLootHashMultimap.put("914", new IdentifiedRandomLoot("ores", SCPBlocks.crateBlock.defaultBlockState())
                .addItem(Blocks.REDSTONE_ORE, 7).addItem(Blocks.DIAMOND_ORE, 3)
                .addItem(Blocks.EMERALD_ORE, 4).addItem(Blocks.GOLD_ORE, 5)
                .addItem(Blocks.LAPIS_ORE, 6).addItem(Blocks.IRON_ORE, 7)
                .addItem(Blocks.COAL_ORE, 8).build());
        identifiedRandomLootHashMultimap.put("914", new IdentifiedRandomLoot("food", Blocks.CHEST.defaultBlockState())
                .addItem(Items.BEEF, 3).addItem(Items.PORKCHOP, 3).addItem(Items.MUTTON, 4)
                .addItem(Items.RABBIT, 4).addItem(Items.SALMON, 4).addItem(Items.COD, 4)
                .addItem(Items.CHICKEN, 5).addItem(Items.POTATO, 5)
                .addItem(Items.CARROT, 5).addItem(Items.APPLE, 4)
                .addItem(Items.BEETROOT, 7).addItem(Items.MELON, 8).build());
        identifiedRandomLootHashMultimap.put("914", new IdentifiedRandomLoot("seeds", SCPBlocks.fourItemTable.defaultBlockState())
                .addItem(Items.MELON_SEEDS, 2).addItem(Items.PUMPKIN_SEEDS, 2).addItem(Items.WHEAT_SEEDS, 4)
                .addItem(Items.BEETROOT_SEEDS, 4).build());
        identifiedRandomLootHashMultimap.put("914", new IdentifiedRandomLoot("horse armor", SCPBlocks.fourItemTable.defaultBlockState())
                .addItem(Items.GOLDEN_HORSE_ARMOR, 2).addItem(Items.LEATHER_HORSE_ARMOR, 2)
                .addItem(Items.IRON_HORSE_ARMOR, 2).addItem(Items.DIAMOND_HORSE_ARMOR, 2).build());
        identifiedRandomLootHashMultimap.put("914", new IdentifiedRandomLoot("other", SCPBlocks.crateBlock.defaultBlockState())
                .addItem(Items.BONE, 10).addItem(Blocks.GRAVEL, 14).addItem(Items.SKELETON_SPAWN_EGG, 3)
                .addItem(Items.BOW, 2).addItem(Items.SADDLE, 2).addItem(Items.SUGAR_CANE, 8)
                .addItem(Items.COW_SPAWN_EGG, 3).addItem(Blocks.SAND, 15).addItem(Blocks.SANDSTONE, 15)
                .addItem(Blocks.GLASS, 20).addItem(Blocks.PUMPKIN, 2).addItem(Blocks.MELON, 2)
                .addItem(Items.WHEAT, 5).addItem(Items.BLAZE_ROD, 3).build());
        identifiedRandomLootHashMultimap.put("914", new IdentifiedRandomLoot("wood", Blocks.BARREL.defaultBlockState())
                .addItem(Blocks.OAK_WOOD, 20).addItem(Blocks.ACACIA_WOOD, 20)
                .addItem(Blocks.BIRCH_WOOD, 20).addItem(Blocks.DARK_OAK_WOOD, 20)
                .addItem(Blocks.JUNGLE_WOOD, 20).addItem(Blocks.SPRUCE_WOOD, 20).build());

        //124 fertile soil
        identifiedRandomLootHashMultimap.put("124", new IdentifiedRandomLoot("crops", SCPBlocks.fourItemTable.defaultBlockState())
                .addItemTag(itemTags.getTag(new ResourceLocation("forge", "crops")), 24).build());
        identifiedRandomLootHashMultimap.put("124", new IdentifiedRandomLoot("saplings", Blocks.CHEST.defaultBlockState())
                .addItemTag(ItemTags.SAPLINGS, 12).build());

        //049 doctor
        identifiedRandomLootHashMultimap.put("049", new IdentifiedRandomLoot("books and paper", SCPBlocks.fourItemTable.defaultBlockState())
                .addItem(Items.PAPER, 15).addItem(Items.BOOK, 9)
                .addItemStack(EnchantmentHelper.enchantItem(random, new ItemStack(Items.BOOK), random.nextInt(30), true)).build());

    }
}
