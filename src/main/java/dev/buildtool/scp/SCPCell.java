package dev.buildtool.scp;

import dev.buildtool.scp.lootblock.LootBlockEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Collections;

public class SCPCell extends Template2 {
    protected String scp;

    /**
     * @param template wrapped template
     * @param scp      number
     */
    public SCPCell(Template template, String scp, ISeedReader seedReader) {
        super(template, Collections.emptyList(), seedReader);
        this.scp = scp;
    }

    @Override
    protected void addLoot(TileEntity tileEntity, Rotation placementRotation) {
        if (tileEntity instanceof LootBlockEntity) {
            LootBlockEntity lootBlockEntity = (LootBlockEntity) tileEntity;
//            Set<RandomLoot> randomLoots = ChamberLootManager.identifiedRandomLootHashMultimap.get(this.scp);
//            for (RandomLoot randomLoot : randomLoots) {
//                if (randomLoot instanceof IdentifiedRandomLoot) {
//                    IdentifiedRandomLoot identifiedRandomLoot = (IdentifiedRandomLoot) randomLoot;
//                    if (lootBlockEntity.identifier.equals(identifiedRandomLoot.target)) {
//                        seedReader.setBlock(lootBlockEntity.getBlockPos(), identifiedRandomLoot.container, 1);
//                        TileEntity blockentity = seedReader.getBlockEntity(lootBlockEntity.getBlockPos());
//                        blockentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(identifiedRandomLoot::generateInto);
//                        break;
//                    }
//                }
//            }

        }
    }
}
