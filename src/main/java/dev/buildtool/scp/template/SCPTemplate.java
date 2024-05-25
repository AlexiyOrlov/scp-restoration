package dev.buildtool.scp.template;

import dev.buildtool.satako.Functions;
import dev.buildtool.scp.SCPObject;
import dev.buildtool.scp.Utils;
import dev.buildtool.scp.scps.infiniteikea.GeneratableTemplate;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
@Deprecated
public class SCPTemplate extends Item {

    public SCPTemplate(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    @Override
    public ActionResultType useOn(ItemUseContext p_195939_1_) {
        BlockPos blockPos = p_195939_1_.getClickedPos();
        Direction face = p_195939_1_.getClickedFace();
        ItemStack itemStack = p_195939_1_.getItemInHand();
        CompoundNBT compoundNBT = itemStack.getTag();
        String scp = compoundNBT.getString("Number");
        World world = p_195939_1_.getLevel();
        if (world instanceof IServerWorld) {
            TemplateManager templateManager = world.getServer().getStructureManager();
            Template template = templateManager.get(new ResourceLocation(dev.buildtool.scp.SCP.ID, "containers/" + scp));
            if (template != null) {
                new GeneratableTemplate(template, Collections.emptyList(), ((IServerWorld) world).getLevel()).placeInWorld((IServerWorld) world, blockPos.relative(face), new PlacementSettings().setIgnoreEntities(false).setFinalizeEntities(true).setRotation(Functions.directionToRotation(p_195939_1_.getHorizontalDirection())), random);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void fillItemCategory(ItemGroup p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        if (allowdedIn(p_150895_1_)) {
            Utils.getAllSCPs().forEach(stringObjectMap -> {
                String number = (String) stringObjectMap.get("number");
                String name = (String) stringObjectMap.get("name");
                ModAnnotation.EnumHolder classification = (ModAnnotation.EnumHolder) stringObjectMap.get("classification");
                CompoundNBT compoundNBT = new CompoundNBT();
                compoundNBT.putString("Number", number);
                compoundNBT.putString("Name", name);
                compoundNBT.putString("Class", classification.getValue());
                ItemStack itemStack = new ItemStack(this);
                itemStack.setTag(compoundNBT);
                p_150895_2_.add(itemStack);
            });
        }
        p_150895_2_.sort((o1, o2) -> {
            if (o1.hasTag() && o2.hasTag())
                return o1.getTag().getString("Number").compareTo(o2.getTag().getString("Number"));
            return 0;
        });
    }

    @Override
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> textComponents, ITooltipFlag p_77624_4_) {
        super.appendHoverText(p_77624_1_, p_77624_2_, textComponents, p_77624_4_);
        if (p_77624_1_.hasTag()) {
            CompoundNBT compoundNBT = p_77624_1_.getTag();
            textComponents.add(new StringTextComponent("SCP-" + compoundNBT.getString("Number")));
            textComponents.add(new TranslationTextComponent("scp.name").append(": " + compoundNBT.getString("Name")));
            textComponents.add(new StringTextComponent(SCPObject.Classification.valueOf(compoundNBT.getString("Class")).toString()));
        }
    }
}
