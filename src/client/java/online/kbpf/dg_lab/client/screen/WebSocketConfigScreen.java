package online.kbpf.dg_lab.client.screen;


import online.kbpf.dg_lab.client.Dg_labClient;
import online.kbpf.dg_lab.client.createQR.ToolQR;
import online.kbpf.dg_lab.client.Config.ModConfig;
import online.kbpf.dg_lab.client.entity.NetworkAdapter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static online.kbpf.dg_lab.client.screen.ConfigScreen.*;


@Environment(EnvType.CLIENT)
public class WebSocketConfigScreen extends Screen {

    protected WebSocketConfigScreen() {
        super(Component.literal("连接配置界面"));
    }

    public ModConfig modConfig = Dg_labClient.modConfig;
    public Button autoStartWebSocketServer;
    public Button createQR;
    public EditBox host;
    public EditBox port;
    public EditBox serverPort;
    public Button host1;
    public Button host2;
    public Button port1;
    public Button serverPort1;
    private NetworkAdapter network = new NetworkAdapter();
    private LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>(network.getNetworkMap());

    @Override
    public void close() {
        Screen configScreen = new ConfigScreen();
        client.setScreen(configScreen);

    }

    @Override
    protected void init() {
        modConfig = Dg_labClient.modConfig;
        autoStartWebSocketServer = Button.builder(Component.literal("自动启动连接服务器:已" + ((modConfig.getAutoStartWebSocketServer()) ? "开启" : "关闭")), button -> {
            if (modConfig.getAutoStartWebSocketServer()) {
                modConfig.setAutoStartWebSocketServer(false);
                autoStartWebSocketServer.setMessage(Component.literal("自动启动连接服务器:已关闭"));
            } else {
                modConfig.setAutoStartWebSocketServer(true);
                autoStartWebSocketServer.setMessage(Component.literal("自动启动连接服务器:已开启"));

            }

        }).bounds(width / 2 - (int) (width * 0.41), 20, (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.of(Component.literal("要在客户端启动时自动启动连接服务器\n如果关闭需要使用指令手动启动\n非必要无需关闭"))).build();

        createQR = Button.builder(Component.literal("创建连接二维码并打开"), button -> {
            ToolQR.CreateQR();
        }).bounds(width / 2 + 5, 20, (int) (width * 0.4), ButtonHeight).tooltip(Tooltip.of(Component.literal("图片默认生成于此地址:\n" + System.getProperty("user.dir")))).build();

        host = new EditBox(this.font, (int) (width * 0.66), 20 + ButtonHeight + ButtonDistance, (int) (width * 0.25), ButtonHeight, Component.literal("Enter address..."));
        host.setValue(modConfig.getAddress());
        host.setHint(Component.literal("this").styled(style -> style.withColor(0xffaaaaaa)));
        host.setResponder(this::hostText);
        host1 = Button.builder(Component.literal("?"), button -> {
        }).bounds((int) (width * 0.63), 20 + ButtonHeight + ButtonDistance, (int) (width * 0.03), ButtonHeight).tooltip(Tooltip.of(Component.literal("扫描二维码连接的地址\n非必要无需修改"))).build();
        host2 = Button.builder(Component.literal("<|>"), button -> {
            toggleNetworkAdapter();
        }).bounds((int) (width * 0.59), 20 + ButtonHeight + ButtonDistance, (int) (width * 0.04), ButtonHeight).tooltip(Tooltip.of(Component.literal("切换网卡"))).build();

        port = new EditBox(this.font, (int) (width * 0.66), 2 * (ButtonHeight + ButtonDistance) + 20, (int) (width * 0.25), ButtonHeight, Component.literal("Enter port..."));
        port.setValue(String.valueOf(modConfig.getPort()));
        port.setHint(Component.literal("9999").styled(style -> style.withColor(0xffaaaaaa)));
        port.setResponder(this::portText);
        port.setMaxLength(5);
        port1 = Button.builder(Component.literal("?"), button -> {
        }).bounds((int) (width * 0.63), 2 * (ButtonHeight + ButtonDistance) + 20, (int) (width * 0.03), ButtonHeight).tooltip(Tooltip.of(Component.literal("扫描二维码连接的端口,非服务器端口\n非必要无需修改"))).build();

        serverPort = new EditBox(this.font, (int) (width * 0.66), 3 * (ButtonHeight + ButtonDistance) + 20, (int) (width * 0.25), ButtonHeight, Component.literal("Enter port..."));
        serverPort.setValue(String.valueOf(modConfig.getPort()));
        serverPort.setHint(Component.literal("9999").styled(style -> style.withColor(0xffaaaaaa)));
        serverPort.setResponder(this::serverPortText);
        serverPort.setMaxLength(5);
        serverPort1 = Button.builder(Component.literal("?"), button -> {
        }).bounds((int) (width * 0.63), 3 * (ButtonHeight + ButtonDistance) + 20, (int) (width * 0.03), ButtonHeight).tooltip(Tooltip.of(Component.literal("服务器对外开放的端口\n非必要无需修改\n修改后请保存重启客户端生效"))).build();


        addRenderableWidget(createQR);
        addRenderableWidget(autoStartWebSocketServer);
        addRenderableWidget(host);
        addRenderableOnly(host1);
        addRenderableWidget(host2);
        addRenderableWidget(port);
        addRenderableOnly(port1);
        addRenderableWidget(serverPort);
        addRenderableOnly(serverPort1);
    }


    private Timer timer = new Timer(); // 定义一个计时器

    private void hostText(String Text) {

        // 重置计时器
        if (timer != null) {
            timer.cancel();
        }

        // 启动一个新的计时器，延迟更新
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 停止输入一段时间后的操作
                modConfig.setAddress(Text);
            }
        }, 1000); // 延迟时间为 1000ms

    }

    private void toggleNetworkAdapter(){
        boolean isKeyFound = false;
        for (Map.Entry<String, String> entry : linkedHashMap.entrySet()){
            if(entry.getKey().equals(modConfig.getNetwork())) isKeyFound = true;
            else if(isKeyFound){
                modConfig.setAddress(entry.getValue());
                modConfig.setNetwork(entry.getKey());
                host.setValue(entry.getValue());
                return;
            }
        }
        Map.Entry<String, String> firstEntry = linkedHashMap.entrySet().iterator().next();
        modConfig.setAddress(firstEntry.getValue());
        modConfig.setNetwork(firstEntry.getKey());
        host.setValue(firstEntry.getValue());
    }

    private void portText(String port) {
        int number;
        try {
            number = Integer.parseInt(port);
            number = (number > 65535 || number < 0) ? 9999 : number;

        } catch (NumberFormatException e) {
            number = 9999;
        }
        modConfig.setPort(number);


    }


    private void serverPortText(String serverPort) {
        int number;
        try {
            number = Integer.parseInt(serverPort);
            number = (number > 65535 || number < 0) ? 9999 : number;

        } catch (NumberFormatException e) {
            number = 9999;
        }
        modConfig.setServerPort(number);
    }

    @Override
    public void render(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);


        context.text(this.font, Component.literal("二维码连接的地址"), (int) (width * 0.1), 49, 0xffffffff);
        context.text(this.font, Component.literal(modConfig.getNetwork()), (int) (width * 0.1), 61, 0xffaaaaaa, false);
        context.text(this.font, Component.literal("二维码连接的端口"), (int) (width * 0.1), 74, 0xffffffff);
        context.text(this.font, Component.literal("服务器开放的端口"), (int) (width * 0.1), 99, 0xffffffff);


    }

}
