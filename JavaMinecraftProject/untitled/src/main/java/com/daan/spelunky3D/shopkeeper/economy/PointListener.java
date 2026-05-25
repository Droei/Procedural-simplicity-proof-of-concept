package com.daan.spelunky3D.shopkeeper.economy;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class PointListener implements Listener {

    private final PointManager pointManager;
    private final PointBarManager pointBarManager;

    public PointListener(PointManager pointManager,
                         PointBarManager pointBarManager) {

        this.pointManager = pointManager;
        this.pointBarManager = pointBarManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block block = event.getBlock();

        int points = getOreValue(block.getType());

        event.setDropItems(false);

        if (points <= 0) return;


        pointManager.addPoints(player, points);

        pointBarManager.updateBar(player);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        Player player = event.getEntity().getKiller();

        if (player == null) return;

        int points = getMonsterValue(event.getEntityType());

        if (points <= 0) return;

        event.getDrops().clear();
        event.setDroppedExp(0);

        pointManager.addPoints(player, points);

        pointBarManager.updateBar(player);
    }

    private int getOreValue(Material material) {

        return switch (material) {

            case IRON_ORE -> 10;
            case DEEPSLATE_IRON_ORE -> 15;

            case GOLD_ORE -> 20;
            case DEEPSLATE_GOLD_ORE -> 30;

            case DIAMOND_ORE -> 60;
            case DEEPSLATE_DIAMOND_ORE -> 75;

            case SEA_LANTERN -> 300;

            default -> 0;
        };
    }

    private int getMonsterValue(EntityType type) {

        return switch (type) {

            case ZOMBIE -> 10;

            case SKELETON -> 15;

            case SPIDER -> 20;

            case CREEPER -> 40;

            default -> 0;
        };
    }
}