package dev.buildtool.scp.goals;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;
import java.util.Set;

public class SeekItems extends Goal {

    protected MobEntity mobEntity;
    protected Set<IItemProvider> toPickupItems;
    protected Set<ITag<Item>> tagsToPickup;

    public SeekItems(MobEntity mobEntity, Set<IItemProvider> itemProviders, Set<ITag<Item>> tagSet) {
        this.mobEntity = mobEntity;
        mobEntity.setCanPickUpLoot(true);
        toPickupItems = itemProviders;
        tagsToPickup = tagSet;
        assert !toPickupItems.isEmpty() || !tagsToPickup.isEmpty();
    }

    @Override
    public boolean canUse() {
        return mobEntity.getMainHandItem().isEmpty();
    }

    @Override
    public void tick() {
        super.tick();
        double range = mobEntity.getAttributeValue(Attributes.FOLLOW_RANGE);
        List<ItemEntity> itemEntities = mobEntity.level.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(mobEntity.blockPosition()).inflate(range), itemEntity -> toPickupItems.contains(itemEntity.getItem().getItem()) || tagsToPickup.stream().anyMatch(itemITag -> itemITag.contains(itemEntity.getItem().getItem())));
        itemEntities.stream().reduce((itemEntity, itemEntity2) -> mobEntity.distanceToSqr(itemEntity) < mobEntity.distanceToSqr(itemEntity2) ? itemEntity : itemEntity2).ifPresent(itemEntity -> mobEntity.getNavigation().moveTo(itemEntity, 1));
    }
}
