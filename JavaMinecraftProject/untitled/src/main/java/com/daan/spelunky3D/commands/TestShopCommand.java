package com.daan.spelunky3D.commands;

import com.daan.spelunky3D.shopkeeper.shop.ShopManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestShopCommand implements CommandExecutor {

    private final ShopManager shopManager;

    public TestShopCommand(ShopManager shopManager) {
        this.shopManager = shopManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        shopManager.spawnShopVillager(player);

        player.sendMessage("Test villager spawned.");

        return true;
    }
}