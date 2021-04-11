package dev.buildtool.scp;

import com.google.gson.JsonObject;
import dev.buildtool.scp.clockworks.ClockworksRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Recipes {

    public static IRecipeSerializer<ClockworksRecipe> clockworksRecipeSerializer;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<IRecipeSerializer<?>> recipeTypeRegister)
    {
        IForgeRegistry<IRecipeSerializer<?>> forgeRegistry=recipeTypeRegister.getRegistry();
        clockworksRecipeSerializer=new IRecipeSerializer<ClockworksRecipe>() {
            private ResourceLocation name;
            @Override
            public ClockworksRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
                String inputId = json.get("input").getAsString();
                String mode = json.get("mode").getAsString();
                int time = json.get("seconds").getAsInt();
                JsonObject result = json.getAsJsonObject("result");
                String resultId = result.get("item").getAsString();
                int resultCount = 1;
                if (result.has("count"))
                    resultCount = result.get("count").getAsInt();
                ItemStack in = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(inputId)));
                ItemStack out = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(resultId)), resultCount);
                for (ClockworksRecipe.Mode value : ClockworksRecipe.Mode.values()) {
                    if (value.name.equals(mode)) {
                        return new ClockworksRecipe(in, out, time, value, recipeId);
                    }
                }
                return null;
            }

            @Nonnull
            @Override
            public ClockworksRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buf) {
                ItemStack in=buf.readItem();
                ItemStack out=buf.readItem();
                int time=buf.readShort();
                ClockworksRecipe.Mode mode=buf.readEnum(ClockworksRecipe.Mode.class);
                return new ClockworksRecipe(in, out, time, mode, recipeId);
            }

            @Override
            public void toNetwork(PacketBuffer buf, ClockworksRecipe recipe) {
                buf.writeItem(recipe.getInput());
                buf.writeItem(recipe.getResultItem());
                buf.writeShort(recipe.getSeconds());
                buf.writeEnum(recipe.getMode());
            }

            @Override
            public IRecipeSerializer<?> setRegistryName(ResourceLocation name) {
                this.name=name;
                return this;
            }

            @Nullable
            @Override
            public ResourceLocation getRegistryName() {
                return name;
            }

            @Override
            public Class<IRecipeSerializer<?>> getRegistryType() {
                return ForgeRegistries.RECIPE_SERIALIZERS.getRegistrySuperType();
            }
        };

        clockworksRecipeSerializer.setRegistryName(new ResourceLocation(SCP.ID,"clockworks"));
        forgeRegistry.registerAll(clockworksRecipeSerializer);
    }
}
