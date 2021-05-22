package dev.buildtool.scp.weapons;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class Flame extends Projectile {

    public Flame(EntityType<? extends Projectile> p_i231584_1_, World p_i231584_2_) {
        super(p_i231584_1_, p_i231584_2_, 1, 1, 10);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 10)
            remove();
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        super.onHitBlock(blockRayTraceResult);
        level.setBlockAndUpdate(blockRayTraceResult.getBlockPos().relative(blockRayTraceResult.getDirection()), Blocks.FIRE.defaultBlockState());
    }
}
