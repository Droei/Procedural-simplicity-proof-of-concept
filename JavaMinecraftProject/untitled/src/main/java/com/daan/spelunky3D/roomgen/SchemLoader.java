package com.daan.spelunky3D.roomgen;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class SchemLoader {

    private final JavaPlugin plugin;
    private final Map<String, Clipboard> schematics = new HashMap<>();

    public SchemLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadAll() {

        File dir = new File(plugin.getDataFolder(), "Chunk");

        if (!dir.exists()) {
            dir.mkdirs();
            plugin.getLogger().info("Created schematics folder: Chunk/");
        }

        load("floor", new File(dir, "floor.schem"));
        load("ceiling", new File(dir, "ceiling.schem"));
        load("wallNorth", new File(dir, "wallNorth.schem"));
        load("wallSouth", new File(dir, "wallSouth.schem"));
        load("wallEast", new File(dir, "wallEast.schem"));
        load("wallWest", new File(dir, "wallWest.schem"));
        load("openingNorth", new File(dir, "openingNorth.schem"));
        load("openingSouth", new File(dir, "openingSouth.schem"));
        load("openingEast", new File(dir,  "openingEast.schem"));
        load("openingWest", new File(dir,  "openingWest.schem"));
        load("ceilingHole", new File(dir,  "ceilingHole.schem"));
        load("floorHole", new File(dir,  "floorHole.schem"));
        load("floorStart", new File(dir,  "floorStart.schem"));
        load("floorEnd", new File(dir,  "floorEnd.schem"));
        plugin.getLogger().info("Loaded schematics: " + schematics.keySet());
    }

    private void load(String key, File file) {

        try {
            if (!file.exists()) {
                plugin.getLogger().warning("Missing schematic: " + file.getName());
                return;
            }

            ClipboardFormat format = ClipboardFormats.findByFile(file);
            if (format == null) {
                plugin.getLogger().warning("Invalid schematic format: " + file.getName());
                return;
            }

            try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                Clipboard clipboard = reader.read();
                schematics.put(key, clipboard);
            }

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load schematic: " + file.getName());
            e.printStackTrace();
        }
    }

    public Clipboard get(String key) {
        return schematics.get(key);
    }
}