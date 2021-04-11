package dev.buildtool.scp.crate;

import dev.buildtool.satako.blocks.Block2;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CrateBlock extends Block2 {
    public CrateBlock(Properties properties) {
        super(properties, false);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        CrateEntity crateEntity= (CrateEntity) worldIn.getBlockEntity(pos);
        if(worldIn instanceof ServerWorld)
        {
            NetworkHooks.openGui((ServerPlayerEntity) player,crateEntity,packetBuffer -> packetBuffer.writeBlockPos(pos));
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
        return SCPTiles.crateEntity.create();
    }
}
