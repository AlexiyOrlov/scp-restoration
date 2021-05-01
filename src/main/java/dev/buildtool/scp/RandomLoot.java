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

@SuppressWarnings("unchecked")
public class RandomLoot {
    static private final Random random = new Random();
    public static final String MESSAGE = "The loot can't be modified after it was built";
    private boolean built;
    RandomizedList<Object> randomKeys;
    HashMap<Object, Integer> objectChanceHashMap;

    public RandomLoot() {
        objectChanceHashMap = new HashMap<>();
    }

    public RandomLoot addItem(IItemProvider item, int maxCount) {
        if (maxCount == 1)
            maxCount = 2;
        if (built)
            throw new IllegalStateException(MESSAGE);
        objectChanceHashMap.put(item, maxCount);
        return this;
    }

    public RandomLoot addItemTag(ITag<Item> itemITag, int maxCount) {
        if (maxCount == 1)
            maxCount = 2;
        if (built)
            throw new IllegalStateException(MESSAGE);
        objectChanceHashMap.put(itemITag, maxCount);
        return this;
    }

    public RandomLoot build() {
        randomKeys = new RandomizedList<>(objectChanceHashMap.keySet());
        built = true;
        return this;
    }

    public void generateInto(IItemHandler itemHandler) {
        int entryCount = Math.max(objectChanceHashMap.size(), 1);
        int times = random.nextInt(entryCount);
        int generateTimes = Math.max(1, Math.min(itemHandler.getSlots(), times));
        for (int i = 0; i < generateTimes; i++) {
            Object obj = randomKeys.getRandom();
            int maxCount = objectChanceHashMap.get(obj);
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
        int entryCount = objectChanceHashMap.size();
        int times = random.nextInt(entryCount);
        int generateTimes = Math.min(inventory.getContainerSize(), times);
        for (int i = 0; i < generateTimes; i++) {
            Object obj = randomKeys.getRandom();
            int maxCount = objectChanceHashMap.get(obj);
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
            }
        }
    }

    public RandomLoot addItemTag(ITag.INamedTag<Item> namedTag, int maxCount) {
        if (maxCount == 1)
            maxCount = 2;
        if (built)
            throw new IllegalStateException(MESSAGE);
        objectChanceHashMap.put(namedTag, maxCount);
        return this;
    }

}
