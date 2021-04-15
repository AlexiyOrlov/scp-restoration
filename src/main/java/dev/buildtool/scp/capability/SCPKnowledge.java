package dev.buildtool.scp.capability;

import dev.buildtool.scp.SCP;
import dev.buildtool.scp.SCPObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SCPKnowledge {

    @CapabilityInject(Knowledge.class)
    public static Capability<Knowledge> KNOWLEDGE;
    public interface Knowledge
    {
        default List<String> knownSCPNumbers() {
            return new ArrayList<>(knownSCPData().keySet());
        }

        /**
         * Keys are scp numbers
         * @return SCP data map
         */
        Map<String,Data> knownSCPData();

    }

    public static class Data
    {
        public String number;
        public SCPObject.Classification classification;
        public String officialName;

        public Data() {
        }

        public Data(String number, SCPObject.Classification classification, String officialName) {
            this.number = number;
            this.classification = classification;
            this.officialName = officialName;
        }

//        public List<String> information;
        public CompoundNBT saveTo(CompoundNBT compoundNBT)
        {
            compoundNBT.putString("Number",number);
            compoundNBT.putString("Class",classification.name());
            compoundNBT.putString("Official name",officialName);
            return compoundNBT;
        }

        public Data loadFrom(CompoundNBT compoundNBT)
        {
            number=compoundNBT.getString("Number");
            classification= SCPObject.Classification.valueOf(compoundNBT.getString("Class"));
            officialName=compoundNBT.getString("Official name");
            return this;
        }
    }
    public static class Storage implements Capability.IStorage<Knowledge>
    {
        @Nullable
        @Override
        public INBT writeNBT(Capability<Knowledge> capability, Knowledge instance, Direction side) {
            CompoundNBT compoundNBT=new CompoundNBT();
            Map<String,Data> dataMap= instance.knownSCPData();
            dataMap.forEach((s, data) -> compoundNBT.put(s, data.saveTo(new CompoundNBT())));
            SCP.channel.send(PacketDistributor.ALL.noArg(), new Packet(instance));
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<Knowledge> capability, Knowledge instance, Direction side, INBT nbt) {
            CompoundNBT compoundNBT= (CompoundNBT) nbt;
            compoundNBT.getAllKeys().forEach(s -> {
                CompoundNBT compound=compoundNBT.getCompound(s);
                Data data=new Data();
                instance.knownSCPData().put(s,data.loadFrom(compound));
            });
            SCP.channel.send(PacketDistributor.ALL.noArg(), new Packet(instance));
        }
    }

    public static class KnowledgeImpl implements Knowledge
    {
        TreeMap<String, Data> data = new TreeMap<>();
        @Override
        public Map<String, Data> knownSCPData() {
            return data;
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT>
    {
        KnowledgeImpl knowledge=new KnowledgeImpl();
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if(cap==KNOWLEDGE)
                return LazyOptional.of(() -> knowledge).cast();
            return LazyOptional.empty();
        }


        @Override
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) KNOWLEDGE.writeNBT(knowledge,null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            KNOWLEDGE.readNBT(knowledge,null,nbt);
        }
    }
}
