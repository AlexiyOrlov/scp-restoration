package dev.buildtool.scp;

import net.minecraft.block.BlockState;

public class IdentifiedRandomLoot extends RandomLoot {
    protected String target;
    protected BlockState container;

    /**
     * @param target    matching {@link dev.buildtool.scp.lootblock.LootBlockEntity#identifier}
     * @param container block to replace {@link dev.buildtool.scp.lootblock.LootBlock} with
     */
    public IdentifiedRandomLoot(String target, BlockState container) {
        this.target = target;
        this.container = container;
    }
}
