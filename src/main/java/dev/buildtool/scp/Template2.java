package dev.buildtool.scp;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.IClearable;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
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
import java.util.Optional;
import java.util.Random;

/**
 * Use this and subclasses instead of original template, because of block rotation difference
 */
public class Template2 extends Template {

    protected final List<ResourceLocation> structureLoots;
    protected ISeedReader seedReader;

    public Template2(Template template, List<ResourceLocation> loots, ISeedReader seedReader) {
        super();
        setAuthor(template.getAuthor());
        size = template.getSize();
        this.palettes = template.palettes;
        this.entityInfoList = template.entityInfoList;
        this.structureLoots = loots;
        this.seedReader = seedReader;
    }

    @Override
    public boolean placeInWorld(IServerWorld serverWorld, BlockPos pos, BlockPos blockPos, PlacementSettings settings, Random random, int intger) {
        if (this.palettes.isEmpty()) {
            return false;
        } else {
            List<BlockInfo> list = settings.getRandomPalette(this.palettes, pos).blocks();
            if ((!list.isEmpty() || !settings.isIgnoreEntities() && !this.entityInfoList.isEmpty()) && this.getSize().getX() >= 1 && this.getSize().getY() >= 1 && this.getSize().getZ() >= 1) {
                MutableBoundingBox mutableboundingbox = settings.getBoundingBox();
                List<BlockPos> list1 = Lists.newArrayListWithCapacity(settings.shouldKeepLiquids() ? list.size() : 0);
                List<Pair<BlockPos, CompoundNBT>> list2 = Lists.newArrayListWithCapacity(list.size());
                int i = Integer.MAX_VALUE;
                int j = Integer.MAX_VALUE;
                int k = Integer.MAX_VALUE;
                int l = Integer.MIN_VALUE;
                int i1 = Integer.MIN_VALUE;
                int j1 = Integer.MIN_VALUE;

                for (BlockInfo template$blockinfo : processBlockInfos(seedReader, pos, blockPos, settings, list, this)) {
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
                                    addLoot(tileentity1);

                                }
                            }

                            if (fluidstate != null && blockstate.getBlock() instanceof ILiquidContainer) {
                                ((ILiquidContainer) blockstate.getBlock()).placeLiquid(seedReader, blockpos, blockstate, fluidstate);
                                if (!fluidstate.isSource()) {
                                    list1.add(blockpos);
                                }
                            }
                        }
                    }
                }

                boolean flag = true;
                Direction[] adirection = new Direction[]{Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

                while(flag && !list1.isEmpty()) {
                    flag = false;
                    Iterator<BlockPos> iterator = list1.iterator();

                    while(iterator.hasNext()) {
                        BlockPos blockpos2 = iterator.next();
                        BlockPos blockpos3 = blockpos2;
                        FluidState fluidstate2 = seedReader.getFluidState(blockpos2);

                        for(int k1 = 0; k1 < adirection.length && !fluidstate2.isSource(); ++k1) {
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

                        for(Pair<BlockPos, CompoundNBT> pair1 : list2) {
                            BlockPos blockpos5 = pair1.getFirst();
                            voxelshapepart.setFull(blockpos5.getX() - i, blockpos5.getY() - j, blockpos5.getZ() - k, true, true);
                        }

                        updateShapeAtEdge(seedReader, intger, voxelshapepart, i, j, k);
                    }

                    for(Pair<BlockPos, CompoundNBT> pair : list2) {
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

    protected void addLoot(TileEntity tileEntity) {
        if (tileEntity instanceof LootContainer && structureLoots.size() > 0) {
            ResourceLocation resourceLocation = structureLoots.get(seedReader.getRandom().nextInt(structureLoots.size()));
            LootTable lootTable = seedReader.getLevel().getServer().getLootTables().get(resourceLocation);
            lootTable.fill(((LootContainer) tileEntity).provide(), new LootContext.Builder(seedReader.getLevel()).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(tileEntity.getBlockPos())).create(LootParameterSets.CHEST));
        }
    }

    protected void addEntitiesToWorld(IServerWorld p_237143_1_, BlockPos p_237143_2_, PlacementSettings placementIn) {
        for (Template.EntityInfo template$entityinfo : processEntityInfos(this, p_237143_1_, p_237143_2_, placementIn, this.entityInfoList)) {
            BlockPos blockpos;
            blockpos = template$entityinfo.blockPos; // FORGE: Position will have already been transformed by processEntityInfos
            if (placementIn.getBoundingBox() == null || placementIn.getBoundingBox().isInside(blockpos)) {
                CompoundNBT compoundnbt = template$entityinfo.nbt.copy();
                Vector3d vector3d1 = template$entityinfo.pos; // FORGE: Position will have already been transformed by processEntityInfos
                ListNBT listnbt = new ListNBT();
                listnbt.add(DoubleNBT.valueOf(vector3d1.x));
                listnbt.add(DoubleNBT.valueOf(vector3d1.y));
                listnbt.add(DoubleNBT.valueOf(vector3d1.z));
                compoundnbt.put("Pos", listnbt);
                compoundnbt.remove("UUID");
                loadEntity(p_237143_1_, compoundnbt).ifPresent((entity) -> {
                    float f = entity.mirror(placementIn.getMirror());
                    f = f + (entity.yRot - entity.rotate(placementIn.getRotation()));
                    entity.moveTo(vector3d1.x, vector3d1.y, vector3d1.z, f, entity.xRot);
                    if (placementIn.shouldFinalizeEntities() && entity instanceof MobEntity) {
                        ((MobEntity)entity).finalizeSpawn(p_237143_1_, p_237143_1_.getCurrentDifficultyAt(new BlockPos(vector3d1)), SpawnReason.STRUCTURE, null, compoundnbt);
                    }

                    p_237143_1_.addFreshEntityWithPassengers(entity);
                });
            }
        }

    }

    private static Optional<Entity> loadEntity(IServerWorld worldIn, CompoundNBT nbt) {
        try {
            return EntityType.create(nbt, worldIn.getLevel());
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
