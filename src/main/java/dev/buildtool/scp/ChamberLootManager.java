package dev.buildtool.scp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ChamberLootManager extends JsonReloadListener {
    static Gson GSON = new GsonBuilder().create();

    public ChamberLootManager() {
        super(GSON, "chamber_loot");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> fileLocationJsonElements, IResourceManager resourceManager, IProfiler profiler) {
        fileLocationJsonElements.forEach((resourceLocation, jsonElement) -> {
            System.out.println(resourceLocation + " " + jsonElement);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
        });
    }
}
