package dev.buildtool.scp.clockworks;

import net.minecraft.util.math.BlockPos;

public class Settings {

    public boolean automaticInput;
    public boolean isWorkind;
    public ClockworksRecipe.Mode mode;
    public BlockPos pos;
    public Settings(boolean autoInput, ClockworksRecipe.Mode mode, boolean working, BlockPos pos) {
        this.automaticInput=autoInput;
        this.isWorkind=working;
        this.mode=mode;
        this.pos=pos;
    }
}
