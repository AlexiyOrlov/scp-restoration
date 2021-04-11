package dev.buildtool.scp.lock;

import net.minecraft.util.math.BlockPos;

public class SetPassword {
    public BlockPos lockpos;

    public SetPassword(BlockPos lockpos, String string) {
        this.lockpos = lockpos;
        this.string = string;
    }

    public String string;
}
