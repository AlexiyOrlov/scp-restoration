package dev.buildtool.scp.chairs;

import dev.buildtool.satako.blocks.BlockHorizontal;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class Chair extends BlockHorizontal {
    public Chair(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
//        if(worldIn.isRemote)
        {
//            Sittable sittable=new Sittable(worldIn);
//            sittable.setNoGravity(true);
//            sittable.setPosition(pos.getX()+0.5,pos.getY()+1,pos.getZ()+0.5);
//            worldIn.addEntity(sittable);
//            player.startRiding(sittable,true);
        }
        return ActionResultType.SUCCESS;
    }
}
