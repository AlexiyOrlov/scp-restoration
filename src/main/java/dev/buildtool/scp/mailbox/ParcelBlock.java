package dev.buildtool.scp.mailbox;

import dev.buildtool.satako.BlockEntity2;
import dev.buildtool.scp.registration.SCPTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ParcelBlock extends Block {
    public ParcelBlock(Properties p_i48401_1_) {
        super(p_i48401_1_);
    }

    @Override
    public ActionResultType use(BlockState p_225533_1_, World world, BlockPos blockPos, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        ParcelEntity parcelEntity = (ParcelEntity) world.getBlockEntity(blockPos);
        world.addFreshEntity(new ItemEntity(world, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), parcelEntity.mail));
        world.removeBlock(blockPos, false);
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.parcelEntityTile.create();
    }

    public static class ParcelEntity extends BlockEntity2 {

        public ItemStack mail = ItemStack.EMPTY;

        public ParcelEntity(TileEntityType<?> tileEntityType) {
            super(tileEntityType);
        }

        @Override
        public CompoundNBT save(CompoundNBT p_189515_1_) {
            p_189515_1_.put("Item", mail.serializeNBT());
            return super.save(p_189515_1_);
        }

        @Override
        public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
            super.load(p_230337_1_, p_230337_2_);
            mail.deserializeNBT(p_230337_2_.getCompound("Item"));
        }
    }
}
