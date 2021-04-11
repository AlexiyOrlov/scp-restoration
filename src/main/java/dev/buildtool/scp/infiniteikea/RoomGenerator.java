package dev.buildtool.scp.infiniteikea;

import dev.buildtool.satako.RandomizedList;
import dev.buildtool.scp.RandomLoot;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.TemplateWithRandomLoot;
import dev.buildtool.scp.events.SCPBlocks;
import dev.buildtool.scp.lamp.SmallLamp;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class RoomGenerator extends Feature<NoFeatureConfig> {

    ITag<Block> PLANKS;
    ITag<Block> WOODS;
    ITag<Block> WOOL;
    ITag<Block> DOORS;
    ITag<Block> BANNERS;
    ITag<Block> FENCES;
    ITag<Block> SLABS;
    ITag<Block> STAIRS;
    ITag<Block> FLOWERS;
    ITag<Block> TRAPDOORS;
    ITag<Block> WALLS;
    private final RandomizedList<ITag<Block>> randomBlockTagList;
    private final ITag<Block> containers;
    private final ITag<Item> helmets;
    private final ITag<Item> chestplates;
    private final ITag<Item> leggings;
    private final ITag<Item> boots;
    RandomizedList<ResourceLocation> structureList;
    RandomLoot randomAmbientLoot;
    RandomLoot randomBaseLoot;
    public RoomGenerator() {
        super(NoFeatureConfig.CODEC);
        ITagCollection<Block> blockTags = TagCollectionManager.getInstance().getBlocks();
        PLANKS = blockTags.getTag(BlockTags.PLANKS.getName());
        WOODS = blockTags.getTag(BlockTags.LOGS.getName());
        WOOL = blockTags.getTag(BlockTags.WOOL.getName());
        DOORS = blockTags.getTag(BlockTags.DOORS.getName());
        BANNERS = blockTags.getTag(BlockTags.BANNERS.getName());
        FENCES = blockTags.getTag(BlockTags.FENCES.getName());
        SLABS = blockTags.getTag(BlockTags.SLABS.getName());
        /*unused*/
        FLOWERS = blockTags.getTag(BlockTags.FLOWERS.getName());
        /*unused*/
        TRAPDOORS = blockTags.getTag(BlockTags.TRAPDOORS.getName());
        WALLS = blockTags.getTag(BlockTags.WALLS.getName());
        randomBlockTagList = new RandomizedList<>(Arrays.asList(WALLS, FENCES, WOOL, WOODS, PLANKS));
        containers = TagCollectionManager.getInstance().getBlocks().getTag(new ResourceLocation(SCP.ID, "containers"));
        assert containers != null;
        ITagCollection<Item> itemTags = TagCollectionManager.getInstance().getItems();
        helmets = itemTags.getTag(new ResourceLocation("helmets"));
        chestplates = itemTags.getTag(new ResourceLocation("chestplates"));
        leggings = itemTags.getTag(new ResourceLocation("leggings"));
        boots = itemTags.getTag(new ResourceLocation("boots"));
        structureList = new RandomizedList<>(3);
        structureList.add(new ResourceLocation(SCP.ID, "iikea/base1"));
        structureList.add(new ResourceLocation(SCP.ID, "iikea/base2"));
        structureList.add(new ResourceLocation(SCP.ID, "iikea/base3"));
        randomAmbientLoot = new RandomLoot().addItemTag(ItemTags.BEDS, 2).addItemTag(ItemTags.BOATS, 2)
                .addItem(Items.FEATHER, 5).addItem(Items.BOOK, 5).addItem(Items.PAPER, 11)
                .addItemTag(itemTags.getTag(new ResourceLocation("forge", "glass")), 4)
                .addItemTag(itemTags.getTag(new ResourceLocation("forge", "glass_panes")), 4)
                .addItemTag(itemTags.getTag(new ResourceLocation("powder")), 6).addItem(Items.STRING, 7)
                .addItemTag(itemTags.getTag(new ResourceLocation("carpets")), 3)
                .addItemTag(itemTags.getTag(new ResourceLocation("wooden_trapdoors")), 8)
                .addItemTag(itemTags.getTag(new ResourceLocation("wooden_slabs")), 4)
                .addItemTag(itemTags.getTag(new ResourceLocation("wooden_stairs")), 7).addItem(Items.COOKED_PORKCHOP, 2)
                .addItem(Items.COOKED_BEEF, 2).addItem(Items.COOKED_CHICKEN, 3).addItem(Items.COOKED_MUTTON, 3)
                .addItem(Items.COOKED_COD, 3).addItem(Items.COOKED_SALMON, 3).addItem(Items.COOKIE, 10)
                .addItem(Items.APPLE, 5).addItem(Items.BAKED_POTATO, 4).addItem(Items.BREAD, 4)
                .addItem(Items.PUMPKIN_PIE, 4).addItem(Items.MUSHROOM_STEW, 1).addItem(Items.RABBIT_STEW, 1)
                .addItem(Items.NAME_TAG, 2).addItem(Items.LANTERN, 3).addItem(Items.REDSTONE_TORCH, 6)
                .build();

        randomBaseLoot = new RandomLoot().addItem(Items.LANTERN, 20).addItem(Items.SOUL_SAND, 6)
                .addItem(Items.IRON_INGOT, 30).addItem(Items.COBWEB, 8).addItem(Items.GOLD_INGOT, 16)
                .addItem(Items.SLIME_BLOCK, 8).addItem(Items.REDSTONE, 4).addItem(Items.HONEY_BLOCK, 8)
                .addItem(Items.PUMPKIN, 3).addItem(Items.TNT, 8).addItem(Items.SOUL_TORCH, 20).build();
    }

    @Override
    public boolean place(ISeedReader seedReader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        while (!seedReader.getBlockState(pos).isAir(seedReader, pos)) {
            pos = pos.above();
        }
        BlockPos mutable = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());

        ServerWorld serverWorld = seedReader.getLevel();
        TemplateManager templateManager = serverWorld.getStructureManager();
        //generate random civilian base
        if (rand.nextInt(80) == 1) {
            ResourceLocation randomResource = structureList.getRandom();
            Template template = templateManager.get(randomResource);

            TemplateWithRandomLoot generetableTemplate = new TemplateWithRandomLoot(template, randomBaseLoot, seedReader);
            generetableTemplate.placeInWorld(serverWorld, mutable, new PlacementSettings(), rand);
            return true;
        }

        //generate exit
        if (rand.nextInt(150) == 1) {
            GeneratableTemplate exit = new GeneratableTemplate(templateManager.get(new ResourceLocation(SCP.ID, "iikea/exit")), Collections.emptyList(), seedReader);
            exit.placeInWorld(serverWorld, mutable, new PlacementSettings(), rand);
            return true;
        }

        Block randomBrick = SCPBlocks.resistantBricks.get(rand.nextInt(16));
        final int floors = 3;
        final int floorHeight = 5;
        boolean makeWalls = rand.nextBoolean();
        boolean northWall = rand.nextBoolean();
        boolean westWall = rand.nextBoolean();
        boolean eastWall = rand.nextBoolean();
        boolean southWall = rand.nextBoolean();
        boolean barricade = rand.nextBoolean();
        //corners (and walls)
        for (int i = 0; i < floors * floorHeight; i++) {
            seedReader.setBlock(mutable.offset(0, i, 0), randomBrick.defaultBlockState(), 1);
            seedReader.setBlock(mutable.offset(15, i, 0), randomBrick.defaultBlockState(), 1);
            seedReader.setBlock(mutable.offset(15, i, 15), randomBrick.defaultBlockState(), 1);
            seedReader.setBlock(mutable.offset(0, i, 15), randomBrick.defaultBlockState(), 1);

            if (makeWalls) {
                if (northWall) {
                    for (int x = 0; x < 16; x++) {
                        seedReader.setBlock(mutable.offset(x, i, 0), randomBrick.defaultBlockState(), 1);
                    }
                    if (barricade) {
                        seedReader.setBlock(mutable.offset(7, i, 0), WOODS.getRandomElement(rand).defaultBlockState(), 1);
                        seedReader.setBlock(mutable.offset(8, i, 0), WOODS.getRandomElement(rand).defaultBlockState(), 2);
                    } else {
                        //opening in the middle
                        seedReader.removeBlock(mutable.offset(7, i, 0), false);
                        seedReader.removeBlock(mutable.offset(8, i, 0), false);
                    }
                }
                if (westWall) {
                    for (int z = 0; z < 16; z++) {
                        seedReader.setBlock(mutable.offset(0, i, z), randomBrick.defaultBlockState(), 1);
                    }
                    if (barricade) {
                        seedReader.setBlock(mutable.offset(0, i, 7), WOODS.getRandomElement(rand).defaultBlockState(), 1);
                        seedReader.setBlock(mutable.offset(0, i, 8), WOODS.getRandomElement(rand).defaultBlockState(), 1);
                    } else {
                        seedReader.removeBlock(mutable.offset(0, i, 7), false);
                        seedReader.removeBlock(mutable.offset(0, i, 8), false);
                    }
                }
                if (eastWall) {
                    for (int x = 0; x < 16; x++) {
                        seedReader.setBlock(mutable.offset(x, i, 15), randomBrick.defaultBlockState(), 1);
                    }
                    if (barricade) {
                        seedReader.setBlock(mutable.offset(7, i, 15), WOODS.getRandomElement(rand).defaultBlockState(), 1);
                        seedReader.setBlock(mutable.offset(8, i, 15), WOODS.getRandomElement(rand).defaultBlockState(), 1);
                    } else {
                        seedReader.removeBlock(mutable.offset(7, i, 15), false);
                        seedReader.removeBlock(mutable.offset(8, i, 15), false);
                    }
                }
                if (southWall) {
                    for (int z = 0; z < 16; z++) {
                        seedReader.setBlock(mutable.offset(15, i, z), randomBrick.defaultBlockState(), 1);
                    }
                    if (barricade) {
                        seedReader.setBlock(mutable.offset(15, i, 7), WOODS.getRandomElement(rand).defaultBlockState(), 1);
                        seedReader.setBlock(mutable.offset(15, i, 8), WOODS.getRandomElement(rand).defaultBlockState(), 1);
                    } else {
                        seedReader.removeBlock(mutable.offset(15, i, 7), false);
                        seedReader.removeBlock(mutable.offset(15, i, 8), false);
                    }
                }
            }
        }
        RandomizedList<BlockPos> positionsOfGoods = new RandomizedList<>(120);
        RandomizedList<BlockPos> centralPositions = new RandomizedList<>(16);
        //floors
        for (int floor = 0; floor < floors; floor++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    seedReader.setBlock(mutable.offset(x, floor * floorHeight, z), randomBrick.defaultBlockState(), 1);
                }
            }

            //goods corner 1
            if (rand.nextBoolean()) {
                for (int x = 3; x < 5; x++) {
                    for (int z = 3; z < 5; z++) {
                        BlockState defaultState = randomBlockTagList.getRandom().getRandomElement(rand).defaultBlockState();
                        for (int y = 0; y < 3; y++) {
                            BlockPos offset = mutable.offset(x, floor * (floorHeight) + y + 1, z);
                            seedReader.setBlock(offset, defaultState, 1);
                            positionsOfGoods.add(offset);
                        }
                    }
                }
            }

            //goods quarter1
            if (rand.nextBoolean()) {
                for (int x = 3; x < 5; x++) {
                    for (int z = 7; z < 9; z++) {
                        BlockState defaultState = randomBlockTagList.getRandom().getRandomElement(rand).defaultBlockState();
                        for (int y = 0; y < 3; y++) {
                            BlockPos offset = mutable.offset(x, floor * (floorHeight) + y + 1, z);
                            seedReader.setBlock(offset, defaultState, 1);
                            positionsOfGoods.add(offset);
                        }
                    }
                }
            }

            //goods corner 2
            if (rand.nextBoolean()) {
                for (int x = 7; x < 9; x++) {
                    for (int z = 11; z < 13; z++) {
                        BlockState defaultState = randomBlockTagList.getRandom().getRandomElement(rand).defaultBlockState();
                        for (int y = 0; y < 3; y++) {
                            BlockPos offset = mutable.offset(x, floor * (floorHeight) + y + 1, z);
                            seedReader.setBlock(offset, defaultState, 1);
                            positionsOfGoods.add(offset);
                        }
                    }
                }
            }

            //goods quarter 2
            if (rand.nextBoolean()) {
                for (int x = 7; x < 9; x++) {
                    for (int z = 3; z < 5; z++) {
                        BlockState defaultState = randomBlockTagList.getRandom().getRandomElement(rand).defaultBlockState();
                        for (int y = 0; y < 3; y++) {
                            BlockPos offset = mutable.offset(x, floor * (floorHeight) + y + 1, z);
                            seedReader.setBlock(offset, defaultState, 1);
                            positionsOfGoods.add(offset);
                        }
                    }
                }
            }

            //goods corner 3
            if (rand.nextBoolean()) {
                for (int x = 11; x < 13; x++) {
                    for (int z = 3; z < 5; z++) {
                        BlockState defaultState = randomBlockTagList.getRandom().getRandomElement(rand).defaultBlockState();
                        for (int y = 0; y < 3; y++) {
                            BlockPos offset = mutable.offset(x, floor * (floorHeight) + y + 1, z);
                            seedReader.setBlock(offset, defaultState, 1);
                            positionsOfGoods.add(offset);
                        }
                    }
                }
            }

            //goods quarter 3
            if (rand.nextBoolean()) {
                for (int x = 11; x < 13; x++) {
                    for (int z = 7; z < 9; z++) {
                        BlockState defaultState = randomBlockTagList.getRandom().getRandomElement(rand).defaultBlockState();
                        for (int y = 0; y < 3; y++) {
                            BlockPos offset = mutable.offset(x, floor * (floorHeight) + y + 1, z);
                            seedReader.setBlock(offset, defaultState, 1);
                            positionsOfGoods.add(offset);
                        }
                    }
                }
            }

            //goods corner 4
            if (rand.nextBoolean()) {
                for (int x = 11; x < 13; x++) {
                    for (int z = 11; z < 13; z++) {
                        BlockState defaultState = randomBlockTagList.getRandom().getRandomElement(rand).defaultBlockState();
                        for (int y = 0; y < 3; y++) {
                            BlockPos offset = mutable.offset(x, floor * (floorHeight) + y + 1, z);
                            seedReader.setBlock(offset, defaultState, 1);
                            positionsOfGoods.add(offset);
                        }
                    }
                }
            }

            //goods quarter 4
            if (rand.nextBoolean()) {
                for (int x = 3; x < 5; x++) {
                    for (int z = 11; z < 13; z++) {
                        BlockState defaultState = randomBlockTagList.getRandom().getRandomElement(rand).defaultBlockState();
                        for (int y = 0; y < 3; y++) {
                            BlockPos offset = mutable.offset(x, floor * (floorHeight) + y + 1, z);
                            seedReader.setBlock(offset, defaultState, 1);
                            positionsOfGoods.add(offset);
                        }
                    }
                }
            }

            //center goods
            if (rand.nextBoolean()) {
                for (int i = 7; i < 9; i++) {
                    for (int j = 7; j < 9; j++) {
                        BlockState blockState = randomBlockTagList.getRandom().getRandomElement(rand).defaultBlockState();
                        for (int k = 0; k < 3; k++) {
                            BlockPos offset = mutable.offset(i, floor * (floorHeight) + k + 1, j);
                            seedReader.setBlock(offset, blockState, 1);
                            positionsOfGoods.add(offset);
                            centralPositions.add(offset);
                        }
                    }
                }
            }

            for (int i = 0; i < 3; i++) {
                BlockPos randomReplaced = positionsOfGoods.removeRandom();
                if (randomReplaced != null) {
                    seedReader.setBlock(randomReplaced, containers.getRandomElement(rand).defaultBlockState(), 1);
                    TileEntity tileEntity = seedReader.getBlockEntity(randomReplaced);
                    if (tileEntity != null)
                        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> randomAmbientLoot.generateInto(itemHandler));
                }
            }

            //place randomly an armor stand
            if (!seedReader.isClientSide() && centralPositions.size() > 0 && rand.nextBoolean()) {
                RandomizedList<BlockPos> randomizedList = new RandomizedList<>(BlockPos.betweenClosedStream(new AxisAlignedBB(centralPositions.get(0), centralPositions.get(centralPositions.size() - 1)).expandTowards(2, 0, 1)).filter(seedReader::isEmptyBlock).collect(Collectors.toList()));
                BlockPos blockPos = randomizedList.getRandom();
                ArmorStandEntity armorStandEntity = EntityType.ARMOR_STAND.create(seedReader.getLevel());
                armorStandEntity.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, rand.nextInt(360), 0);
                if (rand.nextBoolean())
                    armorStandEntity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(helmets.getRandomElement(rand)));
                if (rand.nextBoolean())
                    armorStandEntity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(chestplates.getRandomElement(rand)));
                if (rand.nextBoolean())
                    armorStandEntity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(leggings.getRandomElement(rand)));
                if (rand.nextBoolean())
                    armorStandEntity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(boots.getRandomElement(rand)));
                seedReader.addFreshEntity(armorStandEntity);
            }

            //lamps
            BlockPos pos9 = mutable.offset(1, (floor) * (floorHeight) - 1, 1);
            BlockState switchableLamp = SCPBlocks.switchableLamp.defaultBlockState().setValue(SmallLamp.FACING, Direction.DOWN);
            seedReader.setBlock(pos9, switchableLamp, 1);
            seedReader.getBlockTicks().scheduleTick(pos9, SCPBlocks.switchableLamp, 20);
            BlockPos pos10 = mutable.offset(1, (floor) * (floorHeight) - 1, 14);
            seedReader.setBlock(pos10, switchableLamp, 1);
            seedReader.getBlockTicks().scheduleTick(pos10, SCPBlocks.switchableLamp, 20);
            BlockPos pos11 = mutable.offset(14, (floor) * (floorHeight) - 1, 1);
            seedReader.setBlock(pos11, switchableLamp, 1);
            seedReader.getBlockTicks().scheduleTick(pos11, SCPBlocks.switchableLamp, 20);
            BlockPos pos12 = mutable.offset(14, (floor) * (floorHeight) - 1, 14);
            seedReader.setBlock(pos12, switchableLamp, 1);
            seedReader.getBlockTicks().scheduleTick(pos12, SCPBlocks.switchableLamp, 20);

            BlockPos pos13 = mutable.offset(4, (floor) * (floorHeight) - 1, 4);
            seedReader.setBlock(pos13, switchableLamp, 1);
            seedReader.getBlockTicks().scheduleTick(pos13, SCPBlocks.switchableLamp, 20);
            BlockPos pos14 = mutable.offset(4, (floor) * (floorHeight) - 1, 11);
            seedReader.setBlock(pos14, switchableLamp, 1);
            seedReader.getBlockTicks().scheduleTick(pos14, SCPBlocks.switchableLamp, 20);
            BlockPos pos15 = mutable.offset(11, (floor) * (floorHeight) - 1, 4);
            seedReader.setBlock(pos15, switchableLamp, 1);
            seedReader.getBlockTicks().scheduleTick(pos15, SCPBlocks.switchableLamp, 20);
            BlockPos pos16 = mutable.offset(11, (floor) * (floorHeight) - 1, 11);
            seedReader.setBlock(pos16, switchableLamp, 1);
            seedReader.getBlockTicks().scheduleTick(pos16, SCPBlocks.switchableLamp, 20);

            //roof
            if (floor == floors - 1) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        seedReader.setBlock(mutable.offset(x, floorHeight * floors, z), randomBrick.defaultBlockState(), 1);
                    }
                }
                //support bars
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        seedReader.setBlock(mutable.offset(x, (floors) * floorHeight - 1, z), SCPBlocks.supportBlock.defaultBlockState().setValue(SupportBlock.DOWN, false).setValue(SupportBlock.EAST, true).setValue(SupportBlock.WEST, true).setValue(SupportBlock.SOUTH, true).setValue(SupportBlock.NORTH, true).setValue(SupportBlock.UP, true), 1);
                    }
                }
                //lamps

                BlockPos pos1 = mutable.offset(1, (floors) * (floorHeight) - 1, 1);
                seedReader.setBlock(pos1, switchableLamp, 1);
                seedReader.getBlockTicks().scheduleTick(pos1, SCPBlocks.switchableLamp, 20);
                BlockPos pos2 = mutable.offset(1, (floors) * (floorHeight) - 1, 14);
                seedReader.setBlock(pos2, switchableLamp, 1);
                seedReader.getBlockTicks().scheduleTick(pos2, SCPBlocks.switchableLamp, 20);
                BlockPos pos3 = mutable.offset(14, (floors) * (floorHeight) - 1, 1);
                seedReader.setBlock(pos3, switchableLamp, 1);
                seedReader.getBlockTicks().scheduleTick(pos3, SCPBlocks.switchableLamp, 20);
                BlockPos pos4 = mutable.offset(14, (floors) * (floorHeight) - 1, 14);
                seedReader.setBlock(pos4, switchableLamp, 1);
                seedReader.getBlockTicks().scheduleTick(pos4, SCPBlocks.switchableLamp, 20);

                BlockPos pos5 = mutable.offset(4, (floors) * (floorHeight) - 1, 4);
                seedReader.setBlock(pos5, switchableLamp, 1);
                seedReader.getBlockTicks().scheduleTick(pos5, SCPBlocks.switchableLamp, 20);
                BlockPos pos6 = mutable.offset(4, (floors) * (floorHeight) - 1, 11);
                seedReader.setBlock(pos6, switchableLamp, 1);
                seedReader.getBlockTicks().scheduleTick(pos6, SCPBlocks.switchableLamp, 20);
                BlockPos pos7 = mutable.offset(11, (floors) * (floorHeight) - 1, 4);
                seedReader.setBlock(pos7, switchableLamp, 1);
                seedReader.getBlockTicks().scheduleTick(pos7, SCPBlocks.switchableLamp, 20);
                BlockPos pos8 = mutable.offset(11, (floors) * (floorHeight) - 1, 11);
                seedReader.setBlock(pos8, switchableLamp, 1);
                seedReader.getBlockTicks().scheduleTick(pos8, SCPBlocks.switchableLamp, 20);

            }
        }

        //stairs
        if (!northWall && rand.nextBoolean()) {
            BlockPos blockPos = mutable.offset(4, 0, 0);
            for (int i = 0; i < floorHeight; i++) {
                blockPos = blockPos.offset(1, 1, 0);
                seedReader.setBlock(blockPos, randomBrick.defaultBlockState(), 1);
            }
            seedReader.removeBlock(blockPos.offset(-1, 0, 0), false);
            seedReader.removeBlock(blockPos.offset(-2, 0, 0), false);
            seedReader.removeBlock(blockPos.offset(-3, 0, 0), false);

            blockPos = blockPos.offset(-5, 0, 0);
            for (int i = 0; i < floorHeight; i++) {
                blockPos = blockPos.offset(1, 1, 0);
                seedReader.setBlock(blockPos, randomBrick.defaultBlockState(), 1);
            }
            seedReader.removeBlock(blockPos.offset(-1, 0, 0), false);
            seedReader.removeBlock(blockPos.offset(-2, 0, 0), false);
            seedReader.removeBlock(blockPos.offset(-3, 0, 0), false);
        }
        if (!westWall && rand.nextBoolean()) {
            BlockPos blockPos = mutable.offset(0, 0, 4);
            for (int f = 0; f < floorHeight; f++) {
                blockPos = blockPos.offset(0, 1, 1);
                seedReader.setBlock(blockPos, randomBrick.defaultBlockState(), 1);
            }
            seedReader.removeBlock(blockPos.offset(0, 0, -1), false);
            seedReader.removeBlock(blockPos.offset(0, 0, -2), false);
            seedReader.removeBlock(blockPos.offset(0, 0, -3), false);

            blockPos = blockPos.offset(0, 0, -5);
            for (int f = 0; f < floorHeight; f++) {
                blockPos = blockPos.offset(0, 1, 1);
                seedReader.setBlock(blockPos, randomBrick.defaultBlockState(), 1);
            }
            seedReader.removeBlock(blockPos.offset(0, 0, -1), false);
            seedReader.removeBlock(blockPos.offset(0, 0, -2), false);
            seedReader.removeBlock(blockPos.offset(0, 0, -3), false);
        }

        if (!eastWall && rand.nextBoolean()) {
            BlockPos blockPos = mutable.offset(4, 0, 15);
            for (int i = 0; i < floorHeight; i++) {
                blockPos = blockPos.offset(1, 1, 0);
                seedReader.setBlock(blockPos, randomBrick.defaultBlockState(), 1);
            }
            seedReader.removeBlock(blockPos.offset(-1, 0, 0), false);
            seedReader.removeBlock(blockPos.offset(-2, 0, 0), false);
            seedReader.removeBlock(blockPos.offset(-3, 0, 0), false);

            blockPos = blockPos.offset(-5, 0, 0);
            for (int i = 0; i < floorHeight; i++) {
                blockPos = blockPos.offset(1, 1, 0);
                seedReader.setBlock(blockPos, randomBrick.defaultBlockState(), 1);
            }
            seedReader.removeBlock(blockPos.offset(-1, 0, 0), false);
            seedReader.removeBlock(blockPos.offset(-2, 0, 0), false);
            seedReader.removeBlock(blockPos.offset(-3, 0, 0), false);
        }

        if (!southWall && rand.nextBoolean()) {
            BlockPos blockPos = mutable.offset(15, 0, 4);
            for (int f = 0; f < floorHeight; f++) {
                blockPos = blockPos.offset(0, 1, 1);
                seedReader.setBlock(blockPos, randomBrick.defaultBlockState(), 1);
            }
            seedReader.removeBlock(blockPos.offset(0, 0, -1), false);
            seedReader.removeBlock(blockPos.offset(0, 0, -2), false);
            seedReader.removeBlock(blockPos.offset(0, 0, -3), false);

            blockPos = blockPos.offset(0, 0, -5);
            for (int f = 0; f < floorHeight; f++) {
                blockPos = blockPos.offset(0, 1, 1);
                seedReader.setBlock(blockPos, randomBrick.defaultBlockState(), 1);
            }
            seedReader.removeBlock(blockPos.offset(0, 0, -1), false);
            seedReader.removeBlock(blockPos.offset(0, 0, -2), false);
            seedReader.removeBlock(blockPos.offset(0, 0, -3), false);
        }
        return true;
    }

}
