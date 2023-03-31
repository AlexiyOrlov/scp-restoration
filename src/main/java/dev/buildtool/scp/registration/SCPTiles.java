package dev.buildtool.scp.registration;

import com.google.common.collect.Sets;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.clockworks.ClockworksEntity;
import dev.buildtool.scp.crate.CrateEntity;
import dev.buildtool.scp.harddrivecracker.HardDriveCrackerEntity;
import dev.buildtool.scp.harddrivecracker.HardDriveStoreEntity;
import dev.buildtool.scp.infiniteikea.TeleportBlockEntity;
import dev.buildtool.scp.lock.LockEntity;
import dev.buildtool.scp.lootblock.LootBlockEntity;
import dev.buildtool.scp.mailbox.MailboxEntity;
import dev.buildtool.scp.mailbox.ParcelBlock;
import dev.buildtool.scp.monsterpot.MonsterPotEntity;
import dev.buildtool.scp.oldai.OldAIEntity;
import dev.buildtool.scp.shelf.ShelfEntity;
import dev.buildtool.scp.slidingdoor.SlidingDoorEntity;
import dev.buildtool.scp.table.TableEntity;
import dev.buildtool.scp.table.TableEntity4;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCPTiles {
    public static TileEntityType<SlidingDoorEntity> slidingDoorEntity;
    public static TileEntityType<ClockworksEntity> clockworksEntity;
    public static TileEntityType<CrateEntity> crateEntity;
    public static TileEntityType<ShelfEntity> shelfEntity;
    public static TileEntityType<TableEntity> tableEntity;
    public static TileEntityType<MonsterPotEntity> monsterPotEntity;
    public static TileEntityType<TableEntity4> tableEntity4;
    public static TileEntityType<LockEntity> lockEntity;
    public static TileEntityType<TeleportBlockEntity> ikeaTeleporter;
    public static TileEntityType<OldAIEntity> oldAIEntity;
    public static TileEntityType<dev.buildtool.scp.shelflife.ShelfEntity> shelfLifeEntity;
    public static TileEntityType<LootBlockEntity> lootBlockEntity;
    public static TileEntityType<MailboxEntity> mailboxEntity;
    public static TileEntityType<ParcelBlock.ParcelEntity> parcelEntityTile;
    public static TileEntityType<dev.buildtool.scp.flaregun.CrateEntity> woodenCrate;
    public static TileEntityType<HardDriveCrackerEntity> hardDriveCracker;
    public static TileEntityType<HardDriveStoreEntity> hardDriveStore;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerBlockEntities(RegistryEvent.Register<TileEntityType<?>> registryEvent) {
        IForgeRegistry<TileEntityType<?>> forgeRegistry = registryEvent.getRegistry();

        slidingDoorEntity = new TileEntityType<>(() -> new SlidingDoorEntity(slidingDoorEntity), Collections.singleton(SCPBlocks.slidingDoorBlock), null);
        slidingDoorEntity.setRegistryName(SCP.ID, "sliding_door");
        clockworksEntity = register(ClockworksEntity::new, "clockworks", forgeRegistry, SCPBlocks.clockworksPanel);
        crateEntity = register(() -> new CrateEntity(crateEntity), "crate", forgeRegistry, SCPBlocks.crateBlock);
        shelfEntity = register(() -> new ShelfEntity(shelfEntity), "shelf", forgeRegistry, SCPBlocks.acaciaShelf, SCPBlocks.spruceShelf, SCPBlocks.darkOakShelf, SCPBlocks.oakShelf, SCPBlocks.jungleShelf, SCPBlocks.birchShelf);
        tableEntity = register(() -> new TableEntity(tableEntity), "table_one", forgeRegistry, SCPBlocks.singleItemTable);
        tableEntity4 = register(() -> new TableEntity4(tableEntity4), "table_four", forgeRegistry, SCPBlocks.fourItemTable);
        forgeRegistry.registerAll(slidingDoorEntity, clockworksEntity, crateEntity, shelfEntity, tableEntity, tableEntity4);
        monsterPotEntity = register(() -> new MonsterPotEntity(monsterPotEntity), "monster_pot", forgeRegistry, SCPBlocks.monsterPot);
        lockEntity = register(() -> new LockEntity(lockEntity), "electronic_lock", forgeRegistry, SCPBlocks.electronicLock);
        forgeRegistry.registerAll(monsterPotEntity, lockEntity);
        ikeaTeleporter = register(() -> new TeleportBlockEntity(ikeaTeleporter), "ikea_teleporter", forgeRegistry, SCPBlocks.iikeaEntrance, SCPBlocks.iikeaExit);
        forgeRegistry.register(ikeaTeleporter);
        oldAIEntity = register(() -> new OldAIEntity(oldAIEntity), "old_ai", forgeRegistry, SCPBlocks.oldAIBlock);
        forgeRegistry.registerAll(oldAIEntity);
        shelfLifeEntity = register(() -> new dev.buildtool.scp.shelflife.ShelfEntity(shelfLifeEntity), "shelf_life", forgeRegistry, SCPBlocks.shelfLifeBlock);
        lootBlockEntity = register(() -> new LootBlockEntity(lootBlockEntity), "loot_block", forgeRegistry, SCPBlocks.lootBlock);
        mailboxEntity = register(() -> new MailboxEntity(mailboxEntity), "mailbox", forgeRegistry, SCPBlocks.mailbox);
        parcelEntityTile = register(() -> new ParcelBlock.ParcelEntity(parcelEntityTile), "parcel", forgeRegistry, SCPBlocks.parcelBlock);
        woodenCrate = register(() -> new dev.buildtool.scp.flaregun.CrateEntity(woodenCrate), "wooden_crate", forgeRegistry, SCPBlocks.crate);
        hardDriveCracker=register(() -> new HardDriveCrackerEntity(hardDriveCracker),"hard_drive_cracker",forgeRegistry,SCPBlocks.hardDriveCrackerBlock);
        hardDriveStore=register(() -> new HardDriveStoreEntity(hardDriveStore),"hard_drive_store",forgeRegistry,SCPBlocks.hardDriveStore);
    }

    @SuppressWarnings("unchecked")
    private static <E extends TileEntityType<?>> E register(Supplier<TileEntity> tileEntitySupplier, String id, IForgeRegistry<TileEntityType<?>> forgeRegistry, Block... blocks) {
        TileEntityType<?> tileEntityType = new TileEntityType<>(tileEntitySupplier, Sets.newHashSet(blocks), null);
        tileEntityType.setRegistryName(SCP.ID, id);
        forgeRegistry.register(tileEntityType);
        return (E) tileEntityType;
    }
}
