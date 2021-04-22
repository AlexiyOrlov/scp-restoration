package dev.buildtool.scp.events;

import dev.buildtool.satako.Constants;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.capability.SCPKnowledge;
import dev.buildtool.scp.capability.ThrownItems;
import dev.buildtool.scp.clockworks.ClockworksRecipe;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import org.apache.commons.lang3.text.WordUtils;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    public static IRecipeType<ClockworksRecipe> clockworksRecipeType;
    public static RegistryKey<World> ikeaDimension;

    @SubscribeEvent
    public static void setupCommon(FMLCommonSetupEvent commonSetupEvent) {
        clockworksRecipeType = Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(SCP.ID, "clockworks"), new IRecipeType<ClockworksRecipe>() {
        });
        ikeaDimension = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(SCP.ID, "iikea"));

        CapabilityManager.INSTANCE.register(SCPKnowledge.Knowledge.class, new SCPKnowledge.Storage(), SCPKnowledge.KnowledgeImpl::new);
        CapabilityManager.INSTANCE.register(ThrownItems.ThrownItemMemory.class, new ThrownItems.Storage(), ThrownItems.ThrownItemsImpl::new);

        GlobalEntityTypeAttributes.put(Entities.plagueDoctorEntityType, MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 20).add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_WALK_SPEED).add(Attributes.ATTACK_DAMAGE, 1).add(Attributes.MAX_HEALTH, 512).build());
        GlobalEntityTypeAttributes.put(Entities.corpseEntityType, MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 20).add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.MAX_HEALTH, 20).build());
        GlobalEntityTypeAttributes.put(Entities.sculptureEntityType, MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 64).add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_WALK_SPEED).add(Attributes.ATTACK_DAMAGE, 20).build());
        GlobalEntityTypeAttributes.put(Entities.tickleMonster, MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.27).add(Attributes.ARMOR, 3).add(Attributes.MAX_HEALTH, 40).build());
        GlobalEntityTypeAttributes.put(Entities.humanRefuted, MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_SPRINT_SPEED).add(Attributes.FOLLOW_RANGE, 64).add(Attributes.MAX_HEALTH, 220).add(Attributes.ATTACK_DAMAGE, 10).build());
        GlobalEntityTypeAttributes.put(Entities.humanRefutedChild, MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_SPRINT_SPEED).add(Attributes.FOLLOW_RANGE, 32).add(Attributes.MAX_HEALTH, 110).add(Attributes.ATTACK_DAMAGE, 5).build());
        GlobalEntityTypeAttributes.put(Entities.humanRefutedEgg, MonsterEntity.createMonsterAttributes().add(Attributes.ARMOR, 30).add(Attributes.MAX_HEALTH, 14).build());
        GlobalEntityTypeAttributes.put(Entities.potMonster, MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 4).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 2).build());
        GlobalEntityTypeAttributes.put(Entities.uncleSam, MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.MOVEMENT_SPEED, 0.28).build());
        GlobalEntityTypeAttributes.put(Entities.swatArmorEntity, MonsterEntity.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 3).add(Attributes.MOVEMENT_SPEED, 0.28).add(Attributes.FOLLOW_RANGE, 32).add(Attributes.ARMOR, 20).add(Attributes.MAX_HEALTH, 40).build());
        GlobalEntityTypeAttributes.put(Entities.shyguyEntity, MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 1024).add(Attributes.ATTACK_DAMAGE, 7).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.FOLLOW_RANGE, 64).build());
        GlobalEntityTypeAttributes.put(Entities.theTeacher, MonsterEntity.createMonsterAttributes().add(Attributes.FLYING_SPEED, 0.3).add(Attributes.FOLLOW_RANGE, 16).build());
        GlobalEntityTypeAttributes.put(Entities.sittableEntityType, MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 1).build());
        GlobalEntityTypeAttributes.put(Entities.tatteredFarmer, MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 20).build());
        GlobalEntityTypeAttributes.put(Entities.employeeMonster, MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40).add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_WALK_SPEED).add(Attributes.ATTACK_DAMAGE, 6).build());
        GlobalEntityTypeAttributes.put(Entities.youngGirl, MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_WALK_SPEED).build());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void gatherData(GatherDataEvent gatherDataEvent) {
        DataGenerator dataGenerator = gatherDataEvent.getGenerator();
        ExistingFileHelper existingFileHelper = gatherDataEvent.getExistingFileHelper();
        dataGenerator.addProvider(new LanguageProvider(dataGenerator, SCP.ID, "en_us") {
            @Override
            protected void addTranslations() {
                SCPBlocks.brickSlabs.forEach(slabBlock -> this.add(slabBlock, WordUtils.capitalize(slabBlock.getRegistryName().getPath().replaceAll("_", " "))));
                SCPBlocks.coloredPipes.forEach(rotatedPillarBlock -> add(rotatedPillarBlock, WordUtils.capitalize(rotatedPillarBlock.getRegistryName().getPath().replaceAll("_", " "))));
            }
        });
        dataGenerator.addProvider(new BlockStateProvider(dataGenerator, SCP.ID, existingFileHelper) {
            @Override
            protected void registerStatesAndModels() {
                SCPBlocks.brickSlabs.forEach(block -> {
                    this.slabBlock(block, block.getRegistryName(), new ResourceLocation(SCP.ID, "block/" + block.getRegistryName().getPath().replace("_slab", "")));
                    this.itemModels().withExistingParent(block.getRegistryName().getPath(), new ResourceLocation(SCP.ID, "block/" + block.getRegistryName().getPath()));
                });
                SCPBlocks.coloredPipes.forEach(rotatedPillarBlock -> {
                    String path = rotatedPillarBlock.getRegistryName().getPath();
                    String texture = "block/" + path.substring(0, path.lastIndexOf('_')) + "_noise";
                    this.axisBlock(rotatedPillarBlock, new ResourceLocation(SCP.ID, texture), new ResourceLocation(SCP.ID, texture));
                    itemModels().withExistingParent(path, rotatedPillarBlock.getRegistryName());
                });
            }
        });
        dataGenerator.addProvider(new ForgeRecipeProvider(dataGenerator) {
            @Override
            protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.SKELETON_SPAWN_EGG), new ItemStack(Items.WITHER_SKELETON_SPAWN_EGG), 10, ClockworksRecipe.Mode.FINE, new ResourceLocation(SCP.ID, "clockworks/wither_skeleton")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.COW_SPAWN_EGG), new ItemStack(Items.MOOSHROOM_SPAWN_EGG), 30, ClockworksRecipe.Mode.FINE, new ResourceLocation(SCP.ID, "clockworks/mooshroom")));
//
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.POTATO), new ItemStack(Items.CARROT), 14, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/carrot")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.CARROT), new ItemStack(Items.BEETROOT), 14, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/beet")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.BEETROOT), new ItemStack(Items.WHEAT), 14, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/wheat")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.WHEAT), new ItemStack(Items.POTATO), 14, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/potato")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.PUMPKIN), new ItemStack(Items.MELON), 10, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/melon_block")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.MELON), new ItemStack(Items.PUMPKIN), 10, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/pumpkin_block")));
//
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.MELON_SEEDS), new ItemStack(Items.PUMPKIN_SEEDS), 10, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/melon_seeds")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.PUMPKIN_SEEDS), new ItemStack(Items.MELON_SEEDS), 10, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/pumpkin_seeds")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.BEETROOT_SEEDS), new ItemStack(Items.WHEAT_SEEDS), 10, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/beet_seeds")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.BEETROOT_SEEDS), 10, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/wheat_seeds")));
//
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.DIAMOND_HORSE_ARMOR), new ItemStack(Items.DIAMOND, 5), 20, ClockworksRecipe.Mode.ROUGH, new ResourceLocation(SCP.ID, "clockworks/horse_diamonds")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.IRON_HORSE_ARMOR), new ItemStack(Items.IRON_INGOT, 5), 20, ClockworksRecipe.Mode.ROUGH, new ResourceLocation(SCP.ID, "clockworks/horse_iron")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.LEATHER_HORSE_ARMOR), new ItemStack(Items.LEATHER, 5), 20, ClockworksRecipe.Mode.ROUGH, new ResourceLocation(SCP.ID, "clockworks/horse_leather")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.GOLDEN_HORSE_ARMOR), new ItemStack(Items.GOLD_INGOT, 5), 20, ClockworksRecipe.Mode.ROUGH, new ResourceLocation(SCP.ID, "clockworks/horse_gold")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.SADDLE), new ItemStack(Items.LEATHER, 8), 20, ClockworksRecipe.Mode.COARSE, new ResourceLocation(SCP.ID, "clockworks/saddle_leather")));
//
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.OAK_WOOD), new ItemStack(Items.ACACIA_WOOD), 8, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/acacia_wood")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.ACACIA_WOOD), new ItemStack(Items.BIRCH_WOOD), 8, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/birch_wood")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.BIRCH_WOOD), new ItemStack(Items.JUNGLE_WOOD), 8, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/jungle_wood")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.JUNGLE_WOOD), new ItemStack(Items.DARK_OAK_WOOD), 8, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/dark_wood")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.DARK_OAK_WOOD), new ItemStack(Items.OAK_WOOD), 8, ClockworksRecipe.Mode.ONE_ONE, new ResourceLocation(SCP.ID, "clockworks/oak_wood")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.SUGAR_CANE), new ItemStack(Items.SUGAR, 2), 4, ClockworksRecipe.Mode.ROUGH, new ResourceLocation(SCP.ID, "clockworks/sugar")));
//                consumer.accept(new ClockworksRecipe(new ItemStack(Items.SUGAR_CANE), new ItemStack(Items.PAPER, 2), 4, ClockworksRecipe.Mode.COARSE, new ResourceLocation(SCP.ID, "clockworks/paper")));

                SCPBlocks.coloredPipes.forEach(rotatedPillarBlock -> {
                    int index = SCPBlocks.coloredPipes.indexOf(rotatedPillarBlock);
                    Block block = SCPBlocks.resistantBricks.get(index);
                    new ShapedRecipeBuilder(rotatedPillarBlock, 4).define('B', block).define('D', DyeColor.values()[index].getTag()).pattern("D").pattern("B").unlockedBy("has_brick", has(block)).save(consumer);
                });
            }

        });
    }
}
