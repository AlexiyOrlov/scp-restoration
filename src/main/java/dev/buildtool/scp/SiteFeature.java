package dev.buildtool.scp;

import dev.buildtool.satako.RandomizedList;
import dev.buildtool.satako.UniqueList;
import dev.buildtool.scp.registration.Structures;
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

import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class SiteFeature extends Feature<NoFeatureConfig> {
    public RandomizedList<ResourceLocation> structures;
    public UniqueList<ResourceLocation> generated;
    public dev.buildtool.scp.SCPWorldData scpWorldData;

    public SiteFeature() {
        super(NoFeatureConfig.CODEC);
        structures = new RandomizedList<>();
        structures.add(new ResourceLocation(SCP.ID, "containers/005"));
        structures.add(new ResourceLocation(SCP.ID, "containers/009"));
        structures.add(new ResourceLocation(SCP.ID, "containers/015"));
        structures.add(new ResourceLocation(SCP.ID, "containers/019"));
        structures.add(new ResourceLocation(SCP.ID, "containers/049"));
        structures.add(new ResourceLocation(SCP.ID, "containers/053"));
        structures.add(new ResourceLocation(SCP.ID, "containers/063"));
        structures.add(new ResourceLocation(SCP.ID, "containers/079"));
        structures.add(new ResourceLocation(SCP.ID, "containers/096"));
        structures.add(new ResourceLocation(SCP.ID, "containers/109"));
        structures.add(new ResourceLocation(SCP.ID, "containers/117"));
        structures.add(new ResourceLocation(SCP.ID, "containers/124"));
        structures.add(new ResourceLocation(SCP.ID, "containers/173"));
        structures.add(new ResourceLocation(SCP.ID, "containers/207"));
        structures.add(new ResourceLocation(SCP.ID, "containers/403"));
        structures.add(new ResourceLocation(SCP.ID, "containers/409"));
        structures.add(new ResourceLocation(SCP.ID, "containers/458"));
        structures.add(new ResourceLocation(SCP.ID, "containers/500"));
        structures.add(new ResourceLocation(SCP.ID, "containers/606"));
        structures.add(new ResourceLocation(SCP.ID, "containers/822"));
        structures.add(new ResourceLocation(SCP.ID, "containers/872"));
        structures.add(new ResourceLocation(SCP.ID, "containers/912"));
        structures.add(new ResourceLocation(SCP.ID, "containers/914"));
        structures.add(new ResourceLocation(SCP.ID, "containers/999"));
        structures.add(new ResourceLocation(SCP.ID, "containers/1162"));
        structures.add(new ResourceLocation(SCP.ID, "containers/1188"));
        structures.add(new ResourceLocation(SCP.ID, "containers/1356"));
        structures.add(new ResourceLocation(SCP.ID, "containers/1437"));
        structures.add(new ResourceLocation(SCP.ID, "containers/1577"));
        structures.add(new ResourceLocation(SCP.ID, "containers/1811"));
        structures.add(new ResourceLocation(SCP.ID, "containers/2059"));
        structures.add(new ResourceLocation(SCP.ID, "containers/2295"));
        structures.add(new ResourceLocation(SCP.ID, "containers/2398"));
        structures.add(new ResourceLocation(SCP.ID, "containers/2948"));
        structures.add(new ResourceLocation(SCP.ID, "containers/3008"));
        structures.add(new ResourceLocation(SCP.ID, "containers/3199"));
        structures.add(new ResourceLocation(SCP.ID, "containers/3521"));
        structures.add(new ResourceLocation(SCP.ID, "containers/3821"));
        structures.add(new ResourceLocation(SCP.ID, "containers/5707"));
        Collections.shuffle(structures);
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

            Optional<ResourceLocation> optional = structures.stream().filter(resourceLocation1 -> !generated.contains(resourceLocation1) && !SCP.blacklistedSCPs.get().contains(resourceLocation1)).findAny();
            if (optional.isPresent()) {
                RegistryKey<World> dimensionTypeRegistryKey = serverWorld.dimension();
                ResourceLocation resourceLocation = optional.get();
                if (scpWorldData == null && dimensionTypeRegistryKey == World.OVERWORLD) {
                    scpWorldData = Utils.getData(serverWorld);
                    generated.addAll(scpWorldData.generatedSCPs.stream().map(ResourceLocation::new).collect(Collectors.toList()));
                    SCP.logger.info("Previously generated: {}: {}", generated.size(), generated);
                }

                if (scpWorldData != null && dimensionTypeRegistryKey == World.OVERWORLD) {
                    if (generated.contains(resourceLocation))
                        return false;
                    Template template = structureManager.get(resourceLocation);
                    assert template != null;
                    Template2 template2 = new Template2(template, Structures.structureLootTables.stream().filter(resourceLocation1 -> resourceLocation1.getPath().contains(resourceLocation.getPath().replace("containers/", ""))).collect(Collectors.toList()), seedReader, true);
                    final BlockPos size = template2.getSize();
                    assert size.getX() < 33 && size.getZ() < 33;

                    if (!seedReader.getFluidState(topPos).isEmpty())
                        topPos = topPos.below();
                    template2.placeInWorld(seedReader, topPos, new PlacementSettings().setRotation(rotation), random);
                    SCP.logger.info("Generated {} at {}", resourceLocation.getPath(), topPos);
                    generated.add(resourceLocation);
                    scpWorldData.generatedSCPs.add(resourceLocation.toString());
                    if (generated.size() == structures.size()) {
                        generated.clear();
                        scpWorldData.generatedSCPs.clear();
                    }
                    scpWorldData.setDirty(true);
                    return true;
                }
            }
        }
        return false;
    }
}
