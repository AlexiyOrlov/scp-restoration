package dev.buildtool.scp;

import dev.buildtool.satako.RandomizedList;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.RandomUtils;

import java.util.HashMap;
import java.util.Random;

public class IdentifiedRandomLoot {
    private static final Random random = new Random();
    protected String target;
    protected HashMap<Object, Integer> itemCounts = new HashMap<>();
    protected RandomizedList<Object> keys;
    private boolean complete;

    /**
     * @param target matching {@link dev.buildtool.scp.lootblock.LootBlockEntity#identifier}
     */
    public IdentifiedRandomLoot(String target) {
        this.target = target;
    }

    public IdentifiedRandomLoot addEnchantableItem(ItemStack item, int minLevel, int maxLevel) {
        assert item.isEnchantable();
        itemCounts.put(new EnchantableItem(item, minLevel, maxLevel), 1);
        return this;
    }

    public IdentifiedRandomLoot addItem(IItemProvider itemProvider, int maxCount) {
        assert maxCount < 64;
        itemCounts.put(itemProvider, maxCount);
        return this;
    }

    public IdentifiedRandomLoot addTag(ITag<Item> itemITag, int maxCount) {
        assert maxCount < 64;
        itemCounts.put(itemITag, maxCount);
        return this;
    }

    public IdentifiedRandomLoot addTag(ITag.INamedTag<Item> namedTag, int maxCount) {
        assert maxCount < 64;
        itemCounts.put(namedTag, maxCount);
        return this;
    }

    @SuppressWarnings("unchecked")
    public void generateInto(IItemHandler itemHandler) {
        if (!complete)
            throw new IllegalStateException("Incomplete random loot");
        int slots = itemHandler.getSlots();
        int generateCount = random.nextInt(slots) + 1;
        for (int i = 0; i < generateCount; i++) {
            Object randomItem = keys.getRandom();
            if (randomItem instanceof EnchantableItem) {
                EnchantableItem enchantableItem = (EnchantableItem) randomItem;
                ItemStack itemStack = EnchantmentHelper.enchantItem(random, enchantableItem.itemStack, RandomUtils.nextInt(enchantableItem.minLevel, enchantableItem.maxLevel), true);
                if (itemHandler.isItemValid(i, itemStack))
                    itemHandler.insertItem(i, itemStack, false);
            } else {
                Integer maxcount = itemCounts.get(randomItem);
                if (randomItem instanceof ITag) {
                    ITag<Item> itemITag = (ITag<Item>) randomItem;
                    Item item = itemITag.getRandomElement(random);
                    ItemStack itemStack = new ItemStack(item, random.nextInt(maxcount) + 1);
                    if (itemHandler.isItemValid(i, itemStack))
                        itemHandler.insertItem(i, itemStack, false);
                } else if (randomItem instanceof IItemProvider) {
                    ItemStack itemStack = new ItemStack((IItemProvider) randomItem, random.nextInt(maxcount) + 1);
                    if (itemHandler.isItemValid(i, itemStack))
                        itemHandler.insertItem(i, itemStack, false);
                }
            }
        }
    }

    private static class EnchantableItem {
        ItemStack itemStack;
        int minLevel;
        int maxLevel;

        public EnchantableItem(ItemStack item, int minLevel, int maxLevel) {
            this.itemStack = item;
            this.minLevel = minLevel;
            assert maxLevel < 31;
            this.maxLevel = maxLevel;
            assert minLevel < maxLevel;
        }
    }

    public IdentifiedRandomLoot build() {
        keys = new RandomizedList<>(itemCounts.keySet());
        complete = true;
        return this;
    }
}
