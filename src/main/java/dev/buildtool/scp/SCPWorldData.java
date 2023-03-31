package dev.buildtool.scp;

import dev.buildtool.satako.UniqueList;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class SCPWorldData extends WorldSavedData {
    public List<String> generatedSCPs;
    public List<String> generatedSCPDrives;
    public HashMap<BlockPos, BlockPos> map = new HashMap<>();
    public UniqueList<BlockPos> ikeaTeleporters = new UniqueList<>(4);

    public SCPWorldData() {
        super(SCP.ID);
        generatedSCPs = new ArrayList<>(30);
        generatedSCPDrives=new ArrayList<>(30);
    }

    @Override
    public void load(CompoundNBT nbt) {
        int count = nbt.getShort("Count");
        for (int i = 0; i < count; i++) {
            String nextSCP = nbt.getString("#" + i);
            generatedSCPs.add(nextSCP);
        }
        for (int i = 0; i < nbt.getShort("Drive count"); i++) {
            generatedSCPDrives.add(nbt.getString("SCP#"+i));
        }
        ikeaTeleporters = new UniqueList<>(Arrays.stream(nbt.getLongArray("Ikea teleporters")).mapToObj(BlockPos::of).collect(Collectors.toList()));
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        for (int i = 0; i < generatedSCPs.size(); i++) {
            compound.putString("#" + i, generatedSCPs.get(i));
        }
        compound.putShort("Count", (short) generatedSCPs.size());
        compound.putLongArray("Ikea teleporters", ikeaTeleporters.stream().map(BlockPos::asLong).distinct().collect(Collectors.toList()));
        for (int next = 0; next < generatedSCPDrives.size(); next++) {
            compound.putString("SCP#"+next,generatedSCPDrives.get(next));
        }
        compound.putShort("Drive count", (short) generatedSCPDrives.size());
        return compound;
    }
}
