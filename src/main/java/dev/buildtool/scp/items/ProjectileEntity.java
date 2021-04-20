package dev.buildtool.scp.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class ProjectileEntity extends Entity {
   protected UUID ownerUUID;
   protected int ownerNetworkId;
   protected boolean leftOwner;

   public ProjectileEntity(EntityType<? extends ProjectileEntity> p_i231584_1_, World p_i231584_2_) {
      super(p_i231584_1_, p_i231584_2_);
   }

   public void setOwner(@Nullable Entity entity) {
      if (entity != null) {
         this.ownerUUID = entity.getUUID();
         this.ownerNetworkId = entity.getId();
      }

   }

   @Nullable
   public Entity getOwner() {
      if (this.ownerUUID != null && this.level instanceof ServerWorld) {
         return ((ServerWorld)this.level).getEntity(this.ownerUUID);
      } else {
         return this.ownerNetworkId != 0 ? this.level.getEntity(this.ownerNetworkId) : null;
      }
   }

   protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {
      if (this.ownerUUID != null) {
         p_213281_1_.putUUID("Owner", this.ownerUUID);
      }

      if (this.leftOwner) {
         p_213281_1_.putBoolean("LeftOwner", true);
      }

   }

   protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {
      if (p_70037_1_.hasUUID("Owner")) {
         this.ownerUUID = p_70037_1_.getUUID("Owner");
      }
      this.leftOwner = p_70037_1_.getBoolean("LeftOwner");
   }

   public void tick() {
      if (!this.leftOwner) {
         this.leftOwner = this.checkLeftOwner();
      }
      Vector3d vector3d=getDeltaMovement();
      RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
      if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
         this.onHit(raytraceresult);
      }
      double d = vector3d.x;
      double e = vector3d.y;
      double g = vector3d.z;


      double h = this.getX() + d;
      double j = this.getY() + e;
      double k = this.getZ() + g;
      setPos(h,j,k);
      checkInsideBlocks();

      super.tick();
   }

   protected boolean checkLeftOwner() {
      Entity entity = this.getOwner();
      if (entity != null) {
         for(Entity entity1 : this.level.getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), (entity1) -> !entity1.isSpectator() && entity1.isPickable())) {
            if (entity1.getRootVehicle() == entity.getRootVehicle()) {
               return false;
            }
         }
      }
      return true;
   }

   public void shoot(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_) {
      Vector3d vector3d = (new Vector3d(p_70186_1_, p_70186_3_, p_70186_5_)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)p_70186_8_, this.random.nextGaussian() * (double)0.0075F * (double)p_70186_8_, this.random.nextGaussian() * (double)0.0075F * (double)p_70186_8_).scale(p_70186_7_);
      this.setDeltaMovement(vector3d);
      float f = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
      this.yRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
      this.xRot = (float)(MathHelper.atan2(vector3d.y, f) * (double)(180F / (float)Math.PI));
      this.yRotO = this.yRot;
      this.xRotO = this.xRot;
   }

   public void shootFromRotation(Entity p_234612_1_, float p_234612_2_, float p_234612_3_, float p_234612_4_, float p_234612_5_, float p_234612_6_) {
      float f = -MathHelper.sin(p_234612_3_ * ((float)Math.PI / 180F)) * MathHelper.cos(p_234612_2_ * ((float)Math.PI / 180F));
      float f1 = -MathHelper.sin((p_234612_2_ + p_234612_4_) * ((float)Math.PI / 180F));
      float f2 = MathHelper.cos(p_234612_3_ * ((float)Math.PI / 180F)) * MathHelper.cos(p_234612_2_ * ((float)Math.PI / 180F));
      this.shoot(f, f1, f2, p_234612_5_, p_234612_6_);
      Vector3d vector3d = p_234612_1_.getDeltaMovement();
      this.setDeltaMovement(this.getDeltaMovement().add(vector3d.x, p_234612_1_.isOnGround() ? 0.0D : vector3d.y, vector3d.z));
   }

   protected void onHit(RayTraceResult p_70227_1_) {
      RayTraceResult.Type raytraceresult$type = p_70227_1_.getType();
      if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
         this.onHitEntity((EntityRayTraceResult)p_70227_1_);
      } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
         this.onHitBlock((BlockRayTraceResult)p_70227_1_);
      }

   }

   protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
      Entity playerEntity=getOwner();
      entityRayTraceResult.getEntity().hurt(DamageSource.mobAttack((LivingEntity) playerEntity),1);
      entityRayTraceResult.getEntity().invulnerableTime=0;
      remove();
   }

   protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
      remove();
   }

   @Override
   protected void onInsideBlock(BlockState p_191955_1_) {


   }

   @OnlyIn(Dist.CLIENT)
   public void lerpMotion(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
      this.setDeltaMovement(p_70016_1_, p_70016_3_, p_70016_5_);
      if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
         float f = MathHelper.sqrt(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
         this.xRot = (float)(MathHelper.atan2(p_70016_3_, f) * (double)(180F / (float)Math.PI));
         this.yRot = (float)(MathHelper.atan2(p_70016_1_, p_70016_5_) * (double)(180F / (float)Math.PI));
         this.xRotO = this.xRot;
         this.yRotO = this.yRot;
         this.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
      }

   }

   protected boolean canHitEntity(Entity p_230298_1_) {
      if (!p_230298_1_.isSpectator() && p_230298_1_.isAlive() && p_230298_1_.isPickable()) {
         Entity entity = this.getOwner();
         return entity == null || this.leftOwner || !entity.isPassengerOfSameVehicle(p_230298_1_);
      } else {
         return false;
      }
   }

   protected void updateRotation() {
      Vector3d vector3d = this.getDeltaMovement();
      float f = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
      this.xRot = lerpRotation(this.xRotO, (float)(MathHelper.atan2(vector3d.y, f) * (double)(180F / (float)Math.PI)));
      this.yRot = lerpRotation(this.yRotO, (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI)));
   }

   protected static float lerpRotation(float p_234614_0_, float p_234614_1_) {
      while(p_234614_1_ - p_234614_0_ < -180.0F) {
         p_234614_0_ -= 360.0F;
      }

      while(p_234614_1_ - p_234614_0_ >= 180.0F) {
         p_234614_0_ += 360.0F;
      }

      return MathHelper.lerp(0.2F, p_234614_0_, p_234614_1_);
   }
}