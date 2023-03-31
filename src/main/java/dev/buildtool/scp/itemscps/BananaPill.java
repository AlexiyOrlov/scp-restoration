package dev.buildtool.scp.itemscps;

import dev.buildtool.scp.SCPObject;
import net.minecraft.item.Item;

@SCPObject(number = "3521", name = "Forced Banana Equivalent Dose by dado", classification = SCPObject.Classification.SAFE)
public class BananaPill extends Item {
    public BananaPill(Properties properties) {
        super(properties);
    }
}
