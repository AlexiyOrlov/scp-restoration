package dev.buildtool.scp.lootblock;

import dev.buildtool.satako.BlockEntity2;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class LootBlockEntity extends BlockEntity2 {
    private static final String IDENTIFIER = "Identifier";
    public String identifier = "";

    public LootBlockEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        p_189515_1_.putString(IDENTIFIER, identifier);
        return super.save(p_189515_1_);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        identifier = p_230337_2_.getString(IDENTIFIER);
    }
}
