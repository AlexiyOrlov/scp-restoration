package dev.buildtool.scp.harddrivecracker;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.satako.Functions;
import dev.buildtool.satako.ItemHandler;
import dev.buildtool.scp.SCP;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class HardDriveCrackerEntity extends BlockEntity2 implements ITickableTileEntity, INamedContainerProvider {
    public int time=Functions.minutesToTicks(15);
    ItemHandler itemHandler;
    public boolean inProgress;
    public HardDriveCrackerEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        itemHandler=new ItemHandler(1);
    }

    @Override
    public void tick() {
        if(!getLevel().isClientSide) {
            if (!itemHandler.getItems().isEmpty()) {
                if (inProgress) {
                    if (time == 0) {
                        //hard drive cracked
                        itemHandler.getStackInSlot(0).getOrCreateTag().putBoolean(HardDrive.CRACKED, true);
                        time = Functions.minutesToTicks(15);
                        inProgress = false;
                    } else
                        time--;
                    if(level.getDayTime() %20==0){
                        SCP.channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(getX(),getY(),getZ(),10,level.dimension())),new CrackingProgress(getBlockPos(),time));
                    }
                }
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        p_189515_1_.put("Content",itemHandler.serializeNBT());
        p_189515_1_.putInt("Time left",time);
        p_189515_1_.putBoolean("Is cracking",inProgress);
        return super.save(p_189515_1_);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        itemHandler.deserializeNBT(p_230337_2_.getCompound("Content"));
        time=p_230337_2_.getInt("Time left");
        inProgress=p_230337_2_.getBoolean("Is cracking");
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("scp.hard_drive_cracker");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        PacketBuffer packetBuffer=Functions.emptyBuffer();
        packetBuffer.writeBlockPos(getBlockPos());
        return new HardDriveCrackerContainer(p_createMenu_1_,p_createMenu_2_,packetBuffer);
    }
}
