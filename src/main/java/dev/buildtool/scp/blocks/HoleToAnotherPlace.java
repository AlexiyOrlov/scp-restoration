package dev.buildtool.scp.blocks;

import dev.buildtool.satako.Functions;
import dev.buildtool.satako.blocks.Block2;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.registration.Sounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SCPObject(number = "1437", classification = SCPObject.Classification.SAFE, name = "A Hole to Another Place")
public class HoleToAnotherPlace extends Block2 {
    final static int timeRange = 25;

    public HoleToAnotherPlace(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
        worldIn.getBlockTicks().scheduleTick(pos, this, Functions.minutesToTicks(worldIn.random.nextInt(timeRange)));
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        List<Item> randomMundaneItems = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item.getRarity(ItemStack.EMPTY) == Rarity.COMMON).collect(Collectors.toList());
        Item itemIn = randomMundaneItems.get(rand.nextInt(randomMundaneItems.size()));
        ItemStack itemStack = new ItemStack(itemIn, rand.nextInt(itemIn.getItemStackLimit(ItemStack.EMPTY)));
        worldIn.addFreshEntity(new ItemEntity(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0., itemStack));
        worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), Sounds.scp1437, SoundCategory.BLOCKS, 1, 1, false);
        worldIn.getBlockTicks().scheduleTick(pos, this, Functions.minutesToTicks(worldIn.random.nextInt(timeRange)));
    }

}
