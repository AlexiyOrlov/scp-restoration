package dev.buildtool.scp;

import com.google.common.collect.HashMultimap;
import dev.buildtool.scp.events.SCPBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

public class ChamberLootManager {
    public static HashMultimap<String, RandomLoot> identifiedRandomLootHashMultimap = HashMultimap.create();

    static {
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
    }
}
