package com.daan.spelunky3D.shopkeeper.economy;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PointBarManager {

    private final Map<UUID, BossBar> bars = new HashMap<>();

    private final PointManager pointManager;

    public PointBarManager(PointManager pointManager) {
        this.pointManager = pointManager;
    }

    public void createBar(Player player) {

        removeBar(player);

        BossBar bossBar = Bukkit.createBossBar(
                "",
                BarColor.GREEN,
                BarStyle.SOLID
        );

        bossBar.addPlayer(player);
        bossBar.setVisible(true);

        bars.put(player.getUniqueId(), bossBar);

        updateBar(player);
    }

    public void updateBar(Player player) {

        BossBar bossBar = bars.get(player.getUniqueId());

        if (bossBar == null) {
            createBar(player);
            return;
        }

        int points = pointManager.getPoints(player);

        bossBar.setTitle("Points: " + points);

        double progress = Math.min(points / 1000.0, 1.0);

        bossBar.setProgress(progress);
    }

    public void removeBar(Player player) {

        BossBar bossBar = bars.remove(player.getUniqueId());

        if (bossBar == null) return;

        bossBar.removeAll();

        bossBar.setVisible(false);
    }

    public void removeAllBars() {

        for (BossBar bossBar : bars.values()) {

            bossBar.removeAll();

            bossBar.setVisible(false);
        }

        bars.clear();
    }
}