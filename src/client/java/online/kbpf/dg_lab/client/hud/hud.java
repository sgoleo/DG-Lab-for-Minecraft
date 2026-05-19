package online.kbpf.dg_lab.client.hud;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.network.chat.Component;
import static online.kbpf.dg_lab.client.Dg_labClient.modConfig;
import static online.kbpf.dg_lab.client.Dg_labClient.webSocketServer;

public class hud implements HudElement {



    //屏幕强度显示
    @Override
    public void render(GuiGraphics guiGraphics, RenderTickCounter tickDelta) {

        Minecraft client = Minecraft.getInstance();
        // 在onHudRender方法开头添加测试渲染
//        drawContext.drawTextWithShadow(
//                client.textRenderer,
//                Component.literal("测试文本"),
//                10, 10,
//                0xFF00FF00 // 绿色
//        );
        if (client.player != null && client.level != null && (modConfig.getRenderingPositionX() < client.getWindow().getScaledWidth() || modConfig.getRenderingPositionY() < client.getWindow().getScaledHeight())) {

            // 假设强度数值是一个整数
            //            int strengthValue = getStrengthValue(client.player);

            // 计算图标和文本的位置
            int x = modConfig.getRenderingPositionX();
            int y = modConfig.getRenderingPositionY();


            // 创建并渲染 OrderedText

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
                guiGraphics.drawString(client.font, strengthText, x, y, 0xFFFFFFFF);
                guiGraphics.drawString(client.font, strengthText1, x, y + 9, 0xFFFFFFFF);
            }
            else {
                Component strengthText = Component.literal("未连接");
                guiGraphics.drawString(client.font, strengthText, x, y, 0xFFFF0000);
            }
        }
    }
}
