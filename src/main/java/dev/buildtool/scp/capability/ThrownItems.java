package dev.buildtool.scp.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ThrownItems {

    @CapabilityInject(ThrownItemMemory.class)
    public static Capability<ThrownItemMemory> THROWNITEMS;

    public interface ThrownItemMemory {
        List<ItemStack> thrownItems();
    }

    public static class Storage implements Capability.IStorage<ThrownItemMemory> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<ThrownItemMemory> capability, ThrownItemMemory instance, Direction side) {
            CompoundNBT compoundNBT = new CompoundNBT();
            instance.thrownItems().forEach(itemStack -> compoundNBT.put(itemStack.hashCode() + "", itemStack.serializeNBT()));
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<ThrownItemMemory> capability, ThrownItemMemory instance, Direction side, INBT nbt) {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;
            compoundNBT.getAllKeys().forEach(s -> instance.thrownItems().add(ItemStack.of(compoundNBT.getCompound(s))));
        }
    }

    public static class ThrownItemsImpl implements ThrownItemMemory {
        private final ArrayList<ItemStack> itemStacks = new ArrayList<>();

        @Override
        public List<ItemStack> thrownItems() {
            return itemStacks;
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        private LazyOptional<ThrownItemMemory> thrownItemMemoryLazyOptional = LazyOptional.of(ThrownItemsImpl::new);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return THROWNITEMS.orEmpty(cap, thrownItemMemoryLazyOptional);
        }

        @Override
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) THROWNITEMS.writeNBT(thrownItemMemoryLazyOptional.orElse(null), null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            THROWNITEMS.readNBT(thrownItemMemoryLazyOptional.orElse(null), null, nbt);
        }
    }
}
