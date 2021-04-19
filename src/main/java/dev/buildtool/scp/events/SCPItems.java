package dev.buildtool.scp.events;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.Template2;
import dev.buildtool.scp.items.*;
import dev.buildtool.scp.lock.KeyCard;
import dev.buildtool.scp.swatarmor.PoliceBaton;
import dev.buildtool.scp.template.SCPTemplate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCPItems {
    static ItemGroup items=new ItemGroup("scp.items") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(pizzaSlice);
        }
    };
    static SkeletonKey skeletonKey;
    public static Item pizzaSlice;
    static InfiniteCanteen infiniteCanteen;
    public static SwordItem scalpel;
    static public PoliceBaton policeBaton;
    static public KeyCard keyCard;
    static public Item banana, rubberDuck, gadget;

    public static SCPTemplate scpTemplate;

    static ItemGroup templates = new ItemGroup("scp.templates") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(scpTemplate);
        }
    };

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> itemRegister) {
        IForgeRegistry<Item> forgeRegistry = itemRegister.getRegistry();
        PanaceaPill panaceaPills = new PanaceaPill(scp().stacksTo(47));
        pizzaSlice = register(new Item(properties().food(new Food.Builder().nutrition(5).saturationMod(0.4f).build()).tab(items).stacksTo(16)), "pizza_slice");
        forgeRegistry.registerAll(register(panaceaPills, "panacea_pill"), register(skeletonKey = new SkeletonKey(scp().stacksTo(1)), "skeleton_key"),
                register(new Tothbrush(scp().stacksTo(1)), "tothbrush"), register(infiniteCanteen = new InfiniteCanteen(scp().stacksTo(1)), "infinite_canteen"));
        forgeRegistry.registerAll(register(new Analyzer(single()), "analyzer"), pizzaSlice);
        Item tester = new Item(single()) {
            @Override
            public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
                BlockPos pos = context.getClickedPos();
                World world = context.getLevel();
                if (!world.isClientSide) {
                    Template2 template2 = new Template2(world.getServer().getStructureManager().get(Structures.scpSite.structures.getRandom()), Collections.emptyList(), (ServerWorld) world);
                    BlockPos size = template2.size;
                    MutableBoundingBox mutableBoundingBox = new MutableBoundingBox(pos, new BlockPos(pos.getX() + size.getX(), pos.getY() + size.getY(), pos.getZ() + size.getZ()));
                    for (int i = pos.getX(); i < pos.getX() + size.getX(); i++) {
                        for (int p = pos.getZ(); p < pos.getZ() + size.getZ(); p++) {
                            for (int j = pos.getY(); j < pos.getY() + size.getY(); j++) {

                                if (i == mutableBoundingBox.x0 || i == mutableBoundingBox.x1 - 1 || p == mutableBoundingBox.z1 - 1 || p == mutableBoundingBox.z0 || j == mutableBoundingBox.y0 || j == mutableBoundingBox.y1 - 1) {
                                    world.setBlockAndUpdate(new BlockPos(i, j, p), SCPBlocks.resistantGlass.get(random.nextInt(16)).defaultBlockState());
                                }
                            }
                        }
                    }
                }
                return ActionResultType.SUCCESS;
            }
        };
        forgeRegistry.registerAll(register(tester, "tester"));
        policeBaton = register(new PoliceBaton(single()), "police_baton");
        scalpel = register(new SwordItem(ItemTier.GOLD, 3, -2.4f, single()), "scalpel");
        keyCard = register(new KeyCard(single()), "keycard");
        forgeRegistry.registerAll(scalpel, policeBaton, keyCard);
        Item bananaPill = new BananaPill(SCPItems.scp().stacksTo(16).food(new Food.Builder(). nutrition(0).saturationMod(0).alwaysEat().effect(() -> {
            EffectInstance effectInstance = new EffectInstance(SCPEffects.bananaDeathEffect, Functions.minutesToTicks(31));
            effectInstance.setCurativeItems(Collections.emptyList());
            return effectInstance;
        }, 1).build()));

        banana = register(new Item(properties().food(new Food.Builder().nutrition(4).saturationMod(0.5f).build())), "banana");
        forgeRegistry.registerAll(banana, bananaPill.setRegistryName(SCP.ID, "scp3521"));
        forgeRegistry.register(register(new ColaBottle(scp().stacksTo(24)), "cola_bottle"));

        Item item = new RubberDucky(SCPItems.scp().stacksTo(1));
        rubberDuck = register(item, "rubber_duck");
        forgeRegistry.registerAll(rubberDuck);
        register(new Item(single()) {
            @Override
            public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
                target.remove();
                return ActionResultType.SUCCESS;
            }
        }, "killer", forgeRegistry);
        gadget = register(new Item(properties()), "gadget", forgeRegistry);
        register(new AutoRifle(properties().defaultDurability(1000), 0), "rifle", forgeRegistry);
        scpTemplate = register(new SCPTemplate(single().tab(templates)), "scp_template", forgeRegistry);
    }

    private static Item.Properties scp(){
        return new Item.Properties().tab(Entities.SCPs);
    }

    public static Item.Properties properties() {
        return new Item.Properties().tab(items);
    }

    public static Item.Properties single() {
        return new Item.Properties().tab(items).stacksTo(1);
    }

    @Deprecated
    private static <T extends Item> T register(T item, String id) {
        item.setRegistryName(SCP.ID, id);
        return item;
    }

    private static <I extends Item> I register(I item, String name, IForgeRegistry<Item> itemIForgeRegistry) {
        item.setRegistryName(SCP.ID, name);
        itemIForgeRegistry.register(item);
        return item;
    }

}
