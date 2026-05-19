package online.kbpf.dg_lab.client.screen.WaveformScreen.Custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import online.kbpf.dg_lab.client.entity.Waveform.ControlBar;
import online.kbpf.dg_lab.client.entity.Waveform.Waveform;
import online.kbpf.dg_lab.client.screen.WaveformScreen.WaveformConfigScreen;

import static online.kbpf.dg_lab.client.Dg_labClient.waveformMap;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class CustomScreen extends Screen {

    private Button add, delete;
    private CustomListWidget customListWidget;
    protected static List<ControlBar> list = new ArrayList<>();
    protected String waveformKey;

    public CustomScreen(String waveformKey) {

        super(Component.literal("自定义波形界面"));
        if(waveformMap.containsKey(waveformKey)) {
            list = waveformMap.get(waveformKey).getList();
        }
        this.waveformKey = waveformKey;
    }


    @Override
    public void onClose() {
        Screen backScreen = new WaveformConfigScreen();
        Waveform tmp = new Waveform();
        if(waveformMap.containsKey(this.waveformKey))
            waveformMap.get(this.waveformKey);


        tmp.setList(list);
        tmp.GraphToData();
        if (waveformMap.containsKey(this.waveformKey))
            waveformMap.put(this.waveformKey, tmp);
        else
            waveformMap.replace(this.waveformKey, tmp);

        this.minecraft.setScreen(backScreen);
    }

    @Override
    protected void init() {
        Minecraft client = Minecraft.getInstance();
        customListWidget = new CustomListWidget(client, width, height - 40, 20, 8);


        add = Button.builder(Component.literal((list.size() >= 348) ? "---MAX---" : "+"), button -> {


            if(list.size() < 348) {
                add.setMessage(Component.literal("+"));
                for (int i = 1; i <= 4; i++) {
                    list.add(new ControlBar());
                    customListWidget.addCustomEntry(new CustomListWidget.Entry(customListWidget, list.size() - 1));
                }
            }
            add.setMessage(Component.literal((list.size() >= 348) ? "---MAX---" : "+"));

        }).bounds((int) (width * 0.1), height - 17, (int) (width * 0.7), 15).build();

        delete = Button.builder(Component.literal("-"), button -> {

            if(list.size() > 7) {
                for (int i = 1; i <= 4; i++) {
                    customListWidget.removeLast();
                    list.removeLast();
                }
            }
            add.setMessage(Component.literal((list.size() >= 348) ? "---MAX---" : "+"));
        }).bounds((int) (width * 0.8), height - 17, (int) (width * 0.1), 15).build();

        for (int i = 0; i <list.size(); i++){
            customListWidget.addCustomEntry(new CustomListWidget.Entry(customListWidget, i));
        }




        addRenderableWidget(add);
        addRenderableWidget(delete);
        addRenderableWidget(customListWidget);

    }


}
