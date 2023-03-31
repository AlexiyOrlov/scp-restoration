package dev.buildtool.scp.harddrivecracker;

import net.minecraft.util.math.BlockPos;

public class CrackingProgress {
    public BlockPos pos;
    public int timeLeft;
    public CrackingProgress(BlockPos pos, int timeLeft) {
        this.pos = pos;
        this.timeLeft = timeLeft;
    }
}
