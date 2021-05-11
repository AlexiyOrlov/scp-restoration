package dev.buildtool.scp.mailbox;

import dev.buildtool.satako.Container2;
import dev.buildtool.satako.ItemHandlerSlot;
import dev.buildtool.scp.events.SCPContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;

public class MailboxContainer extends Container2 {
    MailboxEntity mailboxEntity;

    public MailboxContainer(int i, PlayerInventory inventory, PacketBuffer packetBuffer) {
        super(SCPContainers.mailboxContainer, i);
        mailboxEntity = (MailboxEntity) inventory.player.level.getBlockEntity(packetBuffer.readBlockPos());
        addSlot(new ItemHandlerSlot(mailboxEntity.itemHandler, 0, 18 * 4, 0));
        addPlayerInventory(0, 40, inventory);
    }
}
