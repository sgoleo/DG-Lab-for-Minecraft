package online.kbpf.dg_lab.client.screen;


import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Style;
import online.kbpf.dg_lab.client.Dg_labClient;
import online.kbpf.dg_lab.client.createQR.ToolQR;
import online.kbpf.dg_lab.client.Config.WaveformConfig;
import online.kbpf.dg_lab.client.screen.StrengthScreen.StrengthConfigScreen;
import online.kbpf.dg_lab.client.screen.WaveformScreen.WaveformConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;


import static online.kbpf.dg_lab.client.Dg_labClient.*;


@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {

    public static final int ButtonHeight = 20, ButtonDistance = 5;

    public Button saveFile;
    public Button webSocketConfig;
    public Button createQR;
    public Button StrengthConfig;
    public Button WaveFormConfig;
    public Button CustomConfig;
    public Button MaxStrength;
    public Button TwoPlayerMode;


    public AbstractSliderButton RenderingPositionX;
    public AbstractSliderButton RenderingPositionY;
    public AbstractSliderButton SPQS;//第二个玩家退出强度 Second Player Quit Strength

    public EditBox secondPlayerName;






//    Screen customScreen = new CustomScreen();

    public ConfigScreen() {
        // 此参数为屏幕的标题，进入屏幕中，复述功能会复述。
        super(Component.literal("配置界面"));
    }





    @Override
    protected void init() {



        Minecraft client = Minecraft.getInstance();


        int width1 = client.getWindow().getGuiScaledWidth(), height1 = client.getWindow().getGuiScaledHeight();

        CustomConfig = Button.builder(Component.literal("test"), button -> {
//            client.setScreen(customScreen);
        }).bounds((int) ((double) width / 2 - (width * 0.4) - 5), 140, (int) (width * 0.4), ButtonHeight).build();





        SPQS = new AbstractSliderButton((int) ((double) width / 2 + 5), 140 - (2 * (ButtonDistance + ButtonHeight)), (int) (width * 0.2), ButtonHeight,Component.literal("2P退出強度：" + secondPlayerQuitStrength), secondPlayerQuitStrength * 0.005) {
            @Override
            protected void updateMessage() {

            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 200);
                secondPlayerQuitStrength = tmp;
                SPQS.setMessage(Component.literal("2P退出強度：" + ((tmp == 0)? "已關閉" : tmp)));
            }
        };

        RenderingPositionX = new AbstractSliderButton(width / 2 + 5, 140 - ButtonDistance - ButtonHeight, (int) (width * 0.2) - 6, ButtonHeight, Component.literal((modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) ? "已关闭强度显示" : ("显示位置X:" + modConfig.getRenderingPositionX())), (double) modConfig.getRenderingPositionX() / width1) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * width1);
                modConfig.setRenderingPositionX(tmp);
                if (modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) {
                    this.setMessage(Component.literal("已關閉强度顯示"));
                    RenderingPositionY.setMessage(Component.literal("已關閉强度顯示"));
                } else {
                    this.setMessage(Component.literal("顯示位置X:" + tmp));
                    RenderingPositionY.setMessage(Component.literal("顯示位置Y:" + modConfig.getRenderingPositionY()));
                }
            }
        };

        RenderingPositionY = new AbstractSliderButton(RenderingPositionX.getX() + RenderingPositionX.getWidth(), 140 - ButtonDistance - ButtonHeight, (int) (width * 0.2) - 6, ButtonHeight, Component.literal((modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) ? "已关闭强度显示" : ("显示位置Y:" + modConfig.getRenderingPositionY())), (double) modConfig.getRenderingPositionY() / height1) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * height1);
                modConfig.setRenderingPositionY(tmp);
                if (modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) {
                    this.setMessage(Component.literal("已關閉强度顯示"));
                    RenderingPositionX.setMessage(Component.literal("已關閉强度顯示"));
                } else {
                    this.setMessage(Component.literal("顯示位置Y:" + tmp));
                    RenderingPositionX.setMessage(Component.literal("顯示位置X:" + modConfig.getRenderingPositionX()));
                }
            }
        };

        MaxStrength = Button.builder(Component.literal((modConfig.isRenderingMax()) ? "開" : "關"), button -> {
            modConfig.setRenderingMax(!modConfig.isRenderingMax());
            MaxStrength.setMessage(Component.literal((modConfig.isRenderingMax()) ? "開" : "關"));
        }).bounds(RenderingPositionY.getX() + RenderingPositionX.getWidth(), 140 - ButtonDistance - ButtonHeight, 12, ButtonHeight).tooltip(Tooltip.create(Component.literal("是否开启最大强度显示"))).build();

        saveFile = Button.builder(Component.literal("保存設定到文件"), button -> {
                    strengthConfig.savaFile();
                    modConfig.savaFile();
                    WaveformConfig.saveWaveform(waveformMap);
                })
                .bounds((int) ((double) width / 2 - (width * 0.4) - 5), 20, (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.create(Component.literal("所有更改是临时更改\n点击此按钮保存到文件"))).build();



        webSocketConfig = Button.builder(Component.literal("連接設定"), button -> {
                    Screen WebSocketConfigScreen = new WebSocketConfigScreen();
                    client.setScreen(WebSocketConfigScreen);
                })
                .bounds(width / 2 + 5, 20, (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.create(Component.literal("点击修改连接设置\n非必要无需修改"))).build();

        StrengthConfig = Button.builder(Component.literal("强度設定"), button -> {
            Screen strengthConfigScreen = new StrengthConfigScreen();
            client.setScreen(strengthConfigScreen);
        }).bounds((int) ((double) width / 2 - (width * 0.4) - 5), 20 + ButtonHeight + ButtonDistance, (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.create(Component.literal("点击修改强度设置"))).build();

        WaveFormConfig = Button.builder(Component.literal("波形設定"), button -> {
            Screen waveformConfigScreen = new WaveformConfigScreen();
            client.setScreen(waveformConfigScreen);
        }).bounds(width / 2 + 5, 20 + ButtonHeight + ButtonDistance, (int) (width * 0.4), ButtonHeight).tooltip((Tooltip.create(Component.literal(":P")))).build();

        createQR = Button.builder(Component.literal("創建連接二維碼並打開"), button -> {
            ToolQR.CreateQR();
        }).bounds((int) ((double) width / 2 - (width * 0.4) - 5), 140 - ButtonDistance - ButtonHeight, (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.create(Component.literal("图片默认生成于此地址:\n" + System.getProperty("user.dir")))).build();


        TwoPlayerMode = Button.builder(Component.literal((twoPlayerMode) ? "本地雙人模式：开" : "本地雙人模式：关"), button -> {
            if(!client.isSingleplayer()) return;
            IntegratedServer server = client.getSingleplayerServer();
            if(!(server != null && server.isPublished())) return;
            twoPlayerMode = !twoPlayerMode;
            TwoPlayerMode.setMessage(Component.literal((twoPlayerMode) ? "本地雙人模式：开" : "本地雙人模式：关"));
        }).bounds((int) ((double) width / 2 - (width * 0.4) - 5), 140 - (2 * (ButtonDistance + ButtonHeight)), (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.create(Component.literal("只有在单人模式开启局域网联机\n并且2p设置有人且在线才可启用\n2p退出游戏自动关闭\n本地双人模式每次启动游戏需要重新设置"))).build();

        secondPlayerName = new EditBox(this.font, (int) ((double) width / 2 + 6 + (int) (width * 0.2)), 140 - (2 * (ButtonDistance + ButtonHeight)), (int) (width * 0.2), ButtonHeight, Component.literal("输入玩家名字"));
        secondPlayerName.setMaxLength(16);
        secondPlayerName.setHint(Component.literal(secondPlayer).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xaaaaaa))));
        secondPlayerName.setResponder(this::secondPlayerNameText);




        addRenderableWidget(saveFile);
        addRenderableWidget(webSocketConfig);
        addRenderableWidget(StrengthConfig);
        addRenderableWidget(WaveFormConfig);
        addRenderableWidget(createQR);
        addRenderableWidget(RenderingPositionX);
        addRenderableWidget(RenderingPositionY);
        addRenderableWidget(MaxStrength);
        addRenderableWidget(TwoPlayerMode);
        addRenderableWidget(SPQS);
        addRenderableWidget(secondPlayerName);
//        addRenderableWidget(CustomConfig);
    }

    private void secondPlayerNameText(String PlayerName){
        secondPlayer = PlayerName;
    }

    @Override
    public void tick() {
        super.tick();
        TwoPlayerMode.setMessage(Component.literal((twoPlayerMode) ? "本地雙人模式：開" : "本地雙人模式：關"));
    }

}
