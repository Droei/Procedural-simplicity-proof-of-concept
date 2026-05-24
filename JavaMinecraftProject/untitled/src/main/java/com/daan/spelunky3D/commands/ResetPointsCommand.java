package com.daan.spelunky3D.commands;

import com.daan.spelunky3D.shopkeeper.economy.PointBarManager;
import com.daan.spelunky3D.shopkeeper.economy.PointManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetPointsCommand implements CommandExecutor {

    private final PointManager pointManager;
    private final PointBarManager pointBarManager;

    public ResetPointsCommand(PointManager pointManager,
                              PointBarManager pointBarManager) {

        this.pointManager = pointManager;
        this.pointBarManager = pointBarManager;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {

        if (!(sender instanceof Player player)) return true;

        pointManager.resetPoints(player);

        pointBarManager.updateBar(player);

        player.sendMessage("Points reset.");

        return true;
    }
}