package dev.buildtool.scp;

import dev.buildtool.satako.RandomizedList;
import dev.buildtool.satako.UniqueList;
import dev.buildtool.scp.events.Structures;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.stream.Collectors;

public class SiteFeature extends Feature<NoFeatureConfig> {
    RandomizedList<ResourceLocation> structures;
    public UniqueList<ResourceLocation> generated;
    public boolean listInitialized;
    public dev.buildtool.scp.SCPWorldData SCPWorldData;

    public SiteFeature() {
        super(NoFeatureConfig.CODEC);
        structures = new RandomizedList<>();
        structures.add(new ResourceLocation(SCP.ID, "containers/005"));
        structures.add(new ResourceLocation(SCP.ID, "containers/009"));
        structures.add(new ResourceLocation(SCP.ID, "containers/019"));
        structures.add(new ResourceLocation(SCP.ID, "containers/049"));
        structures.add(new ResourceLocation(SCP.ID, "containers/053"));
        structures.add(new ResourceLocation(SCP.ID, "containers/063"));
        structures.add(new ResourceLocation(SCP.ID, "containers/096"));
        structures.add(new ResourceLocation(SCP.ID, "containers/109"));
        structures.add(new ResourceLocation(SCP.ID, "containers/173"));
        structures.add(new ResourceLocation(SCP.ID, "containers/124"));
        structures.add(new ResourceLocation(SCP.ID, "containers/207"));
        structures.add(new ResourceLocation(SCP.ID, "containers/458"));
        structures.add(new ResourceLocation(SCP.ID, "containers/500"));
        structures.add(new ResourceLocation(SCP.ID, "containers/606"));
        structures.add(new ResourceLocation(SCP.ID, "containers/822"));
        structures.add(new ResourceLocation(SCP.ID, "containers/872"));
        structures.add(new ResourceLocation(SCP.ID, "containers/912"));
        structures.add(new ResourceLocation(SCP.ID, "containers/914"));
        structures.add(new ResourceLocation(SCP.ID, "containers/999"));
        structures.add(new ResourceLocation(SCP.ID, "containers/1162"));
        structures.add(new ResourceLocation(SCP.ID, "containers/1356"));
        structures.add(new ResourceLocation(SCP.ID, "containers/1437"));
        structures.add(new ResourceLocation(SCP.ID, "containers/1811"));
        structures.add(new ResourceLocation(SCP.ID, "containers/3008"));
        structures.add(new ResourceLocation(SCP.ID, "containers/3199"));
        structures.add(new ResourceLocation(SCP.ID, "containers/3521"));
        structures.add(new ResourceLocation(SCP.ID, "containers/5707"));
        generated = new UniqueList<>(structures.size());

    }

    @Override
    public boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random, final BlockPos blockPos, NoFeatureConfig noFeatureConfig) {
        if (random.nextInt(Structures.rarity.get()) == 1) {
            ServerWorld serverWorld = seedReader.getLevel();

            if (Structures.structureLootTables == null) {
                Structures.structureLootTables = serverWorld.getServer().getLootTables().tables.keySet().stream().filter(resourceLocation -> resourceLocation.getNamespace().equals(SCP.ID) && resourceLocation.getPath().contains("structureloot")).collect(Collectors.toList());
                assert Structures.structureLootTables.size() > 0;
            }
            Rotation rotation = Rotation.getRandom(random);
            BlockPos topPos = seedReader.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos.Mutable(blockPos.getX(), 64, blockPos.getZ()));
            TemplateManager structureManager = serverWorld.getStructureManager();

            ResourceLocation resourceLocation = structures.getRandom();

            RegistryKey<World> dimensionTypeRegistryKey = serverWorld.dimension();

            if (SCPWorldData == null && dimensionTypeRegistryKey == World.OVERWORLD && dimensionTypeRegistryKey.getRegistryName().toString().equals("minecraft:overworld")) {
                SCPWorldData = serverWorld.getDataStorage().get(SCPWorldData::new, SCP.ID);
                generated.addAll(SCPWorldData.generatedSCPs.stream().map(ResourceLocation::new).collect(Collectors.toList()));
                listInitialized = true;
                SCP.logger.info("Previously generated: {}", generated);
            }

            if (SCPWorldData != null && dimensionTypeRegistryKey == World.OVERWORLD) {
                if (generated.contains(resourceLocation))
                    return false;
                Template template = structureManager.get(resourceLocation);
                assert template != null;
                Template2 template2 = new Template2(template, Structures.structureLootTables.stream().filter(resourceLocation1 -> resourceLocation1.getPath().contains(resourceLocation.getPath().replace("containers/", ""))).collect(Collectors.toList()), seedReader);
                final BlockPos size = template2.getSize();
                assert size.getX() < 33 && size.getZ() < 33;

                if (!seedReader.getFluidState(topPos).isEmpty())
                    topPos = topPos.below();
                template2.placeInWorld(seedReader, topPos, new PlacementSettings().setRotation(rotation), random);
                SCP.logger.info("Generated {} at {}", resourceLocation.getPath(), topPos);
                generated.add(resourceLocation);
                SCPWorldData.generatedSCPs.add(resourceLocation.toString());
                if (generated.size() == structures.size()) {
                    generated.clear();
                    SCPWorldData.generatedSCPs.clear();
                }
                SCPWorldData.setDirty(true);
                return true;
            }
        }
        return false;
    }
}
