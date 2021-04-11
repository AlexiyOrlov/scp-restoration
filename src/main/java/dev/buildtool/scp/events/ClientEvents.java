package dev.buildtool.scp.events;

import dev.buildtool.scp.capability.InfoScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent keyInputEvent) {
        int key = keyInputEvent.getKey();
        if (key == ClientModEvents.openSCPInfo.getKey().getValue() && Minecraft.getInstance().screen == null) {
            Minecraft.getInstance().setScreen(new InfoScreen(Minecraft.getInstance().player, new StringTextComponent("")));
        }
    }
}
