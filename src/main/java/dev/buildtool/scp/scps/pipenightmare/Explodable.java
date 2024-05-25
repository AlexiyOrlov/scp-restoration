package dev.buildtool.scp.scps.pipenightmare;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class Explodable extends Block {
    private final float explosionStrength;

    public Explodable(Properties p_i48440_1_, float explosionStrength) {
        super(p_i48440_1_);
        this.explosionStrength = explosionStrength;
    }

    @Override
    public void playerWillDestroy(World world, BlockPos p_176208_2_, BlockState p_176208_3_, PlayerEntity p_176208_4_) {
        if (!world.isClientSide)
            world.explode(null, p_176208_2_.getX() + 0.5, p_176208_2_.getY(), p_176208_2_.getZ() + 0.5, explosionStrength, Explosion.Mode.BREAK);
    }
}
