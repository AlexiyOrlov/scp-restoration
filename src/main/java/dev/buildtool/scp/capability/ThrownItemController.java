package dev.buildtool.scp.capability;

import dev.buildtool.scp.SCP;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber
@SuppressWarnings("unused")
public class ThrownItemController {
    public static HashMap<ItemStack, Integer> worldItems = new HashMap<>();
    @SubscribeEvent
    public static void onItemThrow(ItemTossEvent tossEvent) {
        ItemStack stack = tossEvent.getEntityItem().getItem();
        PlayerEntity playerEntity = tossEvent.getPlayer();
        playerEntity.getCapability(ThrownItems.THROWNITEMS).ifPresent(thrownItemMemory -> {
            if (thrownItemMemory.thrownItems().size() < SCP.scp1162ItemLimit.get()) {
                thrownItemMemory.thrownItems().add(stack);
                worldItems.put(stack, tossEvent.getEntityItem().getId());
            }
        });
    }

    @SubscribeEvent
    public static void onItemPickedUp(PlayerEvent.ItemPickupEvent itemPickupEvent) {
        ItemStack itemStack = itemPickupEvent.getStack();
        PlayerEntity playerEntity = itemPickupEvent.getPlayer();
        playerEntity.getCapability(ThrownItems.THROWNITEMS).ifPresent(thrownItemMemory -> {
            thrownItemMemory.thrownItems().remove(itemStack);
            worldItems.remove(itemStack);
        });
    }
}
