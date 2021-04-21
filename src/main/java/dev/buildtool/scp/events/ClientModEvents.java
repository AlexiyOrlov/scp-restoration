package dev.buildtool.scp.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.buildtool.satako.gui.ContainerScreen2;
import dev.buildtool.scp.ArmoredRenderer;
import dev.buildtool.scp.BipedModel2;
import dev.buildtool.scp.EntityRenderer2;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.crate.CrateContainer;
import dev.buildtool.scp.human.FemaleRenderer;
import dev.buildtool.scp.human.InteractionContainer;
import dev.buildtool.scp.human.InteractionScreen;
import dev.buildtool.scp.human.MaleRenderer;
import dev.buildtool.scp.humansrefuted.HumanRefutedModel;
import dev.buildtool.scp.humansrefuted.SCP3199Egg;
import dev.buildtool.scp.infiniteikea.CivilianRenderer;
import dev.buildtool.scp.infiniteikea.IkeaMonsterModel;
import dev.buildtool.scp.items.FlakShard;
import dev.buildtool.scp.items.FlakShardModel;
import dev.buildtool.scp.monsterpot.PotMonsterModel1;
import dev.buildtool.scp.monsterpot.PotMonsterRenderer;
import dev.buildtool.scp.plaguedoctor.CorpseRenderer;
import dev.buildtool.scp.plaguedoctor.PlagueDoctor3;
import dev.buildtool.scp.sculpture.SculptureModel;
import dev.buildtool.scp.shelf.ShelfRenderer;
import dev.buildtool.scp.shelflife.ShelfContainer;
import dev.buildtool.scp.shyguy.ShyGuyModelActive;
import dev.buildtool.scp.shyguy.ShyGuyModelCrying;
import dev.buildtool.scp.shyguy.ShyGuyModelIdle;
import dev.buildtool.scp.shyguy.ShyguyEntity;
import dev.buildtool.scp.slidingdoor.SlidingDoorRenderer;
import dev.buildtool.scp.smallgirl.YoungGirlModel;
import dev.buildtool.scp.table.Table4Renderer;
import dev.buildtool.scp.table.TableRenderer;
import dev.buildtool.scp.tatteredfarmer.ScarecrowModel;
import dev.buildtool.scp.theteacher.TheTeacher2;
import dev.buildtool.scp.ticklemonster.TickleMonster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
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
        RenderingRegistry.registerEntityRenderingHandler(Entities.humanEntityType, manager -> new ArmoredRenderer<>(manager, new BipedModel2<>(), "human", 0.3f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.sculptureEntityType, manager -> new EntityRenderer2(manager, new SculptureModel(), "sculpture", false, 0.3f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.tickleMonster, manager -> new EntityRenderer2(manager, new TickleMonster(), "tickle_monster", false, 0.9f) {
            @Override
            protected RenderType getRenderType(LivingEntity p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
                return RenderType.entityTranslucent(getTextureLocation(p_230496_1_));
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(Entities.humanRefuted, manager -> new EntityRenderer2<>(manager, new HumanRefutedModel(1), "scp3199", false, 0.7f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.humanRefutedChild, manager -> new EntityRenderer2<>(manager, new HumanRefutedModel(0.5f), "scp3199", false, 0.3f));
        RenderingRegistry.registerEntityRenderingHandler(Entities.shyguyEntity, manager -> new EntityRenderer2(manager, new ShyGuyModelIdle(), "shyguy-idle", false, 0.2f) {
            final ShyGuyModelCrying crying = new ShyGuyModelCrying();
            final ShyGuyModelActive active = new ShyGuyModelActive();
            final ShyGuyModelIdle idle = new ShyGuyModelIdle();
            final ResourceLocation textureCrying = new ResourceLocation(SCP.ID, "textures/entity/shyguy2.png");
            final ResourceLocation textureActive = new ResourceLocation(SCP.ID, "textures/entity/shyguy-active.png");

            @Override
            public void render(LivingEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
                ShyguyEntity shyguyEntity = (ShyguyEntity) entityIn;
                byte state = shyguyEntity.getEntityData().get(ShyguyEntity.state);
                if (state == ShyguyEntity.State.ACTIVE.b)
                    model = active;
                else if (state == ShyguyEntity.State.CRYING.b)
                    model = crying;
                else
                    model = idle;
                super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            }

            @Override
            public ResourceLocation getTextureLocation(LivingEntity entity) {
                ShyguyEntity shyguyEntity = (ShyguyEntity) entity;
                byte state = shyguyEntity.getEntityData().get(ShyguyEntity.state);
                if (state == ShyguyEntity.State.ACTIVE.b)
                    return textureActive;
                else if (state == ShyguyEntity.State.CRYING.b)
                    return textureCrying;
                return super.getTextureLocation(entity);
            }
        });


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

        RenderingRegistry.registerEntityRenderingHandler(Entities.flakShard,manager -> new EntityRenderer2(manager,new FlakShardModel(),"flak_shard",false,0){
            @Override
            public boolean shouldRender(Entity p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
                return true;
            }
        });

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
