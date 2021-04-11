package dev.buildtool.scp.events;

import dev.buildtool.scp.SCP;
import dev.buildtool.scp.SiteFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.IOException;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Structures {
    public static SiteFeature scpSite;
    public static List<ResourceLocation> structureLootTables;
    public static ForgeConfigSpec.ConfigValue<Integer> rarity;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Feature<?>> featureRegister) throws IOException {
        IForgeRegistry<Feature<?>> registry = featureRegister.getRegistry();
        scpSite = new SiteFeature();
        registry.register(scpSite.setRegistryName(SCP.ID, "scp_site"));
    }

}
