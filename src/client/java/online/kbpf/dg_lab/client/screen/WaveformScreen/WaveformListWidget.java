package online.kbpf.dg_lab.client.screen.WaveformScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import online.kbpf.dg_lab.client.Tool.DGWaveformTool;
import online.kbpf.dg_lab.client.entity.Waveform.Waveform;
import online.kbpf.dg_lab.client.screen.WaveformScreen.Custom.CustomScreen;

import java.util.List;
import org.jspecify.annotations.Nullable;

import static online.kbpf.dg_lab.client.screen.ConfigScreen.*;
import static online.kbpf.dg_lab.client.Dg_labClient.waveformMap;
import static online.kbpf.dg_lab.client.Dg_labClient.webSocketServer;

public class WaveformListWidget extends AbstractSelectionList<WaveformListWidget.Entry> {

    //列表项目内容
    private final int width;


    public WaveformListWidget(Minecraft minecraftClient, int width, int height, int y, int itemHeight) {
        super(minecraftClient, width, height, y, itemHeight);
        this.width = width;
    }

    @Override
    protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput output) {
    }

    //修改左右宽度
    @Override
    public int getRowLeft() {
        return this.getX(); // 从屏幕最左侧开始
    }
    @Override
    public int getRowWidth() {
        return this.width; // 宽度设置为屏幕宽度
    }
    @Override
    protected int scrollBarX() {
        return this.getRight() - 6; // 滚动条紧贴右侧
    }


    public void addWaveformEntry(Entry entry) {
        this.addEntry(entry);
    }

    public static class Entry extends AbstractSelectionList.Entry<Entry> implements ContainerEventHandler {
        Minecraft client = Minecraft.getInstance();
        private final EditBox waveformDataText; //文字输入框
        private final Button copyButton, pasteButton, testButton, customButton;          //按钮
        private final Font textRenderer;        //文本渲染参数
        private final Component text;                        //文本
        private final WaveformListWidget parent;        // 添加对父列表的引用
        private GuiEventListener focused;
        private boolean dragging;

        @Override
        public boolean isDragging() {
            return this.dragging;
        }

        @Override
        public void setDragging(boolean dragging) {
            this.dragging = dragging;
        }

        private Waveform waveform = new Waveform();

        public Entry(WaveformListWidget parent, Font textRenderer, Component text, String key) {
            //设置单个项目相关内容
            this.parent = parent;  // 保存父列表引用


            if(waveformMap.containsKey(key)) waveform = waveformMap.get(key);


            waveformDataText = new EditBox(textRenderer, 0, 0, 100, ButtonHeight, Component.literal(""));
            waveformDataText.setMaxLength(100000);

            waveformDataText.setValue(waveform.getWaveform());



            waveformDataText.setHint(Component.literal("输入波形代码").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xaaaaaa))));

            waveformDataText.setResponder(inputText -> {
                waveform.setWaveform(inputText);
            });

            customButton = Button.builder(Component.literal("✏"), button -> {
                Screen customScreen = new CustomScreen(key);
                client.setScreen(customScreen);
            }).bounds(0, 0, 15, ButtonHeight).tooltip(Tooltip.create(Component.literal("点击修改波形"))).build();

            copyButton = Button.builder(Component.literal("\uD83D\uDCC4"), button -> {
                Minecraft.getInstance().keyboardHandler.setClipboard(waveformDataText.getValue());
            }).bounds(0, 0, 15, ButtonHeight).tooltip(Tooltip.create(Component.literal("点击复制波形代码"))).build();

            pasteButton = Button.builder(Component.literal("\uD83D\uDCCB"), button -> {
                String clipboardText = Minecraft.getInstance().keyboardHandler.getClipboard();
                waveformDataText.setValue(clipboardText);
            }).bounds(0, 0, 15, ButtonHeight).tooltip(Tooltip.create(Component.literal("点击粘贴波形代码"))).build();

            testButton = Button.builder(Component.literal("\uD83D\uDCE8"), button -> {
                webSocketServer.sendDGWaveForm(waveformDataText.getValue(), 1);
            }).bounds(0, 0, 15, ButtonHeight).tooltip(Tooltip.create(Component.literal("发送到终端1通道"))).build();


            this.textRenderer = textRenderer;
            this.text = text;

        }

        @Override
        public void setFocused(@Nullable GuiEventListener focused) {
            this.focused = focused;
        }

        @Override
        @Nullable
        public GuiEventListener getFocused() {
            return this.focused;
        }

        //确保点击/交互被正确传递
        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(waveformDataText, testButton, customButton);
        }


        @Override
        public void extractContent(final GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            // 获取当前 Entry 的位置 and 尺寸信息
            int entryWidth = ((WaveformListWidget)this.parent).getRowWidth();
            int y = this.getY();
            int x = ((WaveformListWidget)this.parent).getRowLeft();

            //渲染相关
            //文本框位置宽高
            waveformDataText.setRectangle(x + (int) (entryWidth * 0.3), ButtonHeight, x + (int) (entryWidth * 0.4), y);
            waveformDataText.extractRenderState(context, mouseX, mouseY, deltaTicks);

            customButton.setRectangle(15, ButtonHeight, waveformDataText.getX() + waveformDataText.getWidth(), y);
            customButton.extractRenderState(context, mouseX, mouseY, deltaTicks);

//            copyButton.setRectangle(15, 20, customButton.getX() + 15, y);
//            copyButton.extractRenderState(context, mouseX, mouseY, deltaTicks);
//
//            pasteButton.setRectangle(15, 20, copyButton.getX() + 15, y);
//            pasteButton.extractRenderState(context, mouseX, mouseY, deltaTicks);

            testButton.setRectangle(15, ButtonHeight, customButton.getX() + 15, y);
            testButton.extractRenderState(context, mouseX, mouseY, deltaTicks);


            context.text(this.textRenderer, this.text, x + (int) (entryWidth * 0.15), y + 5, 0xffffffff);

            int duration = DGWaveformTool.checkAndCountValidSubstrings(waveformDataText.getValue());
            if(duration == 0)
                context.text(this.textRenderer, Component.literal("ERROR"), testButton.getX() + 20, y + 5, 0xffFF0000);
            else context.text(this.textRenderer, Component.literal((duration * 100) + "ms"), testButton.getX() + 15, y + 5, 0xffFFFFFF);
        }
    }
}
