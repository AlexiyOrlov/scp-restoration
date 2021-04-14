package dev.buildtool.scp.oldai;

import dev.buildtool.satako.blocks.BlockHorizontal;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.events.SCPTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@SCPObject(name = "Old AI", number = "079", classification = SCPObject.Classification.EUCLID)
public class OldAIBlock extends BlockHorizontal {
    public static BooleanProperty active = BlockStateProperties.ENABLED;

    public OldAIBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(active, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(active);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.oldAIEntity.create();
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
//        state = state.cycle(active);
        if (worldIn.isClientSide) {
            openGui();
//            if (state.getValue(active)) {
//                player.sendMessage(new TranslationTextComponent("scp.active"), UUID.randomUUID());
//            } else {
//                player.sendMessage(new TranslationTextComponent("scp.inactive"), UUID.randomUUID());
//            }
        }
//        worldIn.setBlockAndUpdate(pos, state);
        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    private void openGui() {

    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos blockPos, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
        super.neighborChanged(state, world, blockPos, p_220069_4_, p_220069_5_, p_220069_6_);
        if (world.hasNeighborSignal(blockPos)) {
            state = state.cycle(active);
            world.setBlockAndUpdate(blockPos, state);
        }
    }
}
