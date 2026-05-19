package online.kbpf.dg_lab.client.screen.WaveformScreen.Custom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public abstract class CustomSliderWidget extends AbstractSliderButton {

    private boolean sliderFocused;
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("widget/slider");
    private static final Identifier HIGHLIGHTED_TEXTURE = Identifier.withDefaultNamespace("widget/slider_highlighted");
    private static final Identifier HANDLE_TEXTURE = Identifier.withDefaultNamespace("widget/slider_handle");
    private static final Identifier HANDLE_HIGHLIGHTED_TEXTURE = Identifier.withDefaultNamespace("widget/slider_handle_highlighted");

    public CustomSliderWidget(int x, int y, int width, int height, Component text, double value) {
        super(x, y, width, height, text, value);
    }

    public void setValue(int value) {
        this.setMessage(Component.literal(String.valueOf(value)));
        this.value = (double) value / 100;
    }

    @Override
    public void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        Minecraft minecraftClient = Minecraft.getInstance();

        int alphaVal = (int)(this.alpha * 255.0F) & 0xFF;
        int whiteWithAlpha = 0x00FFFFFF | (alphaVal << 24);

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getTexture(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), whiteWithAlpha);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getHandleTexture(), this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 8, this.getHeight(), whiteWithAlpha);
        
        int baseColor = this.active ? 0xFFFFFFFF : 0xFF9E9E9E;
        int textColor = (baseColor & 0x00FFFFFF) | (alphaVal << 24);
        
        graphics.text(
                minecraftClient.font,
                this.getMessage(),
                this.getX() + this.getWidth(),
                this.getY(),
                textColor
        );
    }

    private Identifier getTexture() {
        return this.isFocused() && !this.sliderFocused ? HIGHLIGHTED_TEXTURE : TEXTURE;
    }

    private Identifier getHandleTexture() {
        return !this.isHovered() && !this.sliderFocused ? HANDLE_TEXTURE : HANDLE_HIGHLIGHTED_TEXTURE;
    }

}