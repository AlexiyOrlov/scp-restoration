package dev.buildtool.scp.lootblock;

import dev.buildtool.scp.registration.SCPTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class LootBlock extends Block {
    public LootBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.lootBlockEntity.create();
    }

    @Override
    public ActionResultType use(BlockState p_225533_1_, World world, BlockPos pos, PlayerEntity playerEntity, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        LootBlockEntity lootBlockEntity = (LootBlockEntity) world.getBlockEntity(pos);
        if (playerEntity.isCrouching()) {
            world.setBlockAndUpdate(pos, Block.stateById(lootBlockEntity.storedBlockstate));
            return ActionResultType.SUCCESS;
        }
        if (world.isClientSide)
            openScreen(lootBlockEntity);
        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen(LootBlockEntity lootBlockEntity) {
        Minecraft.getInstance().setScreen(new LootBlockScreen(new StringTextComponent(""), lootBlockEntity));
    }

}
