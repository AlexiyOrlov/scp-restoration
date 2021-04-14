package dev.buildtool.scp.pipenightmare;

import dev.buildtool.scp.SCPObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

@SCPObject(name = "Pipe Nightmare", number = "015", classification = SCPObject.Classification.EUCLID)
public class Explodable extends Block {
    private final float explosionStregth;

    public Explodable(Properties p_i48440_1_, float explosionStrength) {
        super(p_i48440_1_);
        explosionStregth = explosionStrength;
    }

    @Override
    public void playerWillDestroy(World world, BlockPos p_176208_2_, BlockState p_176208_3_, PlayerEntity p_176208_4_) {
        if (!world.isClientSide)
            world.explode(null, p_176208_2_.getX() + 0.5, p_176208_2_.getY(), p_176208_2_.getZ() + 0.5, explosionStregth, Explosion.Mode.BREAK);
    }
}