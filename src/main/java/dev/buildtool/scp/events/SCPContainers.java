package dev.buildtool.scp.events;

import dev.buildtool.scp.SCP;
import dev.buildtool.scp.crate.CrateContainer;
import dev.buildtool.scp.human.InteractionContainer;
import dev.buildtool.scp.shelflife.ShelfContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCPContainers {
    public static ContainerType<CrateContainer> crateContainer;
    public static ContainerType<InteractionContainer> humanInterContainer;
    public static ContainerType<ShelfContainer> shelfContainer;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> register) {
        crateContainer = IForgeContainerType.create(CrateContainer::new);
        IForgeRegistry<ContainerType<?>> forgeRegistry = register.getRegistry();
        forgeRegistry.register(crateContainer.setRegistryName(SCP.ID, "crate"));
        humanInterContainer = IForgeContainerType.create(InteractionContainer::new);
        forgeRegistry.register(humanInterContainer.setRegistryName(SCP.ID, "human_interaction"));
        shelfContainer = IForgeContainerType.create(ShelfContainer::new);
        forgeRegistry.register(shelfContainer.setRegistryName(SCP.ID, "shelf_life"));
    }
}
