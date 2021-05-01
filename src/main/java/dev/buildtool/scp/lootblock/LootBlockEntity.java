package dev.buildtool.scp.lootblock;

import dev.buildtool.satako.BlockEntity2;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class LootBlockEntity extends BlockEntity2 {
    private static final String IDENTIFIER = "Identifier", BLOCKSTATE = "Blockstate";
    public String identifier = "";
    public int storedBlockstate;

    public LootBlockEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        p_189515_1_.putString(IDENTIFIER, identifier);
        p_189515_1_.putInt(BLOCKSTATE, storedBlockstate);
        return super.save(p_189515_1_);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        identifier = p_230337_2_.getString(IDENTIFIER);
        storedBlockstate = p_230337_2_.getInt(BLOCKSTATE);
    }
}
