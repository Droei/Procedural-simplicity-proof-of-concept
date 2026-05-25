package com.daan.spelunky3D.shopkeeper.shop;

import com.daan.spelunky3D.shopkeeper.economy.PointBarManager;
import com.daan.spelunky3D.shopkeeper.economy.PointManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShopManager {

    private final Map<Material, Integer> prices = new HashMap<>();

    private final PointManager pointManager;
    private final PointBarManager pointBarManager;

    public ShopManager(PointManager pointManager,
                       PointBarManager pointBarManager) {

        this.pointManager = pointManager;
        this.pointBarManager = pointBarManager;

        initializePrices();
    }

    public void spawnShopVillager(Player player) {

        Location location = player.getLocation();

        Villager villager = (Villager) player.getWorld().spawnEntity(
                location,
                EntityType.VILLAGER
        );

        villager.setAI(false);

        villager.setInvulnerable(true);

        villager.setSilent(true);

        villager.setCollidable(false);

        villager.setProfession(Villager.Profession.NONE);

        villager.setCustomName("§aShopkeeper");

        villager.setCustomNameVisible(true);

        villager.addScoreboardTag("shop_villager");
    }

    public void spawnShopVillager(Location location) {

        Villager villager = (Villager) location.getWorld()
                .spawnEntity(location, EntityType.VILLAGER);

        villager.setAI(false);

        villager.setInvulnerable(true);

        villager.setSilent(true);

        villager.setCollidable(false);

        villager.setProfession(Villager.Profession.NONE);

        villager.setCustomName("§aShopkeeper");

        villager.setCustomNameVisible(true);

        villager.addScoreboardTag("shop_villager");
    }


    private void initializePrices() {

        prices.put(Material.STONE, 5);

        prices.put(Material.PORKCHOP, 25);

        prices.put(Material.GOLDEN_APPLE, 100);

        prices.put(Material.BOW, 120);

        prices.put(Material.ARROW, 5);

        prices.put(Material.DIAMOND_SWORD, 250);

        prices.put(Material.DIAMOND_PICKAXE, 250);

        prices.put(Material.DIAMOND_HELMET, 200);

        prices.put(Material.DIAMOND_CHESTPLATE, 350);

        prices.put(Material.DIAMOND_LEGGINGS, 300);

        prices.put(Material.DIAMOND_BOOTS, 175);
    }
    public void removeAllShops(World world) {

        world.getEntities().stream()

                .filter(entity ->
                        entity instanceof Villager)

                .filter(entity ->
                        entity.getScoreboardTags()
                                .contains("shop_villager"))

                .forEach(entity ->
                        entity.remove());
    }
    public void openShop(Player player) {

        Inventory inventory = Bukkit.createInventory(
                null,
                9,
                "Villager Shop"
        );

        inventory.addItem(createShopItem(Material.STONE, 1));

        inventory.addItem(createShopItem(Material.PORKCHOP, 1));

        inventory.addItem(createShopItem(Material.GOLDEN_APPLE, 1));

        inventory.addItem(createShopItem(Material.BOW, 1));

        inventory.addItem(createShopItem(Material.ARROW, 16));

        inventory.addItem(createShopItem(Material.DIAMOND_SWORD, 1));

        inventory.addItem(createShopItem(Material.DIAMOND_PICKAXE, 1));

        inventory.addItem(createShopItem(Material.DIAMOND_CHESTPLATE, 1));

        player.openInventory(inventory);
    }

    private ItemStack createShopItem(Material material, int amount) {

        ItemStack item = new ItemStack(material, amount);

        int price = prices.getOrDefault(material, 0);

        var meta = item.getItemMeta();

        if (meta != null) {

            meta.setDisplayName(
                    "§f" + formatName(material.name()) +
                            " §7(" + price + " points)"
            );

            item.setItemMeta(meta);
        }

        return item;
    }

    private String formatName(String name) {

        String[] words = name.toLowerCase().split("_");

        StringBuilder builder = new StringBuilder();

        for (String word : words) {

            builder.append(
                    Character.toUpperCase(word.charAt(0))
            );

            builder.append(word.substring(1));

            builder.append(" ");
        }

        return builder.toString().trim();
    }

    public void purchaseItem(Player player, ItemStack item) {

        if (item == null) return;

        Material material = item.getType();

        if (!prices.containsKey(material)) return;

        int price = prices.get(material);

        boolean success = pointManager.payPoints(player, price);

        if (!success) {

            player.sendMessage("§cNot enough points!");

            return;
        }

        player.getInventory().addItem(item.clone());

        pointBarManager.updateBar(player);

        player.sendMessage("§aPurchased " + material.name());
    }
}