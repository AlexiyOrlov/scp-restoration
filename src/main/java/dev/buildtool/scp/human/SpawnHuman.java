package dev.buildtool.scp.human;

import net.minecraft.util.math.BlockPos;

public class SpawnHuman {
    public boolean male;
    public BlockPos at;

    public SpawnHuman() {
    }

    public SpawnHuman(boolean male, BlockPos at) {
        this.male = male;
        this.at = at;
    }
}
