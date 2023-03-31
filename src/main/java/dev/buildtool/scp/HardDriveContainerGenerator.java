package dev.buildtool.scp;

import dev.buildtool.satako.UniqueList;
import dev.buildtool.scp.events.SCPBlocks;
import dev.buildtool.scp.harddrivecracker.HardDriveStoreEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class HardDriveContainerGenerator extends Feature<NoFeatureConfig> {
    UniqueList<String> alreadyGeneratedSCPDrives;
    List<String> allSCPs;
    public SCPWorldData scpWorldData;

    public HardDriveContainerGenerator() {
        super(NoFeatureConfig.CODEC);
        alreadyGeneratedSCPDrives=new UniqueList<>();
        allSCPs=new ArrayList<>();
        Utils.getAllSCPs().forEach(stringObjectMap -> {
            allSCPs.add(stringObjectMap.get("number").toString());
        });
    }

    @Override
    public boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random p_241855_3_, BlockPos p_241855_4_, NoFeatureConfig p_241855_5_) {
        if(p_241855_3_.nextInt(SCP.hardDriveRarity.get())==0){
            Optional<String> optionalSCP=allSCPs.stream().filter(s -> !alreadyGeneratedSCPDrives.contains(s) && !SCP.blacklistedSCPs.get().contains(s)).findAny();
            ServerWorld level = seedReader.getLevel();
            RegistryKey<World> worldRegistryKey=level.dimension();
            if(worldRegistryKey==World.OVERWORLD && optionalSCP.isPresent()){
                BlockPos topPosition=seedReader.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,new BlockPos.Mutable(p_241855_4_.getX(),128,p_241855_4_.getZ()));

                if(scpWorldData==null) {
                    scpWorldData = Utils.getData(level);
                    alreadyGeneratedSCPDrives.addAll(scpWorldData.generatedSCPDrives);
                }
                while (!seedReader.getFluidState(topPosition.below()).isEmpty()){
                    topPosition=topPosition.below();
                }
                seedReader.setBlock(topPosition, SCPBlocks.hardDriveStore.defaultBlockState(), 2);
                HardDriveStoreEntity hardDriveStore = (HardDriveStoreEntity) seedReader.getBlockEntity(topPosition);
                hardDriveStore.hardDrive.getOrCreateTag().putString("Number", optionalSCP.get());
                hardDriveStore.setChanged();
                SCP.logger.info("SCP hard drive with number " + optionalSCP.get() + " generated at {} {} {}", topPosition.getX(), topPosition.getY(), topPosition.getZ());
                alreadyGeneratedSCPDrives.add(optionalSCP.get());
                scpWorldData.generatedSCPDrives.add(optionalSCP.get());
                if(alreadyGeneratedSCPDrives.size()==allSCPs.size()){
                    alreadyGeneratedSCPDrives.clear();
                    scpWorldData.generatedSCPDrives.clear();
                }
                scpWorldData.setDirty();
                return true;
            }
        }
        return false;
    }
}
