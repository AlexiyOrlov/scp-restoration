package dev.buildtool.scp.oldai;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.satako.RandomizedList;
import dev.buildtool.satako.UniqueList;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.stream.Collectors;

import static net.minecraft.state.properties.BlockStateProperties.OPEN;
import static net.minecraft.state.properties.BlockStateProperties.POWERED;

public class OldAIEntity extends BlockEntity2 implements ITickableTileEntity {

    private int iteration = 1;
    /**
     * Scanned positions
     */
    private UniqueList<BlockPos> knownPositions = new UniqueList<>(256);
    /**
     * Positions with applicable blocks
     */
    private UniqueList<BlockPos> occupiedPositions = new UniqueList<>(48);

    public OldAIEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putLongArray("Positions with blocks", occupiedPositions.stream().map(BlockPos::asLong).collect(Collectors.toList()));
        compound.putLongArray("Scanned positions", knownPositions.stream().map(BlockPos::asLong).collect(Collectors.toList()));
        compound.putInt("Iteration", iteration);
        return super.save(compound);

    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        iteration = nbt.getInt("Iteration");
        occupiedPositions = new UniqueList<>(Arrays.stream(nbt.getLongArray("Positions with blocks")).mapToObj(BlockPos::of).collect(Collectors.toList()));
        knownPositions = new UniqueList<>(Arrays.stream(nbt.getLongArray("Scanned positions")).mapToObj(BlockPos::of).collect(Collectors.toList()));
    }

    @Override
    public void tick() {
        if (!level.isClientSide) {
            BlockState state = getBlockState();
            if (state.getValue(OldAIBlock.active)) {
                RandomizedList<BlockPos> posRandomizedList = new RandomizedList<>(128);
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(getBlockPos()).inflate(iteration).expandTowards(-1, 0, -1);
                BlockPos.betweenClosedStream(axisAlignedBB).forEach(e -> posRandomizedList.add(e.immutable()));
                posRandomizedList.remove(getBlockPos());
                BlockPos randomposition = posRandomizedList.getRandom();
                BlockState randomBlock = level.getBlockState(randomposition);
                if (randomBlock.hasProperty(POWERED) || randomBlock.hasProperty(OPEN)) {
                    occupiedPositions.add(randomposition);
                }
                knownPositions.add(randomposition);
                if (!occupiedPositions.isEmpty()) {
                    BlockPos randomKnown = occupiedPositions.get(level.random.nextInt(occupiedPositions.size()));
                    BlockState blockState = level.getBlockState(randomKnown);
                    if (blockState.hasProperty(OPEN)) {
                        level.setBlockAndUpdate(randomKnown, blockState.cycle(OPEN));
                    } else if (blockState.hasProperty(POWERED)) {
                        level.setBlockAndUpdate(randomKnown, blockState.cycle(POWERED));
                    }
                }
                if (knownPositions.size() == posRandomizedList.size() && iteration < 16) {
                    //increase search bounding box
                    iteration++;
                }
            }
        }
    }
}
