package dev.buildtool.scp.events;

import dev.buildtool.scp.SCP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Sounds {

    public static SoundEvent rubberDucky;
    public static SoundEvent manSobbing;
    public static SoundEvent angryScreams;
    public static SoundEvent scp3199Idle;
    public static SoundEvent shyguyAttack;
    public static SoundEvent shyguyHurt;
    public static SoundEvent scp1437;
    public static SoundEvent skeletonKeyUnlock;
    public static SoundEvent doorClosed;
    public static SoundEvent rifleShot;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> soundEventRegister) {
        IForgeRegistry<SoundEvent> registry = soundEventRegister.getRegistry();
        registry.register(rubberDucky = register("rubber_ducky"));
        registry.register(manSobbing = register("sobbing_man"));
        registry.register(angryScreams = register("angry_screams"));
        registry.register(scp3199Idle = register("scp3199"));
        registry.register(shyguyAttack = register("flesh_punch"));
        registry.register(shyguyHurt = register("growl"));
        registry.register(scp1437 = register("scp1437"));
        registry.register(skeletonKeyUnlock = register("unlock"));
        registry.register(doorClosed = register("door_closed"));
        registry.register(rifleShot = register("shot"));
    }

    private static SoundEvent register(String name) {
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(SCP.ID, name));
        soundEvent.setRegistryName(name);
        return soundEvent;
    }
}
