package dev.buildtool.scp.events;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.infiniteikea.RoomGenerator;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
@SuppressWarnings("unused")
public class Events {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void biomesLoad(BiomeLoadingEvent loadingEvent) {
        ResourceLocation biomeName = loadingEvent.getName();
        Biome.Category category = loadingEvent.getCategory();
        if (biomeName != null && biomeName.equals(new ResourceLocation(SCP.ID, "iikea1"))) {
            loadingEvent.getGeneration().getFeatures(GenerationStage.Decoration.RAW_GENERATION).add(() -> new ConfiguredFeature<>(new RoomGenerator(), new NoFeatureConfig()));
        } else if (category != Biome.Category.OCEAN && category != Biome.Category.THEEND && category != Biome.Category.NETHER && category != Biome.Category.RIVER) {
            //at least in this stage the structure isn't that cut out
            loadingEvent.getGeneration().getFeatures(GenerationStage.Decoration.SURFACE_STRUCTURES).add(() -> Structures.scpSite.configured(new NoFeatureConfig()));
            loadingEvent.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(Entities.chaosInsurgencySoldier, SCP.chaosSoldierWeight.get(), 1, 3));
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent playerTickEvent) {
        PlayerEntity playerEntity = playerTickEvent.player;
        if (playerEntity instanceof ServerPlayerEntity && !playerEntity.isSpectator()) {
            if (Functions.isHolding(item -> item==SCPItems.rubberDuck, playerEntity)) {
                List<BlockPos> blockPos = Functions.boundingBoxToPositions(playerEntity.getBoundingBox().inflate(2));
                blockPos.forEach(pos -> {
                    if (playerEntity.level.isWaterAt(pos))
                        playerEntity.level.setBlockAndUpdate(pos, Fluids.EMPTY.defaultFluidState().createLegacyBlock());
                });
            }
        }
    }

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent serverStartedEvent) {
        MinecraftServer server = serverStartedEvent.getServer();
        if (SCP.writeClockworksRecipes.get()) {
            File dir = server.getServerDirectory();
            List<String> recipes = server.getRecipeManager().getAllRecipesFor(ModEvents.clockworksRecipeType).stream().map(clockworksRecipe -> clockworksRecipe.getInput() + " in " + clockworksRecipe.getMode() + " mode makes " + clockworksRecipe.getResultItem() + " in " + clockworksRecipe.getSeconds() + " seconds").collect(Collectors.toList());
            File recipelist = new File(dir, "Clockworks recipes.txt");
            try {
                if (Files.notExists(recipelist.toPath()))
                    Files.createFile(recipelist.toPath());
                Files.write(recipelist.toPath(), recipes, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void trySleep(PlayerSleepInBedEvent playerSleepInBedEvent) {
        PlayerEntity playerEntity = playerSleepInBedEvent.getPlayer();
        //TODO insomnia from scp-207
    }

    @SubscribeEvent
    public static void onServerStop(FMLServerStoppedEvent serverStoppedEvent) {
        Structures.scpSite.SCPWorldData = null;
    }

}
