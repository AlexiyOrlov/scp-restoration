package dev.buildtool.scp;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Collections;
//TODO check for damage application
public class TemplateWithRandomLoot extends Template2 {
    RandomLoot randomLoot;

    public TemplateWithRandomLoot(Template template, RandomLoot loot, ISeedReader seedReader) {
        super(template, Collections.emptyList(), seedReader, false);
        this.randomLoot = loot;
    }

    @Override
    protected void addLoot(TileEntity tileEntity, Rotation placementRotation) {
        if (!(tileEntity instanceof ISidedInventory))
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> randomLoot.generateInto(itemHandler));
    }
}
