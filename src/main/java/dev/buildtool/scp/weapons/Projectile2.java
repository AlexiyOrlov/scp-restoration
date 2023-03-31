package dev.buildtool.scp.weapons;

import dev.buildtool.scp.SCP;
import dev.buildtool.scp.human.Human;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class Projectile2 extends DamagingProjectileEntity {
    private float damage;

    protected Projectile2(EntityType<? extends DamagingProjectileEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public Projectile2(EntityType<? extends DamagingProjectileEntity> p_i50174_1_, double p_i50174_2_, double p_i50174_4_, double p_i50174_6_, double p_i50174_8_, double p_i50174_10_, double p_i50174_12_, World p_i50174_14_) {
        super(p_i50174_1_, p_i50174_2_, p_i50174_4_, p_i50174_6_, p_i50174_8_, p_i50174_10_, p_i50174_12_, p_i50174_14_);
    }

    public Projectile2(EntityType<? extends DamagingProjectileEntity> p_i50175_1_, LivingEntity p_i50175_2_, double p_i50175_3_, double p_i50175_5_, double p_i50175_7_, World p_i50175_9_) {
        super(p_i50175_1_, p_i50175_2_, p_i50175_3_, p_i50175_5_, p_i50175_7_, p_i50175_9_);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected float getInertia() {
        return 1;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
        Entity entity = entityRayTraceResult.getEntity();
        Entity owner = getOwner();
        if (owner instanceof PlayerEntity) {
            if (entity instanceof Human) {
                Human human = (Human) entity;
                if (human.getOwner() == null || !human.getOwner().equals(owner.getUUID())) {
                    human.hurt(DamageSource.playerAttack((PlayerEntity) owner), damage);
                }
            } else entity.hurt(DamageSource.playerAttack((PlayerEntity) owner), damage);
        } else entity.hurt(new DamageSource(SCP.ID + ".projectile"), damage);
        if (ownerUUID != null && !ownerUUID.equals(entity.getUUID()))
            remove();
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
        super.onHitBlock(p_230299_1_);
        remove();
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    public void shootFromRotation(Entity p_234612_1_, float p_234612_2_, float p_234612_3_, float p_234612_4_, float p_234612_5_, float p_234612_6_) {
        float f = -MathHelper.sin(p_234612_3_ * ((float) Math.PI / 180F)) * MathHelper.cos(p_234612_2_ * ((float) Math.PI / 180F));
        float f1 = -MathHelper.sin((p_234612_2_ + p_234612_4_) * ((float) Math.PI / 180F));
        float f2 = MathHelper.cos(p_234612_3_ * ((float) Math.PI / 180F)) * MathHelper.cos(p_234612_2_ * ((float) Math.PI / 180F));
        this.shoot(f, f1, f2, p_234612_5_, p_234612_6_);
        Vector3d vector3d = p_234612_1_.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vector3d.x, vector3d.y, vector3d.z));
    }
}
