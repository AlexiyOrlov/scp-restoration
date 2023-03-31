package dev.buildtool.scp.flaregun;

import dev.buildtool.satako.blocks.Block2;
import dev.buildtool.scp.registration.SCPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class Crate extends Block2 {
    public Crate(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.woodenCrate.create();
    }
}
