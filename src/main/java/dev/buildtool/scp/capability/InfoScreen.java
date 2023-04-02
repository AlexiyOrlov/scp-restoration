package dev.buildtool.scp.capability;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.buildtool.satako.gui.BetterButton;
import dev.buildtool.satako.gui.Label;
import dev.buildtool.satako.gui.Screen2;
import dev.buildtool.scp.SCP;
import dev.buildtool.scp.SCPObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class InfoScreen  extends Screen2 {
    LinkedHashMap<String, List<String>> stringListHashMap=new LinkedHashMap<>();
    LinkedHashMap<String, SCPKnowledge.Data> scpData=new LinkedHashMap<>();
    public InfoScreen(ClientPlayerEntity player, ITextComponent title) {
        super(title);

        Optional<ModFileScanData> optional = ModList.get().getAllScanData().stream().filter(modFileScanData -> modFileScanData.getIModInfoData().get(0).getMods().get(0).getModId().equals(SCP.ID)).findFirst();
        optional.ifPresent(modFileScanData -> {
            List<ModFileScanData.AnnotationData> optionalAnnotationData = modFileScanData.getAnnotations().stream().filter(annotationData -> annotationData.getAnnotationType().getClassName().equals(SCPObject.class.getName())).collect(Collectors.toList());
            optionalAnnotationData.forEach(annotationData -> {

                Map<String, Object> data = annotationData.getAnnotationData();
                String number = (String) data.get("number");
                try {
                    IResource resource = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(SCP.ID, "info/" + number + ".txt"));
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                    ArrayList<String> strings = new ArrayList<>();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        strings.add(line);
                    }
                    bufferedReader.close();
                    stringListHashMap.put(number, strings);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });


        SCPKnowledge.Knowledge knowledge = CapabilityController.getKnowledge(player);
        scpData.putAll(knowledge.knownSCPData());

    }

    @Override
    public void init() {
        super.init();
        Set<String> keySet = scpData.keySet();
        IFormattableTextComponent textComponent = new TranslationTextComponent("scp.discovered.scps").append(" (" + keySet.size() + "/" + stringListHashMap.size() + ")");
        addButton(new Label(centerX - font.width(textComponent) / 2, 3, textComponent));
        int spaceY = 20;
        int spaceX = 10;
        List<String> sortedKeys = new ArrayList<>(keySet);
        sortedKeys.sort(Comparator.comparing(Integer::valueOf));
        for (String s : sortedKeys) {
            addButton(new BetterButton(spaceX, spaceY, new StringTextComponent(s), p_onPress_1_ -> {
                minecraft.setScreen(new SCPEntry(new StringTextComponent(""), this, s));
            }));
            spaceY += 20;
            if (spaceY > this.height - 20) {
                spaceX += font.width("______");
                spaceY = 20;
            }
        }
    }

    class SCPEntry extends Screen2 {
        Screen prev;
        String scp;
        int textVerticalPosition=0;
        public SCPEntry(ITextComponent title, Screen previous, String s) {
            super(title);
            prev=previous;
            scp=s;
        }

        @Override
        public void init() {
            super.init();
        }

        @Override
        public void renderBackground(MatrixStack matrixStack) {
            super.renderBackground(matrixStack);
            matrixStack.pushPose();
            //wrong parameter names
            drawCenteredString(matrixStack,font,"SCP-"+scp,centerX,10+textVerticalPosition,0xffffffff);
            int offsetY=4;
            if(scpData.isEmpty()) {
                for (String s : stringListHashMap.get(scp)) {
                    drawString(matrixStack, font, s, 10, 15 * offsetY, 0xffffffff);
                    offsetY++;
                }
            }
            else {
                SCPKnowledge.Data data = scpData.get(scp);
                drawCenteredString(matrixStack, font, "Class: " + data.classification, centerX, 25 + textVerticalPosition, 0xffffffff);
                drawCenteredString(matrixStack, font, "Name: " + data.officialName, centerX, 40 + textVerticalPosition, 0xffffffff);
                List<String> stringList = stringListHashMap.get(scp);
                if (stringList != null) {
                    for (String information : stringList) {
                        if (font.width(information + "   ") >= width) {
                            int i = information.length() / 2;
                            int f = information.indexOf(' ', i);
                            String part1 = information.substring(0, f);
                            String part2 = information.substring(f);
                            drawString(matrixStack, font, part1, 10, 15 * offsetY + textVerticalPosition, 0xffffffff);
                            offsetY++;
                            drawString(matrixStack, font, part2, 10, 15 * offsetY + textVerticalPosition, 0xffffffff);
                        } else {
                            drawString(matrixStack, font, information, 10, 15 * offsetY + textVerticalPosition, 0xffffffff);
                        }
                        offsetY++;
                    }
                }

            }
            matrixStack.popPose();
        }

        @Override
        public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double amount) {
            textVerticalPosition+=amount*5;
            return super.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, amount);
        }

        @Override
        public void onClose() {
            minecraft.setScreen(prev);
        }
    }
}
