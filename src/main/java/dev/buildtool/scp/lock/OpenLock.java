package dev.buildtool.scp.lock;

import net.minecraft.util.math.BlockPos;

public class OpenLock {
    public BlockPos lockpos;

    public OpenLock(BlockPos lockpos) {
        this.lockpos = lockpos;
    }
}
