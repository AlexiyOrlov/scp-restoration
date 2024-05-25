package dev.buildtool.scp.scps.mailbox;

import net.minecraft.util.math.BlockPos;

public class SendMail {
    public BlockPos tilePos, targetPos;

    public SendMail(BlockPos tilePos, BlockPos targetPos) {
        this.tilePos = tilePos;
        this.targetPos = targetPos;
    }

    public SendMail() {
    }
}
