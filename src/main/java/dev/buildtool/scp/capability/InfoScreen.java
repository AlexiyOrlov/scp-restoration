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
    LinkedHashMap<String, List<String>> scpInfo = new LinkedHashMap<>();
    LinkedHashMap<String, SCPKnowledge.Data> scpData = new LinkedHashMap<>();
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
                    List<String> text = new ArrayList<>();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        text.add(line);
                        ;
                    }
                    bufferedReader.close();
                    scpInfo.put(number, text);
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
        IFormattableTextComponent textComponent = new TranslationTextComponent("scp.discovered.scps").append(" (" + keySet.size() + "/" + scpInfo.size() + ")");
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
            int offsetY = 4;
            List<String> text = scpInfo.get(scp);
            if(scpData.isEmpty()) {
                //
            }
            else {
                SCPKnowledge.Data data = scpData.get(scp);
                drawCenteredString(matrixStack, font, "Class: " + data.classification, centerX, 25 + textVerticalPosition, 0xffffffff);
                drawCenteredString(matrixStack, font, "Name: " + data.officialName, centerX, 40 + textVerticalPosition, 0xffffffff);

                List<String> stringList = splitString(text, new ArrayList<>(), 0);
                for (String information : stringList) {
                    drawString(matrixStack, font, information, 10, 15 * offsetY + textVerticalPosition, 0xffffffff);
                    offsetY++;
                }

            }
            matrixStack.popPose();
        }

        @Override
        public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double amount) {
            textVerticalPosition += amount * 5;
            return super.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, amount);
        }

        @Override
        public void onClose() {
            minecraft.setScreen(prev);
        }
    }

    private List<String> splitString(List<String> strings, List<String> returnList, int nextIndex) {
        String string = strings.get(nextIndex);
        if (font.width(string) > width - 30) {
            String part = string.substring(0, string.lastIndexOf(' '));
            while (font.width(part) > width - 30) {
                part = part.substring(0, part.length() - 2);
            }
            while (!part.endsWith(" ")) {
                part = part.substring(0, part.length() - 2);
            }
            String part2 = string.substring(part.length());
            returnList.add(part);
            returnList.add(part2);
            int indexOfNext = strings.indexOf(string) + 1;
            if (indexOfNext < strings.size()) {
                splitString(strings, returnList, indexOfNext);
            }
        } else {
            if (string.isEmpty()) {
                splitString(strings, returnList, nextIndex + 1);
            } else {
                returnList.add(string);
                int indexOfNext = strings.indexOf(string) + 1;
                if (indexOfNext < strings.size()) {
                    splitString(strings, returnList, indexOfNext);
                }
            }
        }

        return returnList;
    }
}
