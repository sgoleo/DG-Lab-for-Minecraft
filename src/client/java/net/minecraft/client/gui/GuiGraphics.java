package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.gui.GuiRenderState;

public class GuiGraphics extends GuiGraphicsExtractor {
    public GuiGraphics(Minecraft minecraft, GuiRenderState guiRenderState, int mouseX, int mouseY) {
        super(minecraft, guiRenderState, mouseX, mouseY);
    }
}
