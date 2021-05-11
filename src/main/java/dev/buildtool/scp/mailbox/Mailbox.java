package dev.buildtool.scp.mailbox;

import dev.buildtool.satako.blocks.BlockHorizontal;
import dev.buildtool.scp.events.SCPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class Mailbox extends BlockHorizontal {
    public Mailbox(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public ActionResultType use(BlockState p_225533_1_, World world, BlockPos blockPos, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {

        if (!world.isClientSide) {
            MailboxEntity mailboxEntity = (MailboxEntity) world.getBlockEntity(blockPos);
            NetworkHooks.openGui((ServerPlayerEntity) p_225533_4_, mailboxEntity, packetBuffer -> packetBuffer.writeBlockPos(blockPos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.mailboxEntity.create();
    }
}
