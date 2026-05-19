package online.kbpf.dg_lab.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.network.chat.Component;
import static online.kbpf.dg_lab.client.Dg_labClient.modConfig;
import static online.kbpf.dg_lab.client.Dg_labClient.webSocketServer;

public class hud implements HudElement {



    //屏幕強度顯示
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker tickDelta) {

        Minecraft client = Minecraft.getInstance();
        // 在onHudRender方法開頭添加測試渲染
//        drawContext.drawTextWithShadow(
//                client.textRenderer,
//                Component.literal("測試文本"),
//                10, 10,
//                0xFF00FF00 // 綠色
//        );
        if (client.player != null && client.level != null && (modConfig.getRenderingPositionX() < client.getWindow().getScaledWidth() || modConfig.getRenderingPositionY() < client.getWindow().getScaledHeight())) {

            // 假設強度數值是一個整數
            //            int strengthValue = getStrengthValue(client.player);

            // 計算圖標和文本的位置
            int x = modConfig.getRenderingPositionX();
            int y = modConfig.getRenderingPositionY();


            // 創建並渲染 OrderedText

            if(webSocketServer.getConnected()) {
                Component strengthText;
                Component strengthText1;
                if(modConfig.isRenderingMax()) {
                    strengthText = Component.literal("A:" + webSocketServer.getStrength().getAStrength() + ",Max:" + webSocketServer.getStrength().getAMaxStrength());

                    strengthText1 = Component.literal("B:" + webSocketServer.getStrength().getBStrength() + ",Max:" + webSocketServer.getStrength().getBMaxStrength());

                }
                else {
                    strengthText = Component.literal("A:" + webSocketServer.getStrength().getAStrength());

                    strengthText1 = Component.literal("B:" + webSocketServer.getStrength().getBStrength());
                }
                guiGraphics.text(client.font, strengthText, x, y, 0xFFFFFFFF);
                guiGraphics.text(client.font, strengthText1, x, y + 9, 0xFFFFFFFF);
            }
            else {
                Component strengthText = Component.literal("未連接");
                guiGraphics.text(client.font, strengthText, x, y, 0xFFFF0000);
            }
        }
    }
}
