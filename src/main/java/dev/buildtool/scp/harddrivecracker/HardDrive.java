package dev.buildtool.scp.harddrivecracker;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.Utils;
import dev.buildtool.scp.infiniteikea.GeneratableTemplate;
import dev.buildtool.scp.registration.SCPBlocks;
import dev.buildtool.scp.registration.SCPItems;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HardDrive extends Item {
    public static final String NUMBER = "Number";
    static String CRACKED = "Is cracked";

    public HardDrive(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    public ActionResultType useOn(ItemUseContext itemUseContext) {
        ItemStack drive = itemUseContext.getItemInHand();
        if (drive.hasTag()) {
            World world = itemUseContext.getLevel();
            PlayerEntity player = itemUseContext.getPlayer();
            CompoundNBT compoundNBT = drive.getTag();
            boolean cracked = compoundNBT.getBoolean(CRACKED);
            if (cracked) {
                if (player != null) {
                    if (world instanceof ServerWorld) {
                        ServerWorld serverWorld = (ServerWorld) world;
                        TemplateManager templateManager = serverWorld.getStructureManager();
                        BlockPos startPosition = itemUseContext.getClickedPos().relative(itemUseContext.getClickedFace());
                        Template template = templateManager.get(new ResourceLocation(SCP.ID, "cell/" + compoundNBT.getString(NUMBER)));
                        PlacementSettings placementSettings = new PlacementSettings().setIgnoreEntities(false).setFinalizeEntities(true).setRotation(Functions.directionToRotation(itemUseContext.getHorizontalDirection()));
                        if (player.isCrouching()) {
                            if (template != null) {
                                //check for obstacles
                                for (Template.Palette palette : template.palettes) {
                                    List<Template.BlockInfo> blockInfoList = palette.blocks();
                                    for (Template.BlockInfo blockInfo : blockInfoList) {
                                        BlockPos blockPos = blockInfo.pos;
                                        BlockPos positionToPlaceAt = Template.calculateRelativePosition(placementSettings, blockPos).offset(startPosition);
                                        BlockState blockState1 = serverWorld.getBlockState(positionToPlaceAt);
                                        if (!blockState1.isAir(serverWorld, positionToPlaceAt) && !blockState1.is(SCPBlocks.previewBlock)) {
                                            player.sendMessage(new TranslationTextComponent("scp.blocks.are.present", blockState1.getBlock().getName().getString()), Util.NIL_UUID);
                                            return ActionResultType.FAIL;
                                        }
                                    }
                                }
                                new GeneratableTemplate(template, Collections.emptyList(), serverWorld).placeInWorld(serverWorld, startPosition, placementSettings, random);
                                drive.shrink(1);
                            }
                        } else {
                            //place preview
                            for (Template.Palette palette : template.palettes) {
                                List<Template.BlockInfo> blockInfoList = palette.blocks();
                                for (Template.BlockInfo blockInfo : blockInfoList) {
                                    BlockPos blockPos = blockInfo.pos;
                                    BlockPos positionToPlaceAt = Template.calculateRelativePosition(placementSettings, blockPos).offset(startPosition);
                                    if (serverWorld.getBlockState(positionToPlaceAt).isAir()) {
                                        serverWorld.setBlock(positionToPlaceAt, SCPBlocks.previewBlock.defaultBlockState(), 2);
                                        serverWorld.getBlockTicks().scheduleTick(positionToPlaceAt, SCPBlocks.previewBlock, 20 * 5);
                                    }
                                }
                            }
                            player.sendMessage(new TranslationTextComponent("scp.placed.preview"), Util.NIL_UUID);
                        }
                    }
                    return ActionResultType.SUCCESS;
                }
            } else {
                player.sendMessage(new TranslationTextComponent("scp.need.to.crack.drive"), Util.NIL_UUID);
                return ActionResultType.FAIL;
            }
        }
        return super.useOn(itemUseContext);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> textComponents, ITooltipFlag p_77624_4_) {
        super.appendHoverText(stack, p_77624_2_, textComponents, p_77624_4_);
        if (stack.hasTag()) {
            CompoundNBT compoundNBT = stack.getTag();
            if (compoundNBT.contains(NUMBER)) {
                String number = compoundNBT.getString(NUMBER);
                textComponents.add(new StringTextComponent("SCP-" + number));
            }
            boolean cracked = compoundNBT.getBoolean(CRACKED);
            if (cracked) {
                textComponents.add(new TranslationTextComponent("scp.cracked"));
            } else
                textComponents.add(new TranslationTextComponent("scp.encrypted"));
        }
    }

    @Override
    public void fillItemCategory(ItemGroup itemGroup, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(itemGroup)) {
            Utils.getAllSCPs().forEach(stringObjectMap -> {
                ItemStack drive = new ItemStack(SCPItems.scpHardDrive);
                drive.getOrCreateTag().putString(HardDrive.NUMBER, (String) stringObjectMap.get("number"));
                drive.getTag().putBoolean(HardDrive.CRACKED, true);
                stacks.add(drive);
            });
            stacks.sort(Comparator.comparing(o -> {
                if (o.hasTag() && o.getTag().contains(NUMBER))
                    return Integer.valueOf(o.getTag().getString(NUMBER));
                return 0;
            }));
        }
    }
}
