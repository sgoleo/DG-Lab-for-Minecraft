package online.kbpf.dg_lab.client.screen;


import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.network.chat.TextColor;
import online.kbpf.dg_lab.client.Dg_labClient;
import online.kbpf.dg_lab.client.createQR.ToolQR;
import online.kbpf.dg_lab.client.Config.WaveformConfig;
import online.kbpf.dg_lab.client.screen.StrengthScreen.StrengthConfigScreen;
import online.kbpf.dg_lab.client.screen.WaveformScreen.WaveformConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.network.chat.Component;


import static online.kbpf.dg_lab.client.Dg_labClient.*;


@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {

    public static final int ButtonHeight = 20, ButtonDistance = 5;

    public ButtonWidget saveFile;
    public ButtonWidget webSocketConfig;
    public ButtonWidget createQR;
    public ButtonWidget StrengthConfig;
    public ButtonWidget WaveFormConfig;
    public ButtonWidget CustomConfig;
    public ButtonWidget MaxStrength;
    public ButtonWidget TwoPlayerMode;


    public SliderWidget RenderingPositionX;
    public SliderWidget RenderingPositionY;
    public net.minecraft.client.gui.widget.SliderWidget SPQS;//第二个玩家退出强度 Second Player Quit Strength

    public TextFieldWidget secondPlayerName;






//    Screen customScreen = new CustomScreen();

    public ConfigScreen() {
        // 此参数为屏幕的标题，进入屏幕中，复述功能会复述。
        super(Component.literal("配置界面"));
    }





    @Override
    protected void init() {



        Minecraft client = Minecraft.getInstance();


        int width1 = client.getWindow().getScaledWidth(), height1 = client.getWindow().getScaledHeight();

        CustomConfig = ButtonWidget.builder(Component.literal("test"), button -> {
//            client.setScreen(customScreen);
        }).dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 140, (int) (width * 0.4), ButtonHeight).build();





        SPQS = new net.minecraft.client.gui.widget.SliderWidget((int) ((double) width / 2 + 5), 140 - (2 * (ButtonDistance + ButtonHeight)), (int) (width * 0.2), ButtonHeight,Component.literal("2P退出强度：" + secondPlayerQuitStrength), secondPlayerQuitStrength * 0.005) {
            @Override
            protected void updateMessage() {

            }

            @Override
            protected void applyValue() {
                int tmp = (int) (this.value * 200);
                secondPlayerQuitStrength = tmp;
                SPQS.setMessage(Component.literal("2P退出强度：" + ((tmp == 0)? "已關閉" : tmp)));
            }
        };

        RenderingPositionX = new SliderWidget(width / 2 + 5, 140 - ButtonDistance - ButtonHeight, (int) (width * 0.2) - 6, ButtonHeight, Component.literal((modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) ? "已关闭强度显示" : ("显示位置X:" + modConfig.getRenderingPositionX())), (double) modConfig.getRenderingPositionX() / width1) {
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

        RenderingPositionY = new SliderWidget(RenderingPositionX.getX() + RenderingPositionX.getWidth(), 140 - ButtonDistance - ButtonHeight, (int) (width * 0.2) - 6, ButtonHeight, Component.literal((modConfig.getRenderingPositionX() >= width1 || modConfig.getRenderingPositionY() >= height1) ? "已关闭强度显示" : ("显示位置Y:" + modConfig.getRenderingPositionY())), (double) modConfig.getRenderingPositionY() / height1) {
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

        MaxStrength = ButtonWidget.builder(Component.literal((modConfig.isRenderingMax()) ? "開" : "關"), button -> {
            modConfig.setRenderingMax(!modConfig.isRenderingMax());
            MaxStrength.setMessage(Component.literal((modConfig.isRenderingMax()) ? "開" : "關"));
        }).dimensions(RenderingPositionY.getX() + RenderingPositionX.getWidth(), 140 - ButtonDistance - ButtonHeight, 12, ButtonHeight).tooltip(Tooltip.of(Component.literal("是否开启最大强度显示"))).build();

        saveFile = ButtonWidget.builder(Component.literal("保存設定到文件"), button -> {
                    strengthConfig.savaFile();
                    modConfig.savaFile();
                    WaveformConfig.saveWaveform(waveformMap);
                })
                .dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 20, (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.of(Component.literal("所有更改是临时更改\n点击此按钮保存到文件"))).build();



        webSocketConfig = ButtonWidget.builder(Component.literal("連接設定"), button -> {
                    Screen WebSocketConfigScreen = new WebSocketConfigScreen();
                    client.setScreen(WebSocketConfigScreen);
                })
                .dimensions(width / 2 + 5, 20, (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.of(Component.literal("点击修改连接设置\n非必要无需修改"))).build();

        StrengthConfig = ButtonWidget.builder(Component.literal("强度設定"), button -> {
            Screen strengthConfigScreen = new StrengthConfigScreen();
            client.setScreen(strengthConfigScreen);
        }).dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 20 + ButtonHeight + ButtonDistance, (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.of(Component.literal("点击修改强度设置"))).build();

        WaveFormConfig = ButtonWidget.builder(Component.literal("波形設定"), button -> {
            Screen waveformConfigScreen = new WaveformConfigScreen();
            client.setScreen(waveformConfigScreen);
        }).dimensions(width / 2 + 5, 20 + ButtonHeight + ButtonDistance, (int) (width * 0.4), ButtonHeight).tooltip((Tooltip.of(Component.literal(":P")))).build();

        createQR = ButtonWidget.builder(Component.literal("創建連接二維碼並打開"), button -> {
            ToolQR.CreateQR();
        }).dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 140 - ButtonDistance - ButtonHeight, (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.of(Component.literal("图片默认生成于此地址:\n" + System.getProperty("user.dir")))).build();


        TwoPlayerMode = ButtonWidget.builder(Component.literal((twoPlayerMode) ? "本地雙人模式：开" : "本地雙人模式：关"), button -> {
            if(!client.isIntegratedServerRunning()) return;
            IntegratedServer server = client.getServer();
            if(!(server != null && server.isRemote())) return;
            twoPlayerMode = !twoPlayerMode;
            TwoPlayerMode.setMessage(Component.literal((twoPlayerMode) ? "本地雙人模式：开" : "本地雙人模式：关"));
        }).dimensions((int) ((double) width / 2 - (width * 0.4) - 5), 140 - (2 * (ButtonDistance + ButtonHeight)), (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.of(Component.literal("只有在单人模式开启局域网联机\n并且2p设置有人且在线才可启用\n2p退出游戏自动关闭\n本地双人模式每次启动游戏需要重新设置"))).build();

        secondPlayerName = new TextFieldWidget(this.textRenderer, (int) ((double) width / 2 + 6 + (int) (width * 0.2)), 140 - (2 * (ButtonDistance + ButtonHeight)), (int) (width * 0.2), ButtonHeight, Component.literal("输入玩家名字"));
        secondPlayerName.setMaxLength(16);
        secondPlayerName.setPlaceholder(Component.literal(secondPlayer).styled(style -> style.withColor(TextColor.fromRgb(0xaaaaaa))));
        secondPlayerName.setChangedListener(this::secondPlayerNameText);




        addDrawableChild(saveFile);
        addDrawableChild(webSocketConfig);
        addDrawableChild(StrengthConfig);
        addDrawableChild(WaveFormConfig);
        addDrawableChild(createQR);
        addDrawableChild(RenderingPositionX);
        addDrawableChild(RenderingPositionY);
        addDrawableChild(MaxStrength);
        addDrawableChild(TwoPlayerMode);
        addDrawableChild(SPQS);
        addDrawableChild(secondPlayerName);
//        addDrawableChild(CustomConfig);
    }

    private void secondPlayerNameText(String PlayerName){
        secondPlayer = PlayerName;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        TwoPlayerMode.setMessage(Component.literal((twoPlayerMode) ? "本地雙人模式：開" : "本地雙人模式：關"));
    }

}
