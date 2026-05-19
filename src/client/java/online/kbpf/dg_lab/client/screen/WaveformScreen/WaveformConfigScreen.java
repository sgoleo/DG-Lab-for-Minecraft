package online.kbpf.dg_lab.client.screen.WaveformScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import online.kbpf.dg_lab.client.screen.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static online.kbpf.dg_lab.client.screen.ConfigScreen.*;

@Environment(EnvType.CLIENT)
public class WaveformConfigScreen extends Screen {

//波形配置界面
    private WaveformListWidget waveformListWidget;

    public WaveformConfigScreen() {

        super(Component.literal("波形配置界面"));
    }

    @Override
    public void onClose() {
        Screen configScreen = new ConfigScreen();
        if (this.minecraft != null) {
            this.minecraft.setScreen(configScreen);
        }
        //上一级界面
    }

    @Override
    protected void init() {
        //注册列表项目
        Minecraft client = Minecraft.getInstance();
        waveformListWidget = new WaveformListWidget(client, width, height - 40, 40, ButtonHeight + ButtonDistance);
        //用这个滚动列表注意左右边界 添加条目比较少的时候不显示左右边界 但是左右边界的地方无法交互
        WaveformListWidget.Entry a = new WaveformListWidget.Entry(waveformListWidget, client.font, Component.literal("A通道受伤波形"), "ADamage");
        WaveformListWidget.Entry b = new WaveformListWidget.Entry(waveformListWidget, client.font, Component.literal("A通道恢复波形"), "AHealing");
        WaveformListWidget.Entry c = new WaveformListWidget.Entry(waveformListWidget, client.font, Component.literal("B通道受伤波形"), "BDamage");
        WaveformListWidget.Entry d = new WaveformListWidget.Entry(waveformListWidget, client.font, Component.literal("B通道恢复波形"), "BHealing");


        //添加列表项目
        waveformListWidget.addWaveformEntry(a);
        waveformListWidget.addWaveformEntry(b);
        waveformListWidget.addWaveformEntry(c);
        waveformListWidget.addWaveformEntry(d);
        addRenderableWidget(waveformListWidget);
    }

}
