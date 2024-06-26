package dev.buildtool.scp.registration;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.Template2;
import dev.buildtool.scp.harddrivecracker.HardDrive;
import dev.buildtool.scp.items.Analyzer;
import dev.buildtool.scp.items.StasisCage;
import dev.buildtool.scp.lock.KeyCard;
import dev.buildtool.scp.scps.displacingflashlight.Flashlight;
import dev.buildtool.scp.scps.flaregun.FlareGun;
import dev.buildtool.scp.scps.itemscps.*;
import dev.buildtool.scp.scps.swatarmor.PoliceBaton;
import dev.buildtool.scp.weapons.AutoRifle;
import dev.buildtool.scp.weapons.FlakCannon;
import dev.buildtool.scp.weapons.FlameThrower;
import dev.buildtool.scp.weapons.RocketLauncher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCPItems {
    public static ItemGroup items = new ItemGroup("scp.items") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(pizzaSlice);
        }
    };

    public static ItemGroup hardDrives = new ItemGroup("scp.hard.drives") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(scpHardDrive);
        }
    };

    static SkeletonKey skeletonKey;
    public static Item pizzaSlice;
    static InfiniteCanteen infiniteCanteen;
    public static SwordItem scalpel;
    static public PoliceBaton policeBaton;
    static public KeyCard keyCard;
    static public Item banana, rubberDuck, gadget;

    public static Item scpHardDrive;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> itemRegister) {
        IForgeRegistry<Item> forgeRegistry = itemRegister.getRegistry();
        PanaceaPill panaceaPills = new PanaceaPill(scp().stacksTo(47));
        pizzaSlice = register(new Item(properties().food(new Food.Builder().nutrition(5).saturationMod(0.4f).build()).tab(items).stacksTo(16)), "pizza_slice", forgeRegistry);
        register(panaceaPills, "panacea_pill", forgeRegistry);
        skeletonKey = register(new SkeletonKey(scp().stacksTo(1)), "skeleton_key", forgeRegistry);
        register(new Tothbrush(scp().stacksTo(1)), "tothbrush", forgeRegistry);
        infiniteCanteen = register(new InfiniteCanteen(scp().stacksTo(1)), "infinite_canteen", forgeRegistry);
        register(new Analyzer(single()), "analyzer", forgeRegistry);
        Item tester = register(new Item(new Item.Properties().stacksTo(1)) {
            @Override
            public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
                BlockPos pos = context.getClickedPos();
                World world = context.getLevel();
                if (!world.isClientSide) {
                    Template2 template2 = new Template2(world.getServer().getStructureManager().get(Structures.scpSite.structures.getRandom()), Collections.emptyList(), (ServerWorld) world);
                    final PlacementSettings placementSettings = new PlacementSettings().setRotation(Rotation.getRandom(Item.random));
                    template2.placeInWorld((IServerWorld) world, pos, placementSettings, Item.random);
                }
                return ActionResultType.SUCCESS;
            }
        }, "tester", forgeRegistry);
        policeBaton = register(new PoliceBaton(single()), "police_baton", forgeRegistry);
        scalpel = register(new SwordItem(ItemTier.GOLD, 3, -2.4f, single()), "scalpel", forgeRegistry);
        keyCard = register(new KeyCard(single()), "keycard", forgeRegistry);
        register(new BananaPill(SCPItems.scp().stacksTo(16).food(new Food.Builder().nutrition(0).saturationMod(0).alwaysEat().effect(() -> {
            EffectInstance effectInstance = new EffectInstance(SCPEffects.bananaDeathEffect, Functions.minutesToTicks(31));
            effectInstance.setCurativeItems(Collections.emptyList());
            return effectInstance;
        }, 1).build())), "scp3521", forgeRegistry);

        banana = register(new Item(properties().food(new Food.Builder().nutrition(4).saturationMod(0.5f).build())), "banana", forgeRegistry);
        register(new ColaBottle(scp().stacksTo(24)), "cola_bottle", forgeRegistry);

        Item item = new RubberDucky(SCPItems.scp().stacksTo(1));
        rubberDuck = register(item, "rubber_duck", forgeRegistry);
        register(new Item(single()) {
            @Override
            public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
                target.remove();
                return ActionResultType.SUCCESS;
            }
        }, "killer", forgeRegistry);
        gadget = register(new Item(properties()), "gadget", forgeRegistry);
        register(new AutoRifle(properties().defaultDurability(1000), 0), "rifle", forgeRegistry);
        register(new FlakCannon(properties().defaultDurability(500), 30), "flak_cannon", forgeRegistry);
        register(new RocketLauncher(properties().defaultDurability(500), 40), "rocket_launcher", forgeRegistry);
        register(new FlameThrower(properties().defaultDurability(500), 0), "flamer", forgeRegistry);
        register(new Flashlight(scp().stacksTo(1)), "displacement_torch", forgeRegistry);
        register(new StasisCage(single()), "stasis_cage", forgeRegistry);
        register(new FlareGun(scp().stacksTo(1)), "flare_gun", forgeRegistry);
        register(new HomeRunBat(scp().stacksTo(1)), "homerun_bat", forgeRegistry);
        register(new Lighter(scp().stacksTo(1)), "escal_lighter", forgeRegistry);

        register(new CompleteMultitool(scp().stacksTo(1).addToolType(ToolType.AXE, 4).addToolType(ToolType.HOE, 4).addToolType(ToolType.SHOVEL, 4).addToolType(ToolType.PICKAXE, 4)), "complete_multitool", forgeRegistry);
        scpHardDrive = register(new HardDrive(new Item.Properties().stacksTo(1).tab(hardDrives)), "scp_hard_drive", forgeRegistry);
    }

    private static Item.Properties scp(){
        return new Item.Properties().tab(Entities.SCPs).rarity(Rarity.RARE);
    }

    public static Item.Properties properties() {
        return new Item.Properties().tab(items);
    }

    public static Item.Properties single() {
        return new Item.Properties().tab(items).stacksTo(1);
    }

    private static <I extends Item> I register(I item, String name, IForgeRegistry<Item> itemIForgeRegistry) {
        item.setRegistryName(SCP.ID, name);
        itemIForgeRegistry.register(item);
        return item;
    }
}
