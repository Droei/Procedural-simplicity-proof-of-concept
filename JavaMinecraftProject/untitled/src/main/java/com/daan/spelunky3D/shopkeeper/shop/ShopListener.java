package com.daan.spelunky3D.shopkeeper.shop;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ShopListener implements Listener {

    private final ShopManager shopManager;

    public ShopListener(ShopManager shopManager) {
        this.shopManager = shopManager;
    }

    @EventHandler
    public void onVillagerInteract(PlayerInteractEntityEvent event) {

        Entity entity = event.getRightClicked();

        if (!(entity instanceof Villager villager)) return;

        if (!villager.getScoreboardTags().contains("shop_villager")) return;

        event.setCancelled(true);

        shopManager.openShop(event.getPlayer());
    }

    @EventHandler
    public void onVillagerDamage(EntityDamageEvent event) {

        Entity entity = event.getEntity();

        if (!(entity instanceof Villager villager)) return;

        if (!villager.getScoreboardTags().contains("shop_villager")) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (!event.getView().getTitle().equals("Villager Shop")) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();

        if (clicked == null) return;

        shopManager.purchaseItem(player, clicked);
    }
}