package dev.buildtool.scp.registration;

import dev.buildtool.satako.blocks.BlockDirectional;
import dev.buildtool.scp.PreviewBlock;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.blockscps.*;
import dev.buildtool.scp.chairs.Chair;
import dev.buildtool.scp.clockworks.Panel;
import dev.buildtool.scp.crate.CrateBlock;
import dev.buildtool.scp.flaregun.Crate;
import dev.buildtool.scp.harddrivecracker.HardDriveCrackerBlock;
import dev.buildtool.scp.harddrivecracker.HardDriveStore;
import dev.buildtool.scp.human.HumanHireBlock;
import dev.buildtool.scp.infiniteikea.EntranceBlock;
import dev.buildtool.scp.infiniteikea.ExitBlock;
import dev.buildtool.scp.infiniteikea.SupportBlock;
import dev.buildtool.scp.lamp.SmallLamp;
import dev.buildtool.scp.lamp.SwitchableLamp;
import dev.buildtool.scp.lock.ElectronicLock;
import dev.buildtool.scp.lootblock.LootBlock;
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
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Arrays;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCPBlocks {
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
    public static ArrayList<SlabBlock> brickSlabs = new ArrayList<>(16);
    public static ArrayList<Block> solidColorBlocks = new ArrayList<>(16);
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
    public static Crate crate;

    public static HardDriveCrackerBlock hardDriveCrackerBlock;
    public static HardDriveStore hardDriveStore;

    public static PreviewBlock previewBlock;

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
        registerBlock(pizzaBox = new PizzaBox(AbstractBlock.Properties.of(Material.CAKE, MaterialColor.COLOR_YELLOW).noOcclusion()), "pizza_box", forgeRegistry);
        registerBlock(cactus = new BlastingCactus(AbstractBlock.Properties.of(Material.CACTUS)), "cactus_mine", forgeRegistry);
        registerBlock(fertileSoil = new FertileSoil(propertiesOf(Material.DIRT, ToolType.SHOVEL).sound(SoundType.GRASS).randomTicks()), "fertile_soil", forgeRegistry);
        registerBlock(redIce = new RedIce(AbstractBlock.Properties.of(Material.ICE).noOcclusion().sound(SoundType.GLASS)), "red_ice", forgeRegistry);

        Arrays.stream(DyeColor.values()).forEach(color -> {
            resistantGlass.add(registerBlock(new StainedGlassBlock(color, properties().noOcclusion()), color.getName() + "_glass", forgeRegistry));
            resistantBricks.add(registerBlock(new Block(properties()), color.getName() + "_bricks", forgeRegistry));
            solidColorBlocks.add(registerBlock(new Block(properties()), color.getName() + "_solid", forgeRegistry));
            coloredPipes.add(registerBlock(new RotatedPillarBlock(properties().noOcclusion()), color.getName() + "_pipe", forgeRegistry));
        });

        slidingDoorBlock = registerBlock(new SlidingDoorBlock(properties().noOcclusion()), "sliding_door", forgeRegistry);
        clockworksPanel = registerBlock(new Panel(properties()), "clockworks_panel", forgeRegistry);
        clockworksChamber = registerBlock(new Block(properties()), "chamber_block", forgeRegistry);
        clockworksBase = registerBlock(new Block(properties()), "clockwork_base", forgeRegistry);
        electronicLock = registerBlock(new ElectronicLock(properties().noOcclusion()), "lock", forgeRegistry);
        crateBlock = registerBlock(new CrateBlock(properties()), "crate", forgeRegistry);
        copperTube = registerBlock(new BlockDirectional(properties().noOcclusion()), "copper_tube", forgeRegistry);
        acaciaShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "acacia_shelf", forgeRegistry);
        birchShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "birch_shelf", forgeRegistry);
        jungleShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "jungle_shelf", forgeRegistry);
        oakShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "oak_shelf", forgeRegistry);
        darkOakShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "dark_oak_shelf", forgeRegistry);
        spruceShelf = registerBlock(new ShelfBlock(propertiesOf(Material.WOOD, ToolType.AXE).noOcclusion().sound(SoundType.WOOD)), "spruce_shelf", forgeRegistry);
        singleItemTable = registerBlock(new Table(properties().noOcclusion()), "one_item_table", forgeRegistry);
        chair = registerBlock(new Chair(properties().noOcclusion()), "chair", forgeRegistry);
        smallLamp = registerBlock(new SmallLamp(properties().strength(1.5f, 50).noCollission().lightLevel(value -> 15)), "small_lamp", forgeRegistry);
        fourItemTable = registerBlock(new Table4(properties().noOcclusion()), "four_item_table", forgeRegistry);
        monsterPot = registerBlock(new MonsterPot(properties().noOcclusion()), "monster_pot", forgeRegistry);
        holeInTheWall = registerBlock(new HoleInTheWall(properties()), "hole_in_the_wall", forgeRegistry);
        holeToAnotherPlace = registerBlock(new HoleToAnotherPlace(properties()), "hole_to_another_place", forgeRegistry);
        supportBlock = registerBlock(new SupportBlock(1 / 16f, properties()), "support_bar", forgeRegistry);
        buildingBlock1 = registerBlock(new Block(properties()), "building1", forgeRegistry);
        buildingBlock2 = registerBlock(new Block(properties()), "building2", forgeRegistry);
        buildingBlock3 = registerBlock(new BlockDirectional(properties()), "building3", forgeRegistry);
        scpSlab1 = registerBlock(new SlabBlock(properties()), "building1_slab", forgeRegistry);
        scpSlab2 = registerBlock(new SlabBlock(properties()), "building2_slab", forgeRegistry);
        scpSlab3 = registerBlock(new SlabBlock(properties()), "building3_slab", forgeRegistry);
        resistantBricks.forEach(block -> brickSlabs.add(registerBlock(new SlabBlock(properties()), block.getRegistryName().getPath() + "_slab", forgeRegistry)));
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
        repeatingBomb = registerBlock(new Bomb(properties().strength(3, Integer.MAX_VALUE).noOcclusion()), "repeating_bomb", forgeRegistry);
        mailbox = registerBlock(new Mailbox(properties().noOcclusion()), "scp3821", forgeRegistry);
        parcelBlock = registerBlock(new ParcelBlock(properties().strength(-1)), "parcel", forgeRegistry);
        crate = registerBlock(new Crate(propertiesOf(Material.WOOD, ToolType.AXE)), "wooden_crate", forgeRegistry);

        hardDriveCrackerBlock = registerBlock(new HardDriveCrackerBlock(properties()), "hard_drive_cracker", forgeRegistry);
        hardDriveStore = registerBlock(new HardDriveStore(properties()), "hard_drive_store", forgeRegistry);

        previewBlock = registerBlock(new PreviewBlock(properties().noCollission().noOcclusion().noDrops().isViewBlocking((p_test_1_, p_test_2_, p_test_3_) -> false)), "preview_block", forgeRegistry);
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

        forgeRegistry.register(registerSCP(repeatingBomb));
        forgeRegistry.register(registerSCP(mailbox));
        forgeRegistry.register(register(parcelBlock));
        forgeRegistry.register(register(crate));
        forgeRegistry.register(register(hardDriveCrackerBlock));
    }

    private static Item register(Block block) {
        assert block.getRegistryName() != null;
        return new BlockItem(block, itemProperties()).setRegistryName(block.getRegistryName());
    }

    private static Item registerSCP(Block block) {
        assert block.getRegistryName() != null;
        return new BlockItem(block, itemProperties().tab(Entities.SCPs).rarity(Rarity.RARE)).setRegistryName(block.getRegistryName());
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
        return block;
    }
}
