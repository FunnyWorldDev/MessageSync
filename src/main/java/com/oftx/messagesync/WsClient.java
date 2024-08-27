package com.oftx.messagesync;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WsClient extends WebSocketClient {



    public WsClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Bukkit.getLogger().info("Connected to WebSocket server");
    }

    @Override
    public void onMessage(String message) {
        Bukkit.getLogger().info("Received message: " + message);
        // 广播消息给所有在线玩家
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Bukkit.getLogger().warning("Disconnected from WebSocket server with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        Bukkit.getLogger().warning("An error occurred:" + ex.getMessage());
        ex.printStackTrace();
    }
}
