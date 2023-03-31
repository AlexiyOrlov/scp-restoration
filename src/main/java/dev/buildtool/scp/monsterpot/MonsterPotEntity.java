package dev.buildtool.scp.monsterpot;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.satako.Functions;
import dev.buildtool.scp.registration.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;

import java.util.Set;
import java.util.stream.Collectors;

public class MonsterPotEntity extends BlockEntity2 implements ITickableTileEntity {
    int timeUntilSpawn;

    public MonsterPotEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void tick() {
        if (!level.isClientSide) {
            BlockPos topCorner = getBlockPos().above().north().west();
            BlockPos bottomCorner = getBlockPos().below(2).south().east();
            Set<BlockPos> set = BlockPos.betweenClosedStream(bottomCorner, topCorner).collect(Collectors.toSet());
            int increment = 37;
            for (BlockPos blockPos : set) {
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState == Blocks.ICE.defaultBlockState() ||
                        blockState == Blocks.FROSTED_ICE.defaultBlockState() ||
                        blockState == Blocks.BLUE_ICE.defaultBlockState() ||
                        blockState == Blocks.PACKED_ICE.defaultBlockState()) {
                    increment--;
                }
            }
            timeUntilSpawn += increment;
            if (timeUntilSpawn >= Functions.hoursToTicks(1)) {
                PotMonster potMonster = Entities.potMonster.create(level);
                potMonster.finalizeSpawn((IServerWorld) level, level.getCurrentDifficultyAt(getBlockPos()), SpawnReason.SPAWNER, null, null);
                potMonster.setPos(getBlockPos().getX() + 0.5, getBlockPos().getY() + 1, getBlockPos().getZ() + 0.5);
                level.addFreshEntity(potMonster);
                timeUntilSpawn = 0;
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("SpawnTime", timeUntilSpawn);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        timeUntilSpawn = nbt.getInt("SpawnTime");
    }
}
