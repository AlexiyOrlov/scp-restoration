package dev.buildtool.scp.scps.clockworks;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.satako.Functions;
import dev.buildtool.scp.events.ModEvents;
import dev.buildtool.scp.registration.SCPBlocks;
import dev.buildtool.scp.registration.SCPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Clockworks transforms various items. It can be set to automatically ingest input items.
 * TODO make it a multiblock
 */
public class ClockworksEntity extends BlockEntity2 implements ITickableTileEntity {
    public ClockworksRecipe.Mode mode= ClockworksRecipe.Mode.ONE_ONE;
    public boolean working;
    public int workTime;
    public ItemStack input=ItemStack.EMPTY;
    ItemStack output=ItemStack.EMPTY;
    public boolean autoInput=false;

    public ClockworksEntity() {
        super(SCPTiles.clockworksEntity);
    }

    @Override
    public void tick() {
        if(!level.isClientSide)
        {
            if(working || autoInput) {
                BlockState panel = level.getBlockState(getBlockPos());
                Direction direction = panel.getValue(Panel.FACING);
                Direction left = direction.getCounterClockWise();
                Direction right = direction.getClockWise();
                BlockPos inputPoint = getBlockPos().relative(right, 5);
                BlockPos outputPoint = getBlockPos().relative(left, 5);
                BlockState rightBottomBlock = level.getBlockState(inputPoint.below());
                BlockState leftBottomBlock = level.getBlockState(outputPoint.below());
                BlockState rightBackBlock = level.getBlockState(inputPoint.relative(direction.getOpposite()));
                BlockState leftBackBlock = level.getBlockState(outputPoint.relative(direction.getOpposite()));
                BlockState below = level.getBlockState(getBlockPos().below());
                BlockState behind = level.getBlockState(getBlockPos().relative(direction.getOpposite()));
                if (rightBottomBlock == SCPBlocks.clockworksChamber.defaultBlockState() && leftBottomBlock == SCPBlocks.clockworksChamber.defaultBlockState()
                        && rightBackBlock == SCPBlocks.clockworksChamber.defaultBlockState() && leftBackBlock == SCPBlocks.clockworksChamber.defaultBlockState() &&
                        below == SCPBlocks.clockworksBase.defaultBlockState() && behind == SCPBlocks.clockworksBase.defaultBlockState()) {
                    if (input.isEmpty()) {
                        List<ClockworksRecipe> clockworksRecipes = level.getRecipeManager().getRecipesFor(ModEvents.clockworksRecipeType, null, level);
                        List<ItemEntity> itemEntities = level.getEntities(EntityType.ITEM, new AxisAlignedBB(inputPoint), itemEntity -> true);
                        label:
                        for (ItemEntity itemEntity : itemEntities) {
                            ItemStack item = itemEntity.getItem();
                            for (ClockworksRecipe clockworksRecipe : clockworksRecipes) {
                                ItemStack input = clockworksRecipe.getInput();
                                ClockworksRecipe.Mode mode = clockworksRecipe.getMode();
                                if (mode == this.mode && Functions.areItemTypesEqual(item, input)) {
                                    this.input = item;
                                    output = clockworksRecipe.getResultItem().copy();
                                    output.setCount(output.getCount() * item.getCount());
                                    workTime = clockworksRecipe.getSeconds() * item.getCount() * 20;
                                    level.playSound(null, inputPoint.getX(), inputPoint.getY(), inputPoint.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 1, 1);
                                    itemEntity.remove();
                                    break label;
                                }
                            }
                        }
                    }
                    if (workTime == 0) {
                        if (!output.isEmpty()) {
                            ItemEntity drop = new ItemEntity(level, outputPoint.getX() + 0.5, outputPoint.getY() + 0.5, outputPoint.getZ() + 0.5, output);
                            level.addFreshEntity(drop);
                            level.playSound(null, outputPoint.getX(), outputPoint.getY(), outputPoint.getZ(), SoundEvents.PISTON_EXTEND, SoundCategory.BLOCKS, 1, 0.8f);
                            drop.setDeltaMovement(direction.getStepX() / 2d, 0, direction.getStepZ() / 2d);
                            input = ItemStack.EMPTY;
                            output = ItemStack.EMPTY;
                        }
                        working = false;
                        level.blockEvent(getBlockPos(), getBlockState().getBlock(), 0, 0);
                    } else {
                        workTime--;
                    }
                }
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.putInt("Progress",workTime);
        tag.putByte("Mode", (byte) mode.ordinal());
        tag.put("Input",input.serializeNBT());
        tag.put("Output",output.serializeNBT());
        tag.putBoolean("On",working);
        tag.putBoolean("Auto-input",autoInput);
        return super.save(tag);
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        workTime=tag.getInt("Progress");
        mode= ClockworksRecipe.Mode.values()[tag.getByte("Mode")];
        input=ItemStack.of(tag.getCompound("Input"));
        output=ItemStack.of(tag.getCompound("Output"));
        working=tag.getBoolean("On");
        autoInput=tag.getBoolean("Auto-input");
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if(id==0)
        {
            working=type==1;
            return true;
        }
        return super.triggerEvent(id, type);
    }
}
