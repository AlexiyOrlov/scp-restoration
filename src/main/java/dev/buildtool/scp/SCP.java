package dev.buildtool.scp;

import dev.buildtool.scp.capability.Packet;
import dev.buildtool.scp.capability.SCPKnowledge;
import dev.buildtool.scp.clockworks.ClockworksEntity;
import dev.buildtool.scp.clockworks.ClockworksRecipe;
import dev.buildtool.scp.clockworks.Settings;
import dev.buildtool.scp.harddrivecracker.CrackingProgress;
import dev.buildtool.scp.harddrivecracker.HardDriveCrackerEntity;
import dev.buildtool.scp.harddrivecracker.StartCracking;
import dev.buildtool.scp.registration.Entities;
import dev.buildtool.scp.registration.SCPBlocks;
import dev.buildtool.scp.goals.GoalAction;
import dev.buildtool.scp.human.*;
import dev.buildtool.scp.lock.LockEntity;
import dev.buildtool.scp.lock.OpenLock;
import dev.buildtool.scp.lock.SetPassword;
import dev.buildtool.scp.lootblock.LootBlockEntity;
import dev.buildtool.scp.lootblock.SetIdentifier;
import dev.buildtool.scp.mailbox.MailboxEntity;
import dev.buildtool.scp.mailbox.ParcelBlock;
import dev.buildtool.scp.mailbox.SendMail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.items.CapabilityItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mod(SCP.ID)
public class SCP {
    public static final String ID="scp_restoration";
    public static SimpleChannel channel;
    public static Logger logger = LogManager.getLogger("SCP Restoration");
    public static ForgeConfigSpec.BooleanValue writeClockworksRecipes;
    public static ForgeConfigSpec.BooleanValue toothBrushCanBreakUnbreakable;
    public static ForgeConfigSpec.ConfigValue<Double> chamberDamage;
    public static ForgeConfigSpec.IntValue chaosSoldierWeight;
    public static ForgeConfigSpec.IntValue scp1162ItemLimit;
    private static final String networkProtocolVersion = "2";
    public static List<ResourceLocation> scpBlacklist = new ArrayList<>(40);
    public static ForgeConfigSpec.IntValue hardDriveRarity;
    public static ForgeConfigSpec.ConfigValue<List<?>> blacklistedSCPs;
    public SCP() {
        int message = 0;
        channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(ID, "channel1"), () -> networkProtocolVersion, s -> s.equals(networkProtocolVersion), s -> s.equals(networkProtocolVersion));
        channel.registerMessage(message++, Settings.class, (settings, packetBuffer) -> {
            packetBuffer.writeEnum(settings.mode);
            packetBuffer.writeBlockPos(settings.pos);
            packetBuffer.writeBoolean(settings.isWorkind);
            packetBuffer.writeBoolean(settings.automaticInput);
        }, packetBuffer -> {
            ClockworksRecipe.Mode mode = packetBuffer.readEnum(ClockworksRecipe.Mode.class);
            BlockPos b = packetBuffer.readBlockPos();
            boolean w = packetBuffer.readBoolean();
            boolean a = packetBuffer.readBoolean();
            return new Settings(a, mode, w, b);
        },(settings, contextSupplier) -> {
            NetworkEvent.Context context=contextSupplier.get();
            World world=context.getSender().getLevel();
            TileEntity tileEntity=world.getBlockEntity(settings.pos);
            if (tileEntity instanceof ClockworksEntity) {
                ClockworksEntity clockworksEntity = (ClockworksEntity) tileEntity;
                clockworksEntity.working = settings.isWorkind;
                clockworksEntity.mode = settings.mode;
                clockworksEntity.autoInput = settings.automaticInput;
                clockworksEntity.setChanged();
            }
        });

        channel.registerMessage(message++, Packet.class, (packet, packetBuffer) -> {
            packet.knowledge.knownSCPData().forEach((s, data) -> {
                packetBuffer.writeUtf(s);
                packetBuffer.writeNbt(data.saveTo(new CompoundNBT()));
            });
        }, packetBuffer -> {
            Packet packet = new Packet(new SCPKnowledge.KnowledgeImpl());
            while (packetBuffer.isReadable()) {
                String s = packetBuffer.readUtf();
                CompoundNBT compoundNBT = packetBuffer.readNbt();
                packet.knowledge.knownSCPData().put(s, new SCPKnowledge.Data().loadFrom(compoundNBT));
            }
            return packet;
        }, (packet, contextSupplier) -> {
            new ClientProxy().synchronizeKnowledge(packet, contextSupplier);
        });

        channel.registerMessage(message++, SetPassword.class, (setPassword, packetBuffer) -> {
            packetBuffer.writeBlockPos(setPassword.lockpos);
            packetBuffer.writeUtf(setPassword.string);
        }, packetBuffer -> new SetPassword(packetBuffer.readBlockPos(), packetBuffer.readUtf()), (setPassword, contextSupplier) -> {
            World world = contextSupplier.get().getSender().getLevel();
            LockEntity lockEntity = (LockEntity) world.getBlockEntity(setPassword.lockpos);
            if (lockEntity != null) {
                lockEntity.password = setPassword.string;
                lockEntity.setChanged();
            }
            contextSupplier.get().setPacketHandled(true);
        });

        channel.registerMessage(message++, OpenLock.class, (o, packetBuffer) -> {
                    packetBuffer.writeBlockPos(o.lockpos);
                }, packetBuffer -> new OpenLock(packetBuffer.readBlockPos()),
                (openLock, contextSupplier) -> {
                    TileEntity tileEntity = contextSupplier.get().getSender().getLevel().getBlockEntity(openLock.lockpos);
                    if (tileEntity instanceof LockEntity) {
                        ((LockEntity) tileEntity).open();
                        tileEntity.setChanged();
                    }
                    contextSupplier.get().setPacketHandled(true);
                });

        channel.registerMessage(message++, ActivateGoal.class, (activateGoal, packetBuffer) -> {
            packetBuffer.writeInt(activateGoal.humanId);
            packetBuffer.writeEnum(activateGoal.goalAction);
        }, packetBuffer -> {
            int hid = packetBuffer.readInt();
            GoalAction goalAction = packetBuffer.readEnum(GoalAction.class);
            return new ActivateGoal(goalAction, hid);
        }, (activateGoal, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            ServerWorld serverWorld = context.getSender().getLevel();
            Entity entity = serverWorld.getEntity(activateGoal.humanId);
            if (entity instanceof Human) {
                Human human = (Human) entity;
                human.setActiveCommand(activateGoal.goalAction);
                context.getSender().sendMessage(new TranslationTextComponent("scp.activated.goal").append(new StringTextComponent(": " + activateGoal.goalAction.toString().toLowerCase().replace('_', ' '))), UUID.randomUUID());
                context.setPacketHandled(true);
            }
        });

        channel.registerMessage(message++, SetOwner.class, (setOwner, packetBuffer) -> {
            packetBuffer.writeInt(setOwner.eid);
            packetBuffer.writeUUID(setOwner.ownerUUid);
        }, packetBuffer -> new SetOwner(packetBuffer.readInt(), packetBuffer.readUUID()), (setOwner, contextSupplier) -> {
            Entity entity = contextSupplier.get().getSender().getLevel().getEntity(setOwner.eid);
            if (entity instanceof Human) {
                ((Human) entity).setOwner(setOwner.ownerUUid);
                contextSupplier.get().setPacketHandled(true);
                contextSupplier.get().getSender().sendMessage(new TranslationTextComponent("scp.human.claimed"), UUID.randomUUID());
            }
        });

        channel.registerMessage(message++, SorryPlayer.class, (sorryPlayer, packetBuffer) -> {
            packetBuffer.writeInt(sorryPlayer.humanid);
        }, packetBuffer -> new SorryPlayer(packetBuffer.readInt()), (sorryPlayer, contextSupplier) -> {
            ServerPlayerEntity sender = contextSupplier.get().getSender();
            Entity entity = sender.getLevel().getEntity(sorryPlayer.humanid);
            if (entity instanceof Human) {
                Human human = (Human) entity;
                if (human.hasOwner()) {
                    if (human.getTarget() == sender) {
                        human.defend.stop();
                        sender.sendMessage(new StringTextComponent(human.getName().getString() + " " + new TranslationTextComponent("scp.forgiven.you").getString()), human.getUUID());
                        contextSupplier.get().getSender().closeContainer();
                    }
                }
                contextSupplier.get().setPacketHandled(true);
            }
        });

        channel.registerMessage(message++, SpawnHuman.class, (spawnHuman, packetBuffer) -> {
            packetBuffer.writeBlockPos(spawnHuman.at);
            packetBuffer.writeBoolean(spawnHuman.male);
        }, packetBuffer -> {
            BlockPos position = packetBuffer.readBlockPos();
            return new SpawnHuman(packetBuffer.readBoolean(), position);
        }, (spawnHuman, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            Human human;
            ServerWorld serverWorld = context.getSender().getLevel();
            if (spawnHuman.male)
                human = Entities.maleCommoner.create(serverWorld);
            else
                human = Entities.femaleCommoner.create(serverWorld);
            human.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(spawnHuman.at), SpawnReason.MOB_SUMMONED, null, null);
            human.setPos(spawnHuman.at.getX() + 0.5, spawnHuman.at.getY(), spawnHuman.at.getZ() + 0.5);
            serverWorld.addFreshEntity(human);
            int c = 5;
            for (int i = 0; i < context.getSender().inventory.getContainerSize(); i++) {
                ItemStack next = context.getSender().inventory.getItem(i);
                if (next.getItem() == Items.GOLD_INGOT) {
                    while (c > 0) {
                        next.shrink(1);
                        c--;
                        if (next.isEmpty())
                            break;
                    }
                    if (c == 0)
                        break;
                }
            }
            context.setPacketHandled(true);
        });

        channel.registerMessage(message++, SetIdentifier.class, (setIdentifier, packetBuffer) -> {
                    packetBuffer.writeUtf(setIdentifier.identifier);
                    packetBuffer.writeBlockPos(setIdentifier.pos);
                }, packetBuffer -> new SetIdentifier(packetBuffer.readUtf(), packetBuffer.readBlockPos()),
                (setIdentifier, contextSupplier) -> {
                    NetworkEvent.Context context = contextSupplier.get();
                    ServerWorld serverWorld = context.getSender().getLevel();
                    TileEntity tileEntity = serverWorld.getBlockEntity(setIdentifier.pos);
                    if (tileEntity instanceof LootBlockEntity) {
                        ((LootBlockEntity) tileEntity).identifier = setIdentifier.identifier;
                        tileEntity.setChanged();
                        context.setPacketHandled(true);
                        context.getSender().sendMessage(new StringTextComponent("Identifier set to: " + setIdentifier.identifier), UUID.randomUUID());
                    }
                });

        channel.registerMessage(message++, DropWeapon.class, (dropWeapon, packetBuffer) -> packetBuffer.writeInt(dropWeapon.humanid),
                packetBuffer -> new DropWeapon(packetBuffer.readInt()),
                (dropWeapon, contextSupplier) -> {
                    NetworkEvent.Context context = contextSupplier.get();
                    Entity entity = context.getSender().getLevel().getEntity(dropWeapon.humanid);
                    if (entity instanceof Human) {
                        Human human = (Human) entity;
                        if (!human.getMainHandItem().isEmpty()) {
                            ItemEntity itemEntity = human.spawnAtLocation(human.getMainHandItem().copy(), 2);
                            itemEntity.setPickUpDelay(30);
                            human.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                        }
                        context.setPacketHandled(true);
                        context.getSender().closeContainer();
                    }
                });

        channel.registerMessage(message++, SendMail.class, (sendMail, packetBuffer) -> {
                    packetBuffer.writeBlockPos(sendMail.tilePos);
                    packetBuffer.writeBlockPos(sendMail.targetPos);
                }, packetBuffer -> new SendMail(packetBuffer.readBlockPos(), packetBuffer.readBlockPos()),
                (sendMail, contextSupplier) -> {
                    NetworkEvent.Context context = contextSupplier.get();
                    ServerWorld serverWorld = context.getSender().getLevel();
                    if (serverWorld.isLoaded(sendMail.targetPos)) {
                        BlockPos empty = serverWorld.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, sendMail.targetPos);
                        if (serverWorld.isEmptyBlock(empty)) {
                            serverWorld.setBlockAndUpdate(empty, SCPBlocks.parcelBlock.defaultBlockState());
                            MailboxEntity blockEntity = (MailboxEntity) serverWorld.getBlockEntity(sendMail.tilePos);
                            ItemStack itemStack = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null).extractItem(0, 64, false);
                            ParcelBlock.ParcelEntity parcelEntity = (ParcelBlock.ParcelEntity) serverWorld.getBlockEntity(empty);
                            parcelEntity.mail = itemStack;
                            parcelEntity.setChanged();
                            context.getSender().sendMessage(new TranslationTextComponent("scp.parcel.arrived.at").append(" " + empty.getX() + " " + empty.getY() + " " + empty.getZ()), UUID.randomUUID());
                            blockEntity.prevX = empty.getX();
                            blockEntity.prevY = empty.getY();
                            blockEntity.prevZ = empty.getZ();
                            blockEntity.setChanged();
                        } else {
                            context.getSender().sendMessage(new TranslationTextComponent("scp.target.occupied"), UUID.randomUUID());
                        }
                    } else
                        context.getSender().sendMessage(new TranslationTextComponent("scp.position.not.loaded"), UUID.randomUUID());
                });

        channel.registerMessage(message++, CrackingProgress.class,(crackingProgress, packetBuffer) -> {
            packetBuffer.writeBlockPos(crackingProgress.pos);
            packetBuffer.writeInt(crackingProgress.timeLeft);
        },packetBuffer -> new CrackingProgress(packetBuffer.readBlockPos(),packetBuffer.readInt()),(crackingProgress, contextSupplier) -> {
            new ClientProxy().synchronizeCrackerTime(crackingProgress,contextSupplier.get());
        });
        channel.registerMessage(message++, StartCracking.class,(startCracking, packetBuffer) -> {
                packetBuffer.writeBlockPos(startCracking.blockPos);
            },packetBuffer -> new StartCracking(packetBuffer.readBlockPos()),(startCracking, contextSupplier) -> {
            ServerWorld serverWorld=contextSupplier.get().getSender().getLevel();
            TileEntity tileEntity=serverWorld.getBlockEntity(startCracking.blockPos);
            if(tileEntity instanceof HardDriveCrackerEntity)
            {
                HardDriveCrackerEntity crackerEntity= (HardDriveCrackerEntity) tileEntity;
                crackerEntity.inProgress=true;
                crackerEntity.setChanged();
                contextSupplier.get().setPacketHandled(true);
            }
        });
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, new ForgeConfigSpec.Builder().configure(builder -> {
            writeClockworksRecipes = builder.define("Create a file listing all Clockworks recipes", true);
            toothBrushCanBreakUnbreakable = builder.define("SCP-063 can break unbreakable blocks", false);
            chamberDamage = builder.comment("Amount of damage appplied to outer walls of generated SCP chambers").defineInRange("SCP chamber damage", 0.0, 0.0, 0.9);
            chaosSoldierWeight = builder.defineInRange("Spawning frequency of Chaos Insurgency soldiers", 2, 0, 20);
            scp1162ItemLimit = builder.defineInRange("SCP-1162 item limit per player", 54, 10, 150);
            hardDriveRarity=builder.comment("Higher value means less frequent hard drive occurrence").defineInRange("SCP hard drive rarity",200,20,200000);
            blacklistedSCPs=builder.comment("These SCPs won't occur").defineList("Blacklisted SCPs", Collections::emptyList, o -> o instanceof String);
            return builder.build();
        }).getRight());
        Path scpBlacklist = Paths.get("config", "scp-blacklist.ini");
        if (Files.notExists(scpBlacklist)) {
            try {
                Files.createFile(scpBlacklist);
                Files.write(scpBlacklist, Collections.singleton("# One SCP number per line"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            List<String> strings = Files.readAllLines(scpBlacklist);
            strings.forEach(s -> {
                if (!s.startsWith("#"))
                    SCP.scpBlacklist.add(new ResourceLocation(ID, "containers/" + s.trim()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
