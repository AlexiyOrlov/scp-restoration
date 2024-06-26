package dev.buildtool.scp.registration;

import dev.buildtool.satako.gui.ContainerScreen2;
import dev.buildtool.scp.ArmoredRenderer;
import dev.buildtool.scp.BipedModel2;
import dev.buildtool.scp.EntityRenderer2;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.chairs.SittableRenderer;
import dev.buildtool.scp.crate.CrateContainer;
import dev.buildtool.scp.harddrivecracker.HardDriveCrackerScreen;
import dev.buildtool.scp.human.*;
import dev.buildtool.scp.scps.flaregun.FlareRenderer;
import dev.buildtool.scp.scps.humansrefuted.HumanRefutedModel;
import dev.buildtool.scp.scps.humansrefuted.SCP3199Egg;
import dev.buildtool.scp.scps.infiniteikea.CivilianRenderer;
import dev.buildtool.scp.scps.infiniteikea.IkeaMonsterModel;
import dev.buildtool.scp.scps.mailbox.MailboxScreen;
import dev.buildtool.scp.scps.monsterpot.PotMonsterModel1;
import dev.buildtool.scp.scps.monsterpot.PotMonsterRenderer;
import dev.buildtool.scp.scps.patchworkbear.SCP2295;
import dev.buildtool.scp.scps.plaguedoctor.CorpseRenderer;
import dev.buildtool.scp.scps.plaguedoctor.PlagueDoctor3;
import dev.buildtool.scp.scps.sculpture.SCP173Model;
import dev.buildtool.scp.scps.shelflife.ShelfContainer;
import dev.buildtool.scp.scps.shyguy.SCP096;
import dev.buildtool.scp.scps.smallgirl.YoungGirlModel;
import dev.buildtool.scp.scps.tatteredfarmer.ScarecrowModel;
import dev.buildtool.scp.scps.theteacher.TheTeacher2;
import dev.buildtool.scp.scps.ticklemonster.TickleMonster2;
import dev.buildtool.scp.scps.wallofflesh.SCP2059;
import dev.buildtool.scp.shelf.ShelfRenderer;
import dev.buildtool.scp.slidingdoor.SlidingDoorRenderer;
import dev.buildtool.scp.table.Table4Renderer;
import dev.buildtool.scp.table.TableRenderer;
import dev.buildtool.scp.weapons.FlakShardRenderer;
import dev.buildtool.scp.weapons.FlameRenderer;
import dev.buildtool.scp.weapons.RocketRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    public static KeyBinding openSCPInfo;

    @SubscribeEvent
    @SuppressWarnings({"unused", "RedundantCast"})
    public static void setupClient(FMLClientSetupEvent clientSetupEvent) {
        ClientRegistry.bindTileEntityRenderer(SCPTiles.slidingDoorEntity, SlidingDoorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(SCPTiles.shelfEntity, ShelfRenderer::new);
        ClientRegistry.bindTileEntityRenderer(SCPTiles.tableEntity, TableRenderer::new);
        ClientRegistry.bindTileEntityRenderer(SCPTiles.tableEntity4, Table4Renderer::new);

        RenderingRegistry.registerEntityRenderingHandler(Entities.uncleSam, manager -> new ArmoredRenderer<>(manager, new BipedModel2<>(), "uncle_sam", 0.4f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.plagueDoctorEntityType, manager -> new ArmoredRenderer<>(manager, new PlagueDoctor3(), "plague_doctor", 0.3f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.corpseEntityType, CorpseRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Entities.sculptureEntityType, manager -> new EntityRenderer2<>(manager, new SCP173Model(), "scp173", false, 0.3f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.tickleMonster, manager -> new EntityRenderer2(manager, new TickleMonster2(), "tickle_monster2", false, 0.9f) {
            @Override
            protected RenderType getRenderType(LivingEntity p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
                return RenderType.entityTranslucent(getTextureLocation(p_230496_1_));
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(Entities.humanRefuted, manager -> new EntityRenderer2<>(manager, new HumanRefutedModel(1), "scp3199", false, 0.7f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.humanRefutedChild, manager -> new EntityRenderer2<>(manager, new HumanRefutedModel(0.5f), "scp3199", false, 0.3f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.shyguyEntity, manager -> new EntityRenderer2<>(manager, new SCP096(), "scp096", false, 0.2f));

        RenderingRegistry.registerEntityRenderingHandler(Entities.theTeacher, manager -> new EntityRenderer2<>(manager, new TheTeacher2(), "the_teacher", false, 0));
        RenderingRegistry.registerEntityRenderingHandler(Entities.potMonster, manager -> new PotMonsterRenderer(manager, new PotMonsterModel1(), 0.1f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.humanRefutedEgg, manager -> new EntityRenderer2<>(manager, new SCP3199Egg(), "scp3199egg", false, 0.1f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.swatArmorEntity, manager -> new ArmoredRenderer<>(manager, new BipedModel2<>(), "swat_scp", 0.3f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.maleCommoner, manager -> new MaleRenderer(manager, new BipedModel2<>(), 0.4f, "guy1", "guy2", "guy3", "human1", "human2", "human3"));
        RenderingRegistry.registerEntityRenderingHandler(Entities.femaleCommoner, manager -> new FemaleRenderer(manager, new BipedModel2<>(), 0.4f, "girl1", "girl2", "girl3", "girl4", "fem-human1", "fem-human2", "fem-human3"));
        RenderingRegistry.registerEntityRenderingHandler(Entities.tatteredFarmer, manager -> new EntityRenderer2<>(manager, new ScarecrowModel(), "farmer", false, 0.3f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.employeeMonster, manager -> new EntityRenderer2<>(manager, new IkeaMonsterModel(), "ikea_monster", false, 0.4f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.maleCivilian, manager -> new CivilianRenderer(manager, new BipedModel2<>(), 0.4f, "civilian1", "civilian2", "civilian3"));
        RenderingRegistry.registerEntityRenderingHandler(Entities.femaleCivilian, manager -> new CivilianRenderer(manager, new BipedModel2<>(), 0.4f, "fem-civilian1", "fem-civilian2", "fem-civilian3"));
        RenderingRegistry.registerEntityRenderingHandler(Entities.youngGirl, manager -> new EntityRenderer2<>(manager, new YoungGirlModel(), "young-girl", false, 0.1f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.chaosInsurgencySoldier, manager -> new ChaosISoldierRenderer(manager, new BipedModel2<>(), 0.4f, "chaosi-soldier1", "chaosi-soldier2", "chaosi-soldier3"));

        RenderTypeLookup.setRenderLayer(SCPBlocks.redIce, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(SCPBlocks.slidingDoorBlock, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(SCPBlocks.copperTube, RenderType.cutoutMipped());
        SCPBlocks.coloredPipes.forEach(rotatedPillarBlock -> RenderTypeLookup.setRenderLayer(rotatedPillarBlock, RenderType.cutoutMipped()));
        SCPBlocks.resistantGlass.forEach(block -> RenderTypeLookup.setRenderLayer(block, RenderType.translucent()));
        openSCPInfo = new KeyBinding("Open SCP info", GLFW.GLFW_KEY_H, "SCP Restoration");
        ClientRegistry.registerKeyBinding(openSCPInfo);

        ScreenManager.register(SCPContainers.humanInterContainer, (ScreenManager.IScreenFactory<InteractionContainer, InteractionScreen>) (ScreenManager.IScreenFactory<InteractionContainer, InteractionScreen>) InteractionScreen::new);
        ScreenManager.register(SCPContainers.crateContainer, (ScreenManager.IScreenFactory<CrateContainer, ContainerScreen2<CrateContainer>>) (t, f, c) -> new ContainerScreen2<>(t, f, c, true));
        ScreenManager.register(SCPContainers.shelfContainer, (ScreenManager.IScreenFactory<ShelfContainer, ContainerScreen2<ShelfContainer>>) (t, f, c) -> new ContainerScreen2<>(t, f, c, true));
        ScreenManager.register(SCPContainers.mailboxContainer, MailboxScreen::new);
        ScreenManager.register(SCPContainers.hardDriveCrackerContainer, HardDriveCrackerScreen::new);
        RenderingRegistry.registerEntityRenderingHandler(Entities.flakShard, FlakShardRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Entities.rocket, RocketRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Entities.wallOfFlesh, manager -> new EntityRenderer2<>(manager, new SCP2059(), "scp2059", false, 1));
        RenderingRegistry.registerEntityRenderingHandler(Entities.patchworkBear, manager -> new EntityRenderer2<>(manager, new SCP2295(), "scp2295", false, 0.2f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.flare, FlareRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(Entities.flame, FlameRenderer::new);

        RenderTypeLookup.setRenderLayer(SCPBlocks.previewBlock, RenderType.translucent());

        RenderingRegistry.registerEntityRenderingHandler(Entities.sittableEntityType, SittableRenderer::new);
    }

    public static List<String> getResourceText(String resourcePath) {
        ArrayList<String> strings = new ArrayList<>();
        try {
            IResource resource = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(SCP.ID, resourcePath));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                strings.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

}
