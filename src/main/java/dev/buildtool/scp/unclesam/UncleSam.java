package dev.buildtool.scp.unclesam;

import com.google.common.collect.Lists;
import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCPEntity;
import dev.buildtool.scp.SCPObject;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

/**
 * 1:5 ration conversion of bad meat to good; pacifist
 */
@SCPObject(name = "Uncle Sam: The All-American Karcist", number = "5707", classification = SCPObject.Classification.EUCLID)
public class UncleSam extends SCPEntity {
    private int transformationTime;
    static final List<Item> FOOD_RESULTS = Lists.newArrayList(Items.PORKCHOP, Items.CHICKEN, Items.MUTTON, Items.BEEF, Items.RABBIT);

    public UncleSam(EntityType<? extends SCPEntity> type, World worldIn) {
        super(type, worldIn);
        setCanPickUpLoot(true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(5, new TemptGoal(this, 1, Ingredient.of(Items.ROTTEN_FLESH), false));
        goalSelector.addGoal(10, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(11, new LookAtGoal(this, LivingEntity.class, 16));
    }

    @Override
    public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == Items.ROTTEN_FLESH) {
            setItemInHand(Hand.OFF_HAND, stack.copy());
            if (level.isClientSide)
                player.sendMessage(new TranslationTextComponent("scp.job.will.be.done.in").append(" " + Functions.ticksToSeconds(stack.getCount() * 60)).append(new TranslationTextComponent(" scp.seconds")), getUUID());
            stack.shrink(stack.getCount());
            level.playSound(player, getX(), getY(), getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.NEUTRAL, 1, 0.6f);
            return ActionResultType.SUCCESS;
        }
        return super.interactAt(player, vec, hand);
    }

    @Override
    public void tick() {
        super.tick();
        if (getMainHandItem().getItem() instanceof SwordItem) {
            if (getOffhandItem().getItem() == Items.ROTTEN_FLESH) {

                if (transformationTime > 0) {
                    transformationTime--;
                    swing(Hand.MAIN_HAND, false);
                } else {
                    int modifier = level.random.nextInt(5) + 1;
                    Item randomFood = FOOD_RESULTS.get(random.nextInt(FOOD_RESULTS.size()));

                    ItemStack stack = new ItemStack(randomFood, modifier);
                    AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPosition()).inflate (3);
                    List<BlockPos> posSet = Functions.boundingBoxToPositions(axisAlignedBB);
                    boolean foundInventry = false;
                    for (BlockPos blockPos : posSet) {
                        TileEntity tileEntity = level.getBlockEntity(blockPos);
                        if (tileEntity != null && tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
                            IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(null);
                            if (Functions.canInsertItem(itemHandler, stack)) {
                                Functions.tryInsertItem(itemHandler, stack);
                                foundInventry = true;
                                break;
                            }
                        }
                    }
                    if (!foundInventry) {
                        spawnAtLocation(stack);
                    }
                    getMainHandItem().hurtAndBreak(stack.getCount(), this, uncleSam -> uncleSam.broadcastBreakEvent(Hand.MAIN_HAND));
                    getOffhandItem().shrink(1);

                    if (getOffhandItem().getCount() > 0)
                        transformationTime = 60;
                }

            }
        }
    }



    @Override
    public boolean canHoldItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Time", transformationTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        transformationTime = compound.getInt("Time");
    }
}
