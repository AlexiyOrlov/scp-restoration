package dev.buildtool.scp.clockworks;

import dev.buildtool.satako.blocks.BlockHorizontal;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.events.SCPTiles;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.UUID;

@SCPObject(number = "914",classification = SCPObject.Classification.SAFE,name = "The Clockworks")
public class Panel extends BlockHorizontal {
    public Panel(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return SCPTiles.clockworksEntity.create();
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if(tileEntity instanceof ClockworksEntity)
            {
                if(((ClockworksEntity) tileEntity).working)
                    player.sendMessage(new TranslationTextComponent("scp_restoration.cls.busy"), UUID.randomUUID());
                else
                {
                    if(worldIn.isClientSide)
                        openScreen((ClockworksEntity) tileEntity);
                }
            }

        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen(ClockworksEntity clockworksEntity)
    {
        Minecraft.getInstance().setScreen(new ClockworksScreen(clockworksEntity,new StringTextComponent("Clockworks")));
    }
}
