package com.daan.spelunky3D.shopkeeper.economy;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PointManager {

    private final HashMap<UUID, Integer> playerPoints = new HashMap<>();

    public int getPoints(Player player) {
        return playerPoints.getOrDefault(player.getUniqueId(), 0);
    }

    public void resetPoints(Player player) {
        playerPoints.put(player.getUniqueId(), 0);
    }

    public void addPoints(Player player, int amount) {

        int current = getPoints(player);

        playerPoints.put(player.getUniqueId(), current + amount);
    }

    public boolean payPoints(Player player, int amount) {

        int current = getPoints(player);

        if (current < amount) {
            return false;
        }

        playerPoints.put(player.getUniqueId(), current - amount);

        return true;
    }
}