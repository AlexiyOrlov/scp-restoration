package dev.buildtool.scp.scps.smallgirl;

import dev.buildtool.satako.UniqueList;
import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.scps.sculpture.Sculpture;
import dev.buildtool.scp.scps.shyguy.ShyguyEntity;
import dev.buildtool.scp.scps.swatarmor.SwatArmorEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

@SCPObject(number = "053", name = "Young Girl", classification = SCPObject.Classification.EUCLID)
public class YoungGirl extends SCPEntity {

    private final UniqueList<MobEntity> affected = new UniqueList<>();

    public YoungGirl(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SwimGoal(this));
        goalSelector.addGoal(10, new RandomWalkingGoal(this, 1));
        goalSelector.addGoal(11, new LookRandomlyGoal(this));
        goalSelector.addGoal(12, new LookAtGoal(this, LivingEntity.class, 8));
    }

    @Override
    public void tick() {
        super.tick();
        List<MobEntity> livingEntities = level.getEntitiesOfClass(MobEntity.class, new AxisAlignedBB(blockPosition()).inflate(16), livingEntity -> !affected.contains(livingEntity) && livingEntity.getAttribute(Attributes.ATTACK_DAMAGE) != null);
        livingEntities.removeIf(mobEntity -> mobEntity instanceof ShyguyEntity || mobEntity instanceof Sculpture || mobEntity instanceof SwatArmorEntity);
        livingEntities.forEach(livingEntity -> {
            if (livingEntity.canSee(this)) {
                livingEntity.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(livingEntity, YoungGirl.class, false, false));
                affected.add(livingEntity);
            }
        });
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity) {
            source.getEntity().hurt(source, amount);
            return false;
        } else if (source.getDirectEntity() instanceof LivingEntity) {
            source.getDirectEntity().hurt(source, amount);
            return false;
        }
        return super.hurt(source, amount);
    }
}
