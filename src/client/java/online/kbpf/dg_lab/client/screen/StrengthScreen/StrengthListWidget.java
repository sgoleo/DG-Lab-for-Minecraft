//package online.kbpf.dg_lab.client.screen.StrengthScreen;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Font;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.gui.Element;
//import net.minecraft.client.gui.Selectable;
//import net.minecraft.client.gui.tooltip.Tooltip;
//import net.minecraft.client.gui.widget.ButtonWidget;
//import net.minecraft.client.gui.widget.ElementListWidget;
//import net.minecraft.client.gui.widget.SliderWidget;
//import net.minecraft.client.gui.widget.TextFieldWidget;
//import net.minecraft.network.chat.Component;
//
//
//
//import java.util.List;
//
//public class StrengthListWidget extends ElementListWidget<StrengthListWidget.Entry> {
//
//    public StrengthListWidget(Minecraft minecraftClient, int width, int height, int y, int itemHeight) {
//        super(minecraftClient, width, height, y, itemHeight);
//    }
//
//    public void addWaveformEntry(Entry entry) {
//        this.addEntry(entry);
//    }
//
//    public static class Entry extends ElementListWidget.Entry<Entry> {
//        private final TextFieldWidget waveformDataText;
//        private final ButtonWidget sendButton;
//        private final Font textRenderer;
//        private final SliderWidget value;
//        private final Text text;
//
//        public Entry(Font textRenderer, Text text, Runnable runnable) {
//            waveformDataText = new TextFieldWidget(textRenderer, 100, 15, Component.literal("输入波形代码"));
//            waveformDataText.setPlaceholder(Component.literal("输入波形代码").withColor(0xaaaaaa));
//            value = new SliderWidget() {
//                @Override
//                protected void updateMessage() {
//                }
//
//                @Override
//                protected void applyValue() {
//
//                }
//            };
//            sendButton = new ButtonWidget.Builder(Component.literal("❏"), button -> {
//                Minecraft.getInstance().keyboard.setClipboard(waveformDataText.getText());
//            }).tooltip(Tooltip.of(Component.literal("点击复制波形代码"))).build();
//            this.textRenderer = textRenderer;
//            this.text = text;
//
//        }
//
//        @Override
//        public List<? extends Selectable> selectableChildren() {
//            return List.of(waveformDataText, sendButton);
//        }
//
//        @Override
//        public List<? extends Element> children() {
//            return List.of(waveformDataText, sendButton);
//        }
//
//
//        @Override
//        public void render(GuiGraphics context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
//            waveformDataText.setX((int) (x + (entryWidth / 2.5)));
//            waveformDataText.setY(y);
//            waveformDataText.setWidth(entryWidth / 2);
//            waveformDataText.setHeight(15);
//            waveformDataText.render(context, mouseX, mouseY, tickDelta);
//
//            sendButton.setX((int) (x + (entryWidth / 2.5) + ((double) entryWidth * 0.51)));
//            sendButton.setY(y);
//            sendButton.setWidth(15);
//            sendButton.setHeight(15);
//            sendButton.render(context, mouseX, mouseY, tickDelta);
//
//
//            context.drawString( this.text, x, y + 5, 0xffffff);
//
////            System.out.println(x + " " + y + " " + entryWidth + " " + entryHeight + "a");
//        }
//
//
//    }
//
//}
