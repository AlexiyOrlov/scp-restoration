package dev.buildtool.scp;

import dev.buildtool.scp.capability.Packet;
import dev.buildtool.scp.capability.SCPKnowledge;
import dev.buildtool.scp.clockworks.ClockworksEntity;
import dev.buildtool.scp.clockworks.ClockworksRecipe;
import dev.buildtool.scp.clockworks.Settings;
import dev.buildtool.scp.events.Entities;
import dev.buildtool.scp.events.Structures;
import dev.buildtool.scp.goals.GoalAction;
import dev.buildtool.scp.human.*;
import dev.buildtool.scp.lock.LockEntity;
import dev.buildtool.scp.lock.OpenLock;
import dev.buildtool.scp.lock.SetPassword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    public SCP() {
        int message = 0;
        channel= NetworkRegistry.newSimpleChannel(new ResourceLocation(ID,"channel1"),() -> "1",s -> s.equals("1"),s -> s.equals("1"));
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
            new ClientProxy<>().accept(packet, contextSupplier);
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
                context.getSender().sendMessage(new TranslationTextComponent("scp.activated.goal").append(new StringTextComponent(": " + activateGoal.goalAction.toString().toLowerCase())), UUID.randomUUID());
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

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, new ForgeConfigSpec.Builder().configure(builder -> {
            Structures.rarity = builder.comment("Higher rarity means less chambers").define("SCP chamber rarity", 240);
            writeClockworksRecipes = builder.define("Create a file listing all Clockworks recipes", true);
            toothBrushCanBreakUnbreakable = builder.define("SCP-063 can break unbreakable blocks", false);
            chamberDamage = builder.comment("Amount of damage appplied to outer walls of generated SCP chambers").defineInRange("SCP chamber damage", 0.0, 0.0, 1.0);
            chaosSoldierWeight = builder.defineInRange("Spawning frequency of Chaos Insurgency soldiers", 2, 0, 20);
            return builder.build();
        }).getRight());
    }


}
