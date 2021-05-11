package dev.buildtool.scp.events;

import dev.buildtool.satako.blocks.BlockDirectional;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.blocks.*;
import dev.buildtool.scp.chairs.Chair;
import dev.buildtool.scp.clockworks.Panel;
import dev.buildtool.scp.crate.CrateBlock;
import dev.buildtool.scp.human.HumanHireBlock;
import dev.buildtool.scp.infiniteikea.EntranceBlock;
import dev.buildtool.scp.infiniteikea.ExitBlock;
import dev.buildtool.scp.infiniteikea.SupportBlock;
import dev.buildtool.scp.lamp.SmallLamp;
import dev.buildtool.scp.lamp.SwitchableLamp;
import dev.buildtool.scp.lock.ElectronicLock;
import dev.buildtool.scp.lootblock.LootBlock;
import dev.buildtool.scp.lootblock.LootBlockEntity;
import dev.buildtool.scp.mailbox.Mailbox;
import dev.buildtool.scp.mailbox.ParcelBlock;
import dev.buildtool.scp.monsterpot.MonsterPot;
import dev.buildtool.scp.oldai.OldAIBlock;
import dev.buildtool.scp.pipenightmare.Explodable;
import dev.buildtool.scp.pipenightmare.Pipe;
import dev.buildtool.scp.repeatingbomb.Bomb;
import dev.buildtool.scp.shelf.ShelfBlock;
import dev.buildtool.scp.slidingdoor.SlidingDoorBlock;
import dev.buildtool.scp.table.Table;
import dev.buildtool.scp.table.Table4;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Arrays;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCPBlocks {
    static ArrayList<Block> droppableBlocks = new ArrayList<>();
    static PizzaBox pizzaBox;
    static BlastingCactus cactus;
    static FertileSoil fertileSoil;
    public static RedIce redIce;
    public static SlidingDoorBlock slidingDoorBlock;
    static Panel clockworksPanel;
    static Block clockworksBase;
    static Block clockworksChamber;
    static ElectronicLock electronicLock;
    public static ArrayList<Block> resistantGlass = new ArrayList<>(16);
    public static ArrayList<Block> resistantBricks = new ArrayList<>(16);
    static ArrayList<SlabBlock> brickSlabs = new ArrayList<>(16);
    static ArrayList<Block> solidColorBlocks = new ArrayList<>(16);
    public static ArrayList<RotatedPillarBlock> coloredPipes = new ArrayList<>(16);
    public static CrateBlock crateBlock;
    public static Block copperTube, cinderblock;
    static Block buildingBlock1, buildingBlock2, buildingBlock3;
    public static ShelfBlock oakShelf, birchShelf, acaciaShelf, spruceShelf, darkOakShelf, jungleShelf;
    public static Table singleItemTable, fourItemTable;
    static Chair chair;
    public static SmallLamp smallLamp;
    public static SwitchableLamp switchableLamp;
    static MonsterPot monsterPot;
    static HoleInTheWall holeInTheWall;
    public static SupportBlock supportBlock;
    static HoleToAnotherPlace holeToAnotherPlace;
    static SlabBlock scpSlab1, scpSlab2, scpSlab3;
    static public Block invisibleLight;
    public static EntranceBlock iikeaEntrance;
    public static ExitBlock iikeaExit;
    public static HumanHireBlock hireBlock;
    public static OldAIBlock oldAIBlock;
    public static dev.buildtool.scp.shelflife.ShelfBlock shelfLifeBlock;
    public static ContagiousCrystal contagiousCrystal;

    public static Pipe thinPipe;
    public static Pipe thickPipe;
    public static Pipe mediumPipe;
    public static Explodable vent;
    public static Explodable boiler;

    public static LootBlock lootBlock;
    static Bomb repeatingBomb;
    static Mailbox mailbox;
    public static ParcelBlock parcelBlock;

    static ItemGroup blocks = new ItemGroup("scp.blocks") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(copperTube);
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> p_78018_1_) {
            super.fillItemList(p_78018_1_);
            p_78018_1_.add(new ItemStack(Items.STRUCTURE_BLOCK));
        }
    };

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> register)
    {
        IForgeRegistry<Block> forgeRegistry = register.getRegistry();
        forgeRegistry.register(register(pizzaBox = new PizzaBox(AbstractBlock.Properties.of(Material.CAKE, MaterialColor.COLOR_YELLOW).noOcclusion()), "pizza_box"));
        forgeRegistry.register(register(cactus = new BlastingCactus(AbstractBlock.Properties.of(Material.CACTUS)), "cactus_mine"));
        registerBlock(fertileSoil = new FertileSoil(propertiesOf(Material.DIRT, ToolType.SHOVEL).sound(SoundType.GRASS).randomTicks()), "fertile_soil", forgeRegistry);
        forgeRegistry.register(register(redIce = new RedIce(AbstractBlock.Properties.of(Material.ICE).noOcclusion().sound(SoundType.GLASS)), "red_ice"));

        Arrays.stream(DyeColor.values()).forEach(color -> {
            resistantGlass.add(register(new StainedGlassBlock(color, properties().noOcclusion()), color.getName() + "_glass"));
            resistantBricks.add(register(new Block(properties()), color.getName() + "_bricks"));
            solidColorBlocks.add(register(new Block(properties()), color.getName() + "_solid"));
            coloredPipes.add(register(new RotatedPillarBlock(properties().noOcclusion()), color.getName() + "_pipe"));
        });

        resistantBricks.forEach(forgeRegistry::register);
        resistantGlass.forEach(forgeRegistry::register);
        solidColorBlocks.forEach(forgeRegistry::register);
        coloredPipes.forEach(forgeRegistry::register);

        slidingDoorBlock = register(new SlidingDoorBlock(properties().noOcclusion()), "sliding_door");
        clockworksPanel = register(new Panel(properties()), "clockworks_panel");
        clockworksChamber = register(new Block(properties()), "chamber_block");
        clockworksBase = register(new Block(properties()), "clockwork_base");
        forgeRegistry.registerAll(slidingDoorBlock, clockworksPanel, clockworksBase, clockworksChamber);
        electronicLock = register(new ElectronicLock(properties().noOcclusion()), "lock");
        crateBlock = register(new CrateBlock(properties()), "crate");
        copperTube = register(new BlockDirectional(properties().noOcclusion()), "copper_tube");
        forgeRegistry.registerAll(electronicLock, crateBlock, copperTube);
        acaciaShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "acacia_shelf", forgeRegistry);
        birchShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "birch_shelf", forgeRegistry);
        jungleShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "jungle_shelf", forgeRegistry);
        oakShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "oak_shelf", forgeRegistry);
        darkOakShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "dark_oak_shelf", forgeRegistry);
        spruceShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "spruce_shelf", forgeRegistry);
        singleItemTable = register(new Table(properties().noOcclusion()), "one_item_table");
        chair = register(new Chair(properties().noOcclusion()), "chair");
        smallLamp = register(new SmallLamp(properties().strength(1.5f, 50).noCollission().lightLevel(value -> 15)), "small_lamp");
        fourItemTable = register(new Table4(properties().noOcclusion()), "four_item_table");
        forgeRegistry.registerAll(singleItemTable, chair, smallLamp, fourItemTable);
        monsterPot = register(new MonsterPot(properties().noOcclusion()), "monster_pot");
        holeInTheWall = register(new HoleInTheWall(properties()), "hole_in_the_wall");
        forgeRegistry.registerAll(monsterPot, holeInTheWall);
        holeToAnotherPlace = register(new HoleToAnotherPlace(properties()), "hole_to_another_place");
        supportBlock = register(new SupportBlock(1 / 16f, properties()), "support_bar");
        forgeRegistry.registerAll(supportBlock, holeToAnotherPlace);
        buildingBlock1 = register(new Block(properties()), "building1");
        buildingBlock2 = register(new Block(properties()), "building2");
        buildingBlock3 = register(new BlockDirectional(properties()), "building3");
        scpSlab1 = register(new SlabBlock(properties()), "building1_slab");
        scpSlab2 = register(new SlabBlock(properties()), "building2_slab");
        scpSlab3 = register(new SlabBlock(properties()), "building3_slab");
        resistantBricks.forEach(block -> brickSlabs.add(registerBlock(new SlabBlock(properties()), block.getRegistryName().getPath() + "_slab", forgeRegistry)));
        forgeRegistry.registerAll(buildingBlock1, buildingBlock2, scpSlab1, scpSlab2);
        forgeRegistry.registerAll(buildingBlock3, scpSlab3);
        cinderblock = registerBlock(new Block(properties()), "cinderblock", forgeRegistry);
        invisibleLight = registerBlock(new AirBlock(AbstractBlock.Properties.copy(Blocks.AIR).lightLevel(value -> 15).noDrops()), "light", forgeRegistry);

        iikeaExit = registerBlock(new ExitBlock(properties()), "ikea_exit", forgeRegistry);
        iikeaEntrance = registerBlock(new EntranceBlock(properties()), "ikea_entrance", forgeRegistry);
        switchableLamp = registerBlock(new SwitchableLamp(properties().strength(1.5f, 50).noCollission().lightLevel(value -> value.getValue(SwitchableLamp.on) ? 15 : 0)), "switchable_lamp", forgeRegistry);
        hireBlock = registerBlock(new HumanHireBlock(properties()), "hire_block", forgeRegistry);
        oldAIBlock = registerBlock(new OldAIBlock(properties().noOcclusion()), "old_ai", forgeRegistry);
        shelfLifeBlock = registerBlock(new dev.buildtool.scp.shelflife.ShelfBlock(properties().noOcclusion().sound(SoundType.WOOD).harvestTool(ToolType.AXE)), "shelf_life_block", forgeRegistry);
        contagiousCrystal = registerBlock(new ContagiousCrystal(properties()), "cont_crystal", forgeRegistry);

        //SCP-015; bone, wood, steel, pressed ash, human flesh, glass, and granite nightmare pipes
        thinPipe = registerBlock(new Pipe(1 / 16f, propertiesOf(Material.GLASS, ToolType.PICKAXE).strength(3)), "thin_pipe", forgeRegistry);
        mediumPipe = registerBlock(new Pipe(2 / 16f, propertiesOf(Material.GLASS, ToolType.PICKAXE).strength(3)), "medium_pipe", forgeRegistry);
        thickPipe = registerBlock(new Pipe(4 / 16f, propertiesOf(Material.GLASS, ToolType.PICKAXE).strength(3)), "thick_pipe", forgeRegistry);
        vent = registerBlock(new Explodable(propertiesOf(Material.METAL, ToolType.PICKAXE).strength(3), 3), "vent", forgeRegistry);
        boiler = registerBlock(new Explodable(propertiesOf(Material.HEAVY_METAL, ToolType.PICKAXE).strength(3), 4), "boiler", forgeRegistry);
        lootBlock = registerBlock(new LootBlock(AbstractBlock.Properties.of(Material.METAL)), "loot_block", forgeRegistry);
        repeatingBomb = registerBlock(new Bomb(properties().strength(3, Integer.MAX_VALUE).noOcclusion()), "repeating_bomb", forgeRegistry);
        mailbox = registerBlock(new Mailbox(properties().noOcclusion()), "scp3821", forgeRegistry);
        parcelBlock = registerBlock(new ParcelBlock(properties()), "parcel", forgeRegistry);
    }


    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> itemRegister)
    {
        IForgeRegistry<Item> forgeRegistry= itemRegister.getRegistry();
        forgeRegistry.registerAll(registerSCP(pizzaBox),registerSCP(cactus),registerSCP(fertileSoil),registerSCP(redIce));
        resistantGlass.forEach(block -> forgeRegistry.register(register(block)));
        resistantBricks.forEach(block -> forgeRegistry.register(register(block)));
        forgeRegistry.registerAll(register(slidingDoorBlock),registerSCP(clockworksPanel));
        forgeRegistry.registerAll(register(clockworksBase),register(clockworksChamber));
        forgeRegistry.registerAll(register(electronicLock),register(crateBlock),register(copperTube));
        forgeRegistry.registerAll(register(acaciaShelf),register(birchShelf),register(darkOakShelf),register(jungleShelf),register(oakShelf),register(spruceShelf));
        forgeRegistry.registerAll(register(singleItemTable), register(chair), register(smallLamp));
        forgeRegistry.registerAll(register(fourItemTable), registerSCP(monsterPot));
        forgeRegistry.registerAll(registerSCP(holeInTheWall));
        forgeRegistry.registerAll(register(buildingBlock1), register(buildingBlock2));
        forgeRegistry.registerAll(register(scpSlab1), register(scpSlab2));
        forgeRegistry.registerAll(register(buildingBlock3), register(scpSlab3));
        forgeRegistry.registerAll(register(supportBlock), registerSCP(holeToAnotherPlace));
        solidColorBlocks.forEach(block -> forgeRegistry.register(register(block)));
        forgeRegistry.registerAll(register(cinderblock));
        brickSlabs.forEach(slabBlock -> forgeRegistry.register(register(slabBlock)));
        forgeRegistry.register(registerSCP(iikeaEntrance));
        forgeRegistry.register(registerSCP(iikeaExit));
        forgeRegistry.register(register(switchableLamp));
        forgeRegistry.register(new BlockItem(hireBlock, itemProperties().stacksTo(1)).setRegistryName(hireBlock.getRegistryName()));

        forgeRegistry.register(registerSCP(shelfLifeBlock));
        coloredPipes.forEach(rotatedPillarBlock -> forgeRegistry.register(register(rotatedPillarBlock)));
        forgeRegistry.register(registerSCP(contagiousCrystal));

        forgeRegistry.register(registerSCP(thinPipe));
        forgeRegistry.register(registerSCP(mediumPipe));
        forgeRegistry.register(registerSCP(thickPipe));
        forgeRegistry.register(registerSCP(vent));
        forgeRegistry.register(registerSCP(boiler));

        forgeRegistry.register(registerSCP(oldAIBlock));

        forgeRegistry.register(new BlockItem(lootBlock, itemProperties()) {
            @Override
            public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
                BlockPos target = context.getClickedPos();
                World world = context.getLevel();
                BlockState blockState = world.getBlockState(target);
                int blockid = Block.getId(blockState);
                world.setBlockAndUpdate(target, lootBlock.defaultBlockState());
                LootBlockEntity lootBlockEntity = (LootBlockEntity) world.getBlockEntity(target);
                lootBlockEntity.storedBlockstate = blockid;
                lootBlockEntity.setChanged();
                return ActionResultType.SUCCESS;
            }
        }.setRegistryName(lootBlock.getRegistryName()));

        forgeRegistry.register(registerSCP(repeatingBomb));
        forgeRegistry.register(registerSCP(mailbox));
        forgeRegistry.register(register(parcelBlock));
    }

    private static Item register(Block block) {
        assert block.getRegistryName() != null;
        return new BlockItem(block, itemProperties()).setRegistryName(block.getRegistryName());
    }

    private static Item registerSCP(Block block) {
        assert block.getRegistryName() != null;
        return new BlockItem(block, itemProperties().tab(Entities.SCPs)).setRegistryName(block.getRegistryName());
    }

    private static Item registerSCPWithProperties(Block block, Item.Properties properties) {
        assert block.getRegistryName() != null;
        return new BlockItem(block, properties.tab(Entities.SCPs)).setRegistryName(block.getRegistryName());
    }

    private static Item.Properties itemProperties() {
        return new Item.Properties().tab(blocks).stacksTo(64);
    }

    static AbstractBlock.Properties properties() {
        return AbstractBlock.Properties.of(Material.STONE).strength(3, 50).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE);
    }

    static AbstractBlock.Properties propertiesOf(Material material, ToolType harvestTool) {
        return AbstractBlock.Properties.of(material).strength(3, 50).requiresCorrectToolForDrops().harvestTool(harvestTool);
    }

    private static <B extends Block> B registerBlock(B block, String name, IForgeRegistry<Block> registry) {
        block.setRegistryName(SCP.ID, name);
        registry.register(block);
        droppableBlocks.add(block);
        return block;
    }

    @Deprecated
    private static <B extends Block> B register(B block, String id) {
        block.setRegistryName(SCP.ID, id);
        return block;
    }
}
