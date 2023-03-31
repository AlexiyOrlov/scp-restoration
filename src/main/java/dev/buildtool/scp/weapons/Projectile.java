package dev.buildtool.scp.weapons;

import dev.buildtool.satako.InanimateEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.UUID;
@Deprecated
public abstract class Projectile extends InanimateEntity {

   protected UUID ownerNetworkId;
   protected boolean leftOwner;
   protected int damage;
   protected double lightness;
   protected int invulAfterImpact;

   /**
    * @param lightness                  0 to 1
    */
   public Projectile(EntityType<? extends Projectile> p_i231584_1_, World p_i231584_2_, int damage_, double lightness, int invulnerabilityAfterImpact) {
      super(p_i231584_1_, p_i231584_2_);
      setSilent(true);
      this.damage = damage_;
      this.lightness = lightness;
      this.invulAfterImpact = invulnerabilityAfterImpact;
   }

   public void setOwner(@Nullable Entity entity) {
      if (entity != null) {
         this.ownerNetworkId = entity.getUUID();
      }
   }

   @Override
   protected void pushEntities() {

   }

   @Nullable
   public Entity getOwner() {
      if (ownerNetworkId != null) {
         PlayerEntity playerEntity = level.getPlayerByUUID(ownerNetworkId);
         if (playerEntity != null)
            return playerEntity;
      }
      return ownerNetworkId != null && level instanceof ServerWorld ? ((ServerWorld) level).getEntity(ownerNetworkId) : null;
   }

   public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
      super.addAdditionalSaveData(p_213281_1_);

      if (this.leftOwner) {
         p_213281_1_.putBoolean("LeftOwner", true);
      }

   }

   public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
      super.readAdditionalSaveData(p_70037_1_);
      this.leftOwner = p_70037_1_.getBoolean("LeftOwner");
   }

   public void tick() {
      if (!this.leftOwner) {
         this.leftOwner = this.checkLeftOwner();
      }
      RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
      if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
         this.onHit(raytraceresult);
      }
      super.tick();
   }

   @Override
   public void travel(Vector3d p_213352_1_) {
      if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
         double d0;
         ModifiableAttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
         boolean flag = this.getDeltaMovement().y <= 0.0D;
         d0 = gravity.getValue();

         FluidState fluidstate = this.level.getFluidState(this.blockPosition());
         if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
            double d8 = this.getY();
            float f5 = this.getWaterSlowDown();
            float f6 = 0.02F;
            float f7 = 0;

            if (!this.onGround) {
               f7 *= 0.5F;
            }

            if (f7 > 0.0F) {
               f5 += (0.54600006F - f5) * f7 / 3.0F;
               f6 += (this.getSpeed() - f6) * f7 / 3.0F;
            }

            f6 *= (float)this.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
            this.moveRelative(f6, p_213352_1_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            Vector3d vector3d6 = this.getDeltaMovement();
            if (this.horizontalCollision && this.onClimbable()) {
               vector3d6 = new Vector3d(vector3d6.x, 0.2D, vector3d6.z);
            }

            this.setDeltaMovement(vector3d6.multiply(f5, 0.8F, f5));
            Vector3d vector3d2 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
            this.setDeltaMovement(vector3d2);
            if (this.horizontalCollision && this.isFree(vector3d2.x, vector3d2.y + (double)0.6F - this.getY() + d8, vector3d2.z)) {
               this.setDeltaMovement(vector3d2.x, 0.3F, vector3d2.z);
            }
         } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
            double d7 = this.getY();
            this.moveRelative(0.02F, p_213352_1_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
               this.setDeltaMovement(this.getDeltaMovement().multiply(0.5D, 0.8F, 0.5D));
               Vector3d vector3d3 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
               this.setDeltaMovement(vector3d3);
            } else {
               this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            }

            if (!this.isNoGravity()) {
               this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -d0 / 4.0D, 0.0D));
            }

            Vector3d vector3d4 = this.getDeltaMovement();
            if (this.horizontalCollision && this.isFree(vector3d4.x, vector3d4.y + (double)0.6F - this.getY() + d7, vector3d4.z)) {
               this.setDeltaMovement(vector3d4.x, 0.3F, vector3d4.z);
            }
         } else if (this.isFallFlying()) {
            Vector3d vector3d = this.getDeltaMovement();
            if (vector3d.y > -0.5D) {
               this.fallDistance = 1.0F;
            }

            Vector3d vector3d1 = this.getLookAngle();
            float f = this.xRot * ((float)Math.PI / 180F);
            double d1 = Math.sqrt(vector3d1.x * vector3d1.x + vector3d1.z * vector3d1.z);
            double d3 = Math.sqrt(getHorizontalDistanceSqr(vector3d));
            double d4 = vector3d1.length();
            float f1 = MathHelper.cos(f);
            f1 = (float)((double)f1 * (double)f1 * Math.min(1.0D, d4 / 0.4D));
            vector3d = this.getDeltaMovement().add(0.0D, d0 * (-1.0D + (double)f1 * 0.75D), 0.0D);
            if (vector3d.y < 0.0D && d1 > 0.0D) {
               double d5 = vector3d.y * -0.1D * (double)f1;
               vector3d = vector3d.add(vector3d1.x * d5 / d1, d5, vector3d1.z * d5 / d1);
            }

            if (f < 0.0F && d1 > 0.0D) {
               double d9 = d3 * (double)(-MathHelper.sin(f)) * 0.04D;
               vector3d = vector3d.add(-vector3d1.x * d9 / d1, d9 * 3.2D, -vector3d1.z * d9 / d1);
            }

            if (d1 > 0.0D) {
               vector3d = vector3d.add((vector3d1.x / d1 * d3 - vector3d.x) * 0.1D, 0.0D, (vector3d1.z / d1 * d3 - vector3d.z) * 0.1D);
            }

            this.setDeltaMovement(vector3d.multiply(0.99F, 0.98F, 0.99F));
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (this.horizontalCollision && !this.level.isClientSide) {
               double d10 = Math.sqrt(getHorizontalDistanceSqr(this.getDeltaMovement()));
               double d6 = d3 - d10;
               float f2 = (float)(d6 * 10.0D - 3.0D);
               if (f2 > 0.0F) {
                  this.playSound(this.getFallDamageSound((int)f2), 1.0F, 1.0F);
                  this.hurt(DamageSource.FLY_INTO_WALL, f2);
               }
            }

            if (this.onGround && !this.level.isClientSide) {
               this.setSharedFlag(7, false);
            }
         } else {
            BlockPos blockpos = this.getBlockPosBelowThatAffectsMyMovement();
            float f3 = this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getSlipperiness(level, this.getBlockPosBelowThatAffectsMyMovement(), this);
            float f4 = this.onGround ? f3 * 0.91F : 0.91F;
            Vector3d vector3d5 = this.handleRelativeFrictionAndCalculateMovement(p_213352_1_, f3);
            double d2 = vector3d5.y;
            if (this.level.isClientSide && !this.level.hasChunkAt(blockpos)) {
               if (this.getY() > 0.0D) {
                  d2 = -0.1D;
               } else {
                  d2 = 0.0D;
               }
            } else if (!this.isNoGravity()) {
               d2 -= d0;
            }

            this.setDeltaMovement(vector3d5.x , d2+d0* lightness, vector3d5.z );
//            updateRotation();
         }
      }

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
      Entity owner=getOwner();
      Entity traced = entityRayTraceResult.getEntity();
      if(traced !=owner && traced.getClass()!=getClass()) {
         traced.hurt(DamageSource.mobAttack((LivingEntity) owner), damage);
         traced.invulnerableTime = invulAfterImpact;
         if (!level.isClientSide)
            remove();
      }
   }

   protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
      remove();
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
      while (p_234614_1_ - p_234614_0_ < -180.0F) {
         p_234614_0_ -= 360.0F;
      }

      while (p_234614_1_ - p_234614_0_ >= 180.0F) {
         p_234614_0_ += 360.0F;
      }

      return MathHelper.lerp(0.2F, p_234614_0_, p_234614_1_);
   }

   @Override
   public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
      return false;
   }

   @Override
   protected boolean isMovementNoisy() {
      return false;
   }
}