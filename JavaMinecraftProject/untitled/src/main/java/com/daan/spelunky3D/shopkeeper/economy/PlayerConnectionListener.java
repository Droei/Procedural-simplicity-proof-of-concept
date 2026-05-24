package com.daan.spelunky3D.shopkeeper.economy;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final PointBarManager pointBarManager;

    public PlayerConnectionListener(PointBarManager pointBarManager) {
        this.pointBarManager = pointBarManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        pointBarManager.createBar(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        pointBarManager.removeBar(player);
    }
}