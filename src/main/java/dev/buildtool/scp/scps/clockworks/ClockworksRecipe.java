package dev.buildtool.scp.scps.clockworks;

import dev.buildtool.scp.Recipes;
import dev.buildtool.scp.events.ModEvents;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ClockworksRecipe implements IRecipe<Inventory> {
    private final ItemStack result;
    private final ItemStack input;
    private final int seconds;
    private final Mode mode;
    private final ResourceLocation identifier;

    public ClockworksRecipe(ItemStack input, ItemStack result, int seconds, Mode mode, ResourceLocation identifier) {
        this.result = result;
        this.input = input;
        this.seconds = seconds;
        this.mode = mode;
        this.identifier = identifier;
    }

    @Override
    public boolean matches(Inventory inv, World worldIn) {
        return true;
    }

    @Override
    public ItemStack assemble(Inventory inv) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return identifier;
    }

//    @Override
//    public void serializeRecipeData(JsonObject json) {
//        json.add("input", new JsonPrimitive(input.getItem().getRegistryName().toString()));
//        json.add("mode", new JsonPrimitive(mode.name));
//        json.add("seconds", new JsonPrimitive(seconds));
//        JsonObject result = new JsonObject();
//        result.add("item", new JsonPrimitive(this.result.getItem().getRegistryName().toString()));
//        result.add("count", new JsonPrimitive(this.result.getCount()));
//        json.add("result", result);
//    }



    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Recipes.clockworksRecipeSerializer;
    }

    @Override
    public IRecipeType<?> getType() {
        return ModEvents.clockworksRecipeType;
    }

//    @Nullable
//    @Override
//    public JsonObject serializeAdvancement() {
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public ResourceLocation getAdvancementId() {
//        return null;
//    }

    public int getSeconds() {
        return seconds;
    }

    public Mode getMode() {
        return mode;
    }

    public ItemStack getInput() {
        return input;
    }

    public enum Mode
    {
        ROUGH("rough"),
        COARSE("coarse"),
        ONE_ONE("one_one"),
        FINE("fine"),
        VERY_FINE("very_fine");

        public String name;

        Mode(String lowercase)
        {
            name=lowercase;
        }
    }
}
