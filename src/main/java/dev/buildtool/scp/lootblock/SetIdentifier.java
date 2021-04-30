package dev.buildtool.scp.lootblock;

import net.minecraft.util.math.BlockPos;

public class SetIdentifier {
    public String identifier;
    public BlockPos pos;

    public SetIdentifier() {
    }

    public SetIdentifier(String value, BlockPos blockPos) {
        this.identifier = value;
        this.pos = blockPos;
    }
}
