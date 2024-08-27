package com.oftx.messagesync;

import org.bukkit.plugin.java.JavaPlugin;
import java.net.URI;
import java.net.URISyntaxException;

public final class MessageSync extends JavaPlugin {

    private WsClient wsClient;

    @Override
    public void onEnable() {
        // 插件启动时的逻辑
        getLogger().info("MessageSync 插件已启动");

        // 初始化 WebSocket 客户端
        try {
            URI serverUri = new URI("ws://localhost:8888");
            wsClient = new WsClient(serverUri);
            wsClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // 注册玩家聊天事件监听器
        getServer().getPluginManager().registerEvents(new PlayerEventListener(wsClient, this), this);
    }

    @Override
    public void onDisable() {
        // 插件关闭时的逻辑
        getLogger().info("MessageSync 插件已关闭");
        if (wsClient != null) {
            wsClient.close();
        }
    }
}
