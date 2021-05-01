package dev.buildtool.scp;

import dev.buildtool.satako.RandomizedList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;
import java.util.Random;

public class RandomLoot {
    static private final Random random = new Random();
    public static final String MESSAGE = "The loot can't be modified after it was built";
    private boolean built;
    RandomizedList<Object> randomKeys;
    HashMap<Object, Integer> objectIntegerHashMap;

    public RandomLoot() {
        objectIntegerHashMap = new HashMap<>();
    }

    public RandomLoot addItem(IItemProvider item, int maxCount) {
        if (built)
            throw new IllegalStateException(MESSAGE);
        objectIntegerHashMap.put(item, maxCount);
        return this;
    }

    public RandomLoot addItemTag(ITag<Item> itemITag, int maxCount) {
        if (built)
            throw new IllegalStateException(MESSAGE);
        objectIntegerHashMap.put(itemITag, maxCount);
        return this;
    }

    public RandomLoot build() {
        randomKeys = new RandomizedList<>(objectIntegerHashMap.keySet());
        built = true;
        return this;
    }


    public void generateInto(IItemHandler itemHandler) {
        int entryCount = objectIntegerHashMap.size();
        int times = random.nextInt(entryCount);
        int generateTimes = Math.min(itemHandler.getSlots(), times);
        for (int i = 0; i < generateTimes; i++) {
            Object obj = randomKeys.getRandom();
            int maxCount = objectIntegerHashMap.get(obj);
            if (obj instanceof IItemProvider) {
                ItemStack itemStack = new ItemStack((IItemProvider) obj, random.nextInt(maxCount));
                if (itemHandler.isItemValid(i, itemStack))
                    itemHandler.insertItem(i, itemStack, false);
            } else if (obj instanceof ITag) {
                ITag<Item> itemITag = (ITag<Item>) obj;
                Item item = itemITag.getRandomElement(random);
                ItemStack itemStack = new ItemStack(item, random.nextInt(maxCount));
                if (itemHandler.isItemValid(i, itemStack))
                    itemHandler.insertItem(i, itemStack, false);
            } else if (obj instanceof ItemStack) {
                ItemStack stack = (ItemStack) obj;
                if (itemHandler.isItemValid(i, stack))
                    itemHandler.insertItem(i, stack, false);
            }
        }
    }

    public void generateInto(IInventory inventory) {
        int entryCount = objectIntegerHashMap.size();
        int times = random.nextInt(entryCount);
        int generateTimes = Math.min(inventory.getContainerSize(), times);
        for (int i = 0; i < generateTimes; i++) {
            Object obj = randomKeys.getRandom();
            int maxCount = objectIntegerHashMap.get(obj);
            if (obj instanceof IItemProvider) {
                ItemStack itemStack = new ItemStack((IItemProvider) obj, random.nextInt(maxCount));
                if (inventory.canPlaceItem(i, itemStack))
                    inventory.setItem(i, itemStack);
            } else if (obj instanceof ITag) {
                ITag<Item> itemITag = (ITag<Item>) obj;
                Item item = itemITag.getRandomElement(random);
                ItemStack itemStack = new ItemStack(item, random.nextInt(maxCount));
                if (inventory.canPlaceItem(i, itemStack))
                    inventory.setItem(i, itemStack);
            } else if (obj instanceof ItemStack) {
                if (inventory.canPlaceItem(i, (ItemStack) obj))
                    inventory.setItem(i, (ItemStack) obj);
            }
        }
    }

    public RandomLoot addItemTag(ITag.INamedTag<Item> namedTag, int maxCount) {
        if (built)
            throw new IllegalStateException(MESSAGE);
        objectIntegerHashMap.put(namedTag, maxCount);
        return this;
    }

    /**
     * For non-stackable items
     */
    public RandomLoot addItemStack(ItemStack itemStack) {
        if (built)
            throw new IllegalStateException(MESSAGE);
        objectIntegerHashMap.put(itemStack, 1);
        return this;
    }
}
