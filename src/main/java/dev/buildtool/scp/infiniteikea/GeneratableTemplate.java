package dev.buildtool.scp.infiniteikea;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.buildtool.scp.LootContainer;
import dev.buildtool.scp.Template2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.IClearable;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Usable in world generation by using the {@link ISeedReader} instance.
 */
public class GeneratableTemplate extends Template2 {

    public GeneratableTemplate(Template template, List<ResourceLocation> loots, ISeedReader reader) {
        super(template, loots, reader, false);
    }

    @Override
    public boolean placeInWorld(@Deprecated IServerWorld serverWorld, BlockPos pos, BlockPos blockPos, PlacementSettings settings, Random random, int intger) {
        if (this.palettes.isEmpty()) {
            return false;
        } else {
            List<BlockInfo> blockInfos = settings.getRandomPalette(this.palettes, pos).blocks();
            if ((!blockInfos.isEmpty() || !settings.isIgnoreEntities() && !this.entityInfoList.isEmpty()) && this.getSize().getX() >= 1 && this.getSize().getY() >= 1 && this.getSize().getZ() >= 1) {
                MutableBoundingBox mutableboundingbox = settings.getBoundingBox();
                List<BlockPos> posArrayList = Lists.newArrayListWithCapacity(settings.shouldKeepLiquids() ? blockInfos.size() : 0);
                List<Pair<BlockPos, CompoundNBT>> list2 = Lists.newArrayListWithCapacity(blockInfos.size());
                int i = Integer.MAX_VALUE;
                int j = Integer.MAX_VALUE;
                int k = Integer.MAX_VALUE;
                int l = Integer.MIN_VALUE;
                int i1 = Integer.MIN_VALUE;
                int j1 = Integer.MIN_VALUE;

                for (BlockInfo template$blockinfo : processBlockInfos(seedReader, pos, blockPos, settings, blockInfos, this)) {
                    BlockPos blockpos = template$blockinfo.pos;
                    if (mutableboundingbox == null || mutableboundingbox.isInside(blockpos)) {
                        FluidState fluidstate = settings.shouldKeepLiquids() ? seedReader.getFluidState(blockpos) : null;
                        BlockState blockstate = template$blockinfo.state.mirror(settings.getMirror()).rotate(seedReader, blockPos, settings.getRotation());
                        if (template$blockinfo.nbt != null) {
                            TileEntity tileentity = seedReader.getBlockEntity(blockpos);
                            IClearable.tryClear(tileentity);
                            seedReader.setBlock(blockpos, Blocks.BARRIER.defaultBlockState(), 20);
                        }

                        if (seedReader.setBlock(blockpos, blockstate, intger)) {
                            i = Math.min(i, blockpos.getX());
                            j = Math.min(j, blockpos.getY());
                            k = Math.min(k, blockpos.getZ());
                            l = Math.max(l, blockpos.getX());
                            i1 = Math.max(i1, blockpos.getY());
                            j1 = Math.max(j1, blockpos.getZ());
                            list2.add(Pair.of(blockpos, template$blockinfo.nbt));
                            if (template$blockinfo.nbt != null) {
                                TileEntity tileentity1 = seedReader.getBlockEntity(blockpos);
                                if (tileentity1 != null) {
                                    template$blockinfo.nbt.putInt("x", blockpos.getX());
                                    template$blockinfo.nbt.putInt("y", blockpos.getY());
                                    template$blockinfo.nbt.putInt("z", blockpos.getZ());
                                    if (tileentity1 instanceof LockableLootTileEntity) {
                                        template$blockinfo.nbt.putLong("LootTableSeed", random.nextLong());
                                    }

                                    tileentity1.load(template$blockinfo.state, template$blockinfo.nbt);
                                    tileentity1.mirror(settings.getMirror());
                                    tileentity1.rotate(settings.getRotation());
                                    if (tileentity1 instanceof LootContainer && structureLoots.size() > 0) {
                                        ResourceLocation resourceLocation = structureLoots.get(random.nextInt(structureLoots.size()));
                                        LootTable lootTable = seedReader.getLevel().getServer().getLootTables().get(resourceLocation);
                                        lootTable.fill(((LootContainer) tileentity1).provide(), new LootContext.Builder(seedReader.getLevel()).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(tileentity1.getBlockPos())).create(LootParameterSets.CHEST));

                                    }
                                }
                            }

                            if (fluidstate != null && blockstate.getBlock() instanceof ILiquidContainer) {
                                ((ILiquidContainer) blockstate.getBlock()).placeLiquid(seedReader, blockpos, blockstate, fluidstate);
                                if (!fluidstate.isSource()) {
                                    posArrayList.add(blockpos);
                                }
                            }
                        }
                    }
                }

                boolean flag = true;
                Direction[] adirection = new Direction[]{Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

                while (flag && !posArrayList.isEmpty()) {
                    flag = false;
                    Iterator<BlockPos> iterator = posArrayList.iterator();

                    while (iterator.hasNext()) {
                        BlockPos blockpos2 = iterator.next();
                        BlockPos blockpos3 = blockpos2;
                        FluidState fluidstate2 = seedReader.getFluidState(blockpos2);

                        for (int k1 = 0; k1 < adirection.length && !fluidstate2.isSource(); ++k1) {
                            BlockPos blockpos1 = blockpos3.relative(adirection[k1]);
                            FluidState fluidstate1 = seedReader.getFluidState(blockpos1);
                            if (fluidstate1.getHeight(seedReader, blockpos1) > fluidstate2.getHeight(seedReader, blockpos3) || fluidstate1.isSource() && !fluidstate2.isSource()) {
                                fluidstate2 = fluidstate1;
                                blockpos3 = blockpos1;
                            }
                        }

                        if (fluidstate2.isSource()) {
                            BlockState blockstate2 = seedReader.getBlockState(blockpos2);
                            Block block = blockstate2.getBlock();
                            if (block instanceof ILiquidContainer) {
                                ((ILiquidContainer) block).placeLiquid(seedReader, blockpos2, blockstate2, fluidstate2);
                                flag = true;
                                iterator.remove();
                            }
                        }
                    }
                }

                if (i <= l) {
                    if (!settings.getKnownShape()) {
                        VoxelShapePart voxelshapepart = new BitSetVoxelShapePart(l - i + 1, i1 - j + 1, j1 - k + 1);

                        for (Pair<BlockPos, CompoundNBT> pair1 : list2) {
                            BlockPos blockpos5 = pair1.getFirst();
                            voxelshapepart.setFull(blockpos5.getX() - i, blockpos5.getY() - j, blockpos5.getZ() - k, true, true);
                        }

                        updateShapeAtEdge(seedReader, intger, voxelshapepart, i, j, k);
                    }

                    for (Pair<BlockPos, CompoundNBT> pair : list2) {
                        BlockPos blockpos4 = pair.getFirst();
                        if (!settings.getKnownShape()) {
                            BlockState blockstate1 = seedReader.getBlockState(blockpos4);
                            BlockState blockstate3 = Block.updateFromNeighbourShapes(blockstate1, seedReader, blockpos4);
                            if (blockstate1 != blockstate3) {
                                seedReader.setBlock(blockpos4, blockstate3, intger & -2 | 16);
                            }

                            seedReader.blockUpdated(blockpos4, blockstate3.getBlock());
                        }

                        if (pair.getSecond() != null) {
                            TileEntity tileentity2 = seedReader.getBlockEntity(blockpos4);
                            if (tileentity2 != null) {
                                tileentity2.setChanged();
                            }
                        }
                    }
                }

                if (!settings.isIgnoreEntities()) {
                    this.addEntitiesToWorld(seedReader, pos, settings);
                }

                return true;
            } else {
                return false;
            }
        }

    }
}
