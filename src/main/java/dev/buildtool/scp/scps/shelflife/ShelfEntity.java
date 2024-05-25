package dev.buildtool.scp.scps.shelflife;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.satako.Functions;
import dev.buildtool.satako.ItemHandler;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Transforms enchanted books every 3 minutes
 */
public class ShelfEntity extends BlockEntity2 implements ITickableTileEntity, INamedContainerProvider {
    ItemHandler book1 = new ItemHandler(1);
    ItemHandler book2 = new ItemHandler(1);
    ItemHandler book3 = new ItemHandler(1);
    ItemHandler book4 = new ItemHandler(1);
    ItemHandler book5 = new ItemHandler(1);
    ItemHandler book6 = new ItemHandler(1);
    ItemHandler book7 = new ItemHandler(1);
    ItemHandler book8 = new ItemHandler(1);
    ItemHandler book9 = new ItemHandler(1);

    public ArrayList<ItemHandler> books = new ArrayList<>(Arrays.asList(book1, book2, book3, book4, book5, book6, book7, book8, book9));

    HashMap<ItemHandler, Integer> transformationTimes = new HashMap<>(9);

    public ShelfEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        int minutes = Functions.minutesToTicks(3);
        transformationTimes.put(book1, minutes);
        transformationTimes.put(book2, minutes);
        transformationTimes.put(book3, minutes);
        transformationTimes.put(book4, minutes);
        transformationTimes.put(book5, minutes);
        transformationTimes.put(book6, minutes);
        transformationTimes.put(book7, minutes);
        transformationTimes.put(book8, minutes);
        transformationTimes.put(book9, minutes);
    }

    @Override
    public void tick() {
        if (!level.isClientSide) {
            int bookAmount = 0;
            for (ItemHandler book : books) {
                if (!book.isEmpty()) {
                    bookAmount++;
                    if (bookAmount >= 2)
                        break;
                }
            }
            if (bookAmount >= 2) {
                transformationTimes.forEach((itemHandler, integer) -> {
                    if (!itemHandler.getStackInSlot(0).isEmpty()) {
                        transformationTimes.put(itemHandler, integer - 1);
                        if (integer == 0) {
                            itemHandler.setStackInSlot(0, EnchantmentHelper.enchantItem(level.random, new ItemStack(Items.BOOK), level.random.nextInt(30) + 1, true));
                            transformationTimes.put(itemHandler, Functions.minutesToTicks(3));
                        }
                    }
                });
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.put("Book1", book1.serializeNBT());
        compound.put("Book2", book2.serializeNBT());
        compound.put("Book3", book3.serializeNBT());
        compound.put("Book4", book4.serializeNBT());
        compound.put("Book5", book5.serializeNBT());
        compound.put("Book6", book6.serializeNBT());
        compound.put("Book7", book7.serializeNBT());
        compound.put("Book8", book8.serializeNBT());
        compound.put("Book9", book9.serializeNBT());
        int num = 0;
        for (ItemHandler itemHandler : transformationTimes.keySet()) {
            int time = transformationTimes.get(itemHandler);
            compound.putInt("Time #" + num++, time);
        }
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        book1.deserializeNBT(nbt.getCompound("Book1"));
        book2.deserializeNBT(nbt.getCompound("Book2"));
        book3.deserializeNBT(nbt.getCompound("Book3"));
        book4.deserializeNBT(nbt.getCompound("Book4"));
        book5.deserializeNBT(nbt.getCompound("Book5"));
        book6.deserializeNBT(nbt.getCompound("Book6"));
        book7.deserializeNBT(nbt.getCompound("Book7"));
        book8.deserializeNBT(nbt.getCompound("Book8"));
        book9.deserializeNBT(nbt.getCompound("Book9"));
        for (int i = 0; i < 9; i++) {
            int time = nbt.getInt("Time #" + i);
            transformationTimes.put(books.get(i), time);
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Shelf life entity");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeBlockPos(getBlockPos());
        return new ShelfContainer(p_createMenu_1_, p_createMenu_2_, packetBuffer);
    }
}
