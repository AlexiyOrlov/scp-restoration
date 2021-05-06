package dev.buildtool.scp.events;

import dev.buildtool.satako.Constants;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.chairs.Sittable;
import dev.buildtool.scp.human.ChaosInsurgencySoldier;
import dev.buildtool.scp.human.FemaleCommoner;
import dev.buildtool.scp.human.Human;
import dev.buildtool.scp.human.MaleCommoner;
import dev.buildtool.scp.humansrefuted.EggEntity;
import dev.buildtool.scp.humansrefuted.HumanRefuted;
import dev.buildtool.scp.humansrefuted.HumanRefutedChild;
import dev.buildtool.scp.infiniteikea.FemaleCivilian;
import dev.buildtool.scp.infiniteikea.IkeaMonster;
import dev.buildtool.scp.infiniteikea.MaleCivilian;
import dev.buildtool.scp.monsterpot.PotMonster;
import dev.buildtool.scp.plaguedoctor.Corpse;
import dev.buildtool.scp.plaguedoctor.PlagueDoctor;
import dev.buildtool.scp.sculpture.Sculpture;
import dev.buildtool.scp.shyguy.ShyguyEntity;
import dev.buildtool.scp.smallgirl.YoungGirl;
import dev.buildtool.scp.swatarmor.SwatArmorEntity;
import dev.buildtool.scp.tatteredfarmer.TatteredFarmer;
import dev.buildtool.scp.theteacher.TheTeacherEntity;
import dev.buildtool.scp.ticklemonster.TickleMonsterEntity;
import dev.buildtool.scp.unclesam.UncleSam;
import dev.buildtool.scp.wallofflesh.WallOfFlesh;
import dev.buildtool.scp.weapons.FlakShard;
import dev.buildtool.scp.weapons.Rocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class Entities {
    public static EntityType<Sittable> sittableEntityType;
    public static EntityType<PlagueDoctor> plagueDoctorEntityType;
    public static EntityType<Corpse> corpseEntityType;
    public static EntityType<Human> humanEntityType;
    public static EntityType<MaleCommoner> maleCommoner;
    public static EntityType<FemaleCommoner> femaleCommoner;
    public static EntityType<Sculpture> sculptureEntityType;
    public static EntityType<TickleMonsterEntity> tickleMonster;
    public static EntityType<HumanRefuted> humanRefuted;
    public static EntityType<HumanRefutedChild> humanRefutedChild;
    public static EntityType<EggEntity> humanRefutedEgg;
    public static EntityType<PotMonster> potMonster;
    public static EntityType<SwatArmorEntity> swatArmorEntity;
    public static EntityType<UncleSam> uncleSam;
    public static EntityType<ShyguyEntity> shyguyEntity;
    public static EntityType<TheTeacherEntity> theTeacher;
    public static EntityType<TatteredFarmer> tatteredFarmer;
    public static EntityType<IkeaMonster> employeeMonster;
    public static EntityType<MaleCivilian> maleCivilian;
    public static EntityType<FemaleCivilian> femaleCivilian;
    public static EntityType<YoungGirl> youngGirl;
    public static EntityType<FlakShard> flakShard;
    public static EntityType<Rocket> rocket;
    public static EntityType<ChaosInsurgencySoldier> chaosInsurgencySoldier;
    public static EntityType<WallOfFlesh> wallOfFlesh;

    public static ItemGroup SCPs = new ItemGroup("scps") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(SCPItems.skeletonKey);
        }
    };

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> itemRegister) {
        IForgeRegistry<Item> items = itemRegister.getRegistry();
        plagueDoctorEntityType = registerEntity("plague_doctor", EntityClassification.MONSTER, PlagueDoctor::new, 0.8f, 1.9f, null);
        humanEntityType = registerEntity("human", EntityClassification.CREATURE, Human::new, 0.6f, 1.8f, null);
        sculptureEntityType = registerEntity("sculpture", EntityClassification.MONSTER, Sculpture::new, 0.7f, 1.9f, null);
        SpawnEggItem spawnEggItem = new SpawnEggItem(plagueDoctorEntityType, 0x4e4e4e, 0xd9d9d9, properties());
        items.registerAll(spawnEggItem.setRegistryName(SCP.ID, "plague_doctor_egg"));
        items.register(registerEgg(sculptureEntityType, 0xd26f2d, 0x1c701e));
        tickleMonster = registerEntity("tickle_monster", EntityClassification.CREATURE, TickleMonsterEntity::new, 1.9f, 0.9f, null);
        humanRefuted = registerEntity("scp3199", EntityClassification.MONSTER, HumanRefuted::new, 0.9f, 2.9f, null);
        items.register(registerEgg(tickleMonster, 0xe07b2d, 0xe07b2d));
        items.register(registerEgg(humanRefuted, 0xf69dd1, 0xf69dd1));
        swatArmorEntity = registerEntity("scp912", EntityClassification.CREATURE, SwatArmorEntity::new, 0.7f, 1.8f, null);
        items.registerAll(registerEgg(swatArmorEntity, 0x1A1D19, 0x565857));

        maleCommoner = registerEntity("male", EntityClassification.CREATURE, MaleCommoner::new, 0.6f, 1.8f, null);
        femaleCommoner = registerEntity("female", EntityClassification.CREATURE, FemaleCommoner::new, 0.6f, 1.8f, null);
        items.registerAll(new SpawnEggItem(maleCommoner, 0x347BAF, 0x347BAF, properties().tab(SCPItems.items)).setRegistryName(SCP.ID, "male_egg"), new SpawnEggItem(femaleCommoner, 0xAF5D9E, 0xAF5D9E, properties().tab(SCPItems.items)).setRegistryName(SCP.ID, "female_egg"));
        uncleSam = registerEntity("uncle_sam", EntityClassification.CREATURE, UncleSam::new, 0.6f, 1.8f, null);
        items.register(registerEgg(uncleSam, 0x855200, 0x7A7859));
        shyguyEntity = registerEntity("shyguy", EntityClassification.MONSTER, ShyguyEntity::new, 0.6f, 2.9f, null);
        items.register(registerEgg(shyguyEntity, 0x998d78, 0x998d78));
        theTeacher = registerEntity("the_teacher", EntityClassification.CREATURE, TheTeacherEntity::new, 0.9f, 0.9f, null);
        items.register(registerEgg(theTeacher, 0xffffff, 0xffffff));
        tatteredFarmer = registerEntity("tattered_farmer", EntityClassification.MISC, TatteredFarmer::new, 0.5f, 2.9f, null);
        items.register(registerEgg(tatteredFarmer, 0x234a20, 0xf1bb2a));
        employeeMonster = registerEntity("ikea_monster", EntityClassification.MONSTER, IkeaMonster::new, 0.8f, 2.8f, null);
        items.register(registerEgg(employeeMonster, 0x243CAF, 0xAF9C1E));
        youngGirl = registerEntity("young_girl", EntityClassification.CREATURE, YoungGirl::new, 0.6f, 1, null);
        items.register(registerEgg(youngGirl, 0xaa5915, 0x871131));
        chaosInsurgencySoldier = registerEntity("chaos_ins_soldier", EntityClassification.MONSTER, ChaosInsurgencySoldier::new, 0.7f, 1.8f, null);
        items.register(registerEgg(chaosInsurgencySoldier, new Item.Properties().tab(SCPItems.items), 0x307B1F, 0x7B1E1E));
        wallOfFlesh = registerEntity("wall_of_flesh", EntityClassification.MONSTER, WallOfFlesh::new, 0.99f, 1.99f, null);
        items.register(registerEgg(wallOfFlesh, 0xf74848, 0xfff6bd));
    }

    private static Item registerEgg(EntityType<? extends Entity> entityType, int primaryColor, int secondaryColor) {
        return new SpawnEggItem(entityType, primaryColor, secondaryColor, properties()).setRegistryName(SCP.ID, entityType.getRegistryName().getPath() + "_egg");
    }

    private static Item registerEgg(EntityType<? extends Entity> entityType, Item.Properties properties, int primaryColor, int secondaryColor) {
        return new SpawnEggItem(entityType, primaryColor, secondaryColor, properties).setRegistryName(SCP.ID, entityType.getRegistryName().getPath() + "_egg");
    }

    private static Item.Properties properties() {
        return new Item.Properties().tab(SCPs);
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> registryEvent) {
        IForgeRegistry<EntityType<?>> forgeRegistry = registryEvent.getRegistry();
        sittableEntityType = registerEntity("sit_anchor", EntityClassification.MISC, (p_create_1_, p_create_2_) -> new Sittable(p_create_2_), 0.5f, 0.5f, forgeRegistry);
        forgeRegistry.register(plagueDoctorEntityType);
        corpseEntityType = registerEntity("corpse", EntityClassification.MONSTER, Corpse::new, 0.9f, 1.9f, forgeRegistry);
        humanRefutedEgg = registerEntity("scp3199egg", EntityClassification.MISC, EggEntity::new, 6 / 16f, 7 / 16f, forgeRegistry);
        potMonster = registerEntity("pot_monster", EntityClassification.MONSTER, PotMonster::new, 7 / 16f, 7 / 16f, forgeRegistry);
        forgeRegistry.registerAll(swatArmorEntity);
        forgeRegistry.registerAll(humanEntityType, sculptureEntityType, tickleMonster, humanRefuted);
        forgeRegistry.registerAll(maleCommoner, femaleCommoner, uncleSam, shyguyEntity);
        humanRefutedChild = registerEntity("human_refuted_child", EntityClassification.MONSTER, HumanRefutedChild::new, 0.6f, 1.6f, forgeRegistry);
        forgeRegistry.registerAll(theTeacher, tatteredFarmer);
        forgeRegistry.register(employeeMonster);
        maleCivilian = registerEntity("male_civilian", EntityClassification.CREATURE, MaleCivilian::new, 0.6f, 1.9f, forgeRegistry);
        femaleCivilian = registerEntity("female_civilian", EntityClassification.CREATURE, FemaleCivilian::new, 0.6f, 1.9f, forgeRegistry);
        forgeRegistry.register(youngGirl);

        EntitySpawnPlacementRegistry.register(employeeMonster, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, (monsterEntityType, serverWorld, spawnReason, blockPos, random) -> true);
        EntitySpawnPlacementRegistry.register(femaleCivilian, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, (monsterEntityType, serverWorld, spawnReason, blockPos, random) -> serverWorld.getLightEmission(blockPos) > 0);
        EntitySpawnPlacementRegistry.register(maleCivilian, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, (monsterEntityType, serverWorld, spawnReason, blockPos, random) -> serverWorld.getLightEmission(blockPos) > 0);
        forgeRegistry.register(chaosInsurgencySoldier);
        EntitySpawnPlacementRegistry.register(chaosInsurgencySoldier, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, (p_test_1_, p_test_2_, p_test_3_, p_test_4_, p_test_5_) -> true);

        flakShard = registerFastEntity("flak_shard", EntityClassification.MISC, FlakShard::new, 0.1f, 0.1f, forgeRegistry);
        rocket = registerFastEntity("rocket", EntityClassification.MISC, (p_create_1_, p_create_2_) -> new Rocket(p_create_1_, p_create_2_, 0, 1), 0.2f, 0.2f, forgeRegistry);
        forgeRegistry.register(wallOfFlesh);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent attributeCreationEvent) {
        attributeCreationEvent.put(Entities.flakShard, MonsterEntity.createMonsterAttributes().build());
        attributeCreationEvent.put(Entities.rocket, MonsterEntity.createMonsterAttributes().build());
        attributeCreationEvent.put(Entities.maleCivilian, MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_WALK_SPEED).add(Attributes.ATTACK_DAMAGE, 1).add(ForgeMod.SWIM_SPEED.get(), 3).build());
        attributeCreationEvent.put(Entities.femaleCivilian, MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_WALK_SPEED).add(Attributes.ATTACK_DAMAGE, 1).add(ForgeMod.SWIM_SPEED.get(), 3).build());
        attributeCreationEvent.put(Entities.maleCommoner, MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 32).add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_SPRINT_SPEED).add(Attributes.ATTACK_DAMAGE, 1).add(ForgeMod.SWIM_SPEED.get(), 3).add(Attributes.MAX_HEALTH, 20).build());
        attributeCreationEvent.put(Entities.femaleCommoner, MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 32).add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_SPRINT_SPEED).add(Attributes.ATTACK_DAMAGE, 1).add(ForgeMod.SWIM_SPEED.get(), 3).add(Attributes.MAX_HEALTH, 20).build());
        attributeCreationEvent.put(Entities.humanEntityType, MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 60).add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_SPRINT_SPEED).add(Attributes.ATTACK_DAMAGE, 1).add(Attributes.MAX_HEALTH, 20).build());
        attributeCreationEvent.put(chaosInsurgencySoldier, MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 32).add(Attributes.MOVEMENT_SPEED, Constants.PLAYER_WALK_SPEED).add(Attributes.ATTACK_DAMAGE, 1).add(ForgeMod.SWIM_SPEED.get(), 3).add(Attributes.MAX_HEALTH, 20).build());
        attributeCreationEvent.put(wallOfFlesh, MonsterEntity.createMonsterAttributes().add(Attributes.ARMOR, 4).add(Attributes.MAX_HEALTH, 1024).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.ATTACK_DAMAGE, 20).build());
    }

    @SuppressWarnings("unchecked")
    private static <E extends EntityType<T>, T extends Entity> E registerEntity(String id, EntityClassification entityCategory, EntityType.IFactory<T> factory, float width, float height, IForgeRegistry<EntityType<?>> forgeRegistry) {
        EntityType<T> entityType = EntityType.Builder.of(factory, entityCategory).sized(width, height).setTrackingRange(64).setUpdateInterval(3).build(id);
        entityType.setRegistryName(SCP.ID, id);
        if (forgeRegistry != null)
            forgeRegistry.register(entityType);
        return (E) entityType;
    }

    @SuppressWarnings("unchecked")
    private static <E extends EntityType<T>, T extends Entity> E registerFastEntity(String id, EntityClassification entityCategory, EntityType.IFactory<T> factory, float width, float height, IForgeRegistry<EntityType<?>> forgeRegistry) {
        EntityType<T> entityType = EntityType.Builder.of(factory, entityCategory).sized(width, height).setTrackingRange(100).setUpdateInterval(1).build(id);
        entityType.setRegistryName(SCP.ID, id);
        if(forgeRegistry!=null)
            forgeRegistry.register(entityType);
        return (E) entityType;
    }

}
