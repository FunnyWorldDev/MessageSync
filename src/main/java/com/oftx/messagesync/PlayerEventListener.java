package com.oftx.messagesync;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerEventListener implements Listener {

    private final WsClient wsClient;
    private final Plugin plugin;

    public PlayerEventListener(WsClient wsClient, Plugin plugin) {
        this.wsClient = wsClient;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return; // 如果事件被取消，则直接返回，不发送消息
        }

        String message = event.getMessage();
        String playerName = event.getPlayer().getName();
        wsClient.send("<" + playerName + "> " + message);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final int onlinePlayerCount = Bukkit.getOnlinePlayers().size();
        final String playerName = event.getPlayer().getName();
        wsClient.send(playerName + " 加入了游戏\n当前在线 " + onlinePlayerCount + " 人");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final String playerName = event.getPlayer().getName();

        // 调度器推迟1秒（20个tick）运行任务
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            final int onlinePlayerCount = Bukkit.getOnlinePlayers().size();
            final String msg = onlinePlayerCount == 0 ? "无人在线" : ("当前在线 " + onlinePlayerCount + " 人");
            wsClient.send(playerName + " 离开了游戏\n" + msg);
        }, 20L); // 20 tick = 1 second
    }

}
