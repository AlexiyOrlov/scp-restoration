package dev.buildtool.scp.registration;

import dev.buildtool.scp.SCP;
import dev.buildtool.scp.radioactbananas.BananaDeathEffect;
import dev.buildtool.scp.radioactbananas.Radiation;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCPEffects {

    public static BananaDeathEffect bananaDeathEffect;
    public static Radiation radiation;
    public static Effect insomnia;
    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Effect> effectRegister) {
        IForgeRegistry<Effect> forgeRegistry = effectRegister.getRegistry();
        bananaDeathEffect = new BananaDeathEffect(EffectType.HARMFUL, 0xAF942F);
        forgeRegistry.register(bananaDeathEffect.setRegistryName(SCP.ID, "bananas"));
        radiation = new Radiation(EffectType.HARMFUL, 0x42AF35);
        forgeRegistry.register(radiation.setRegistryName(SCP.ID, "radiation"));
        insomnia = new Effect(EffectType.HARMFUL, 0xff0000) {
        };
        forgeRegistry.register(insomnia.setRegistryName(SCP.ID, "insomnia"));
    }
}
