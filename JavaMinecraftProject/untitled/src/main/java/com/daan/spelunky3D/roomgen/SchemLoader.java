package com.daan.spelunky3D.roomgen;

import com.daan.spelunky3D.pathgen.utils.RandomGen;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemLoader {

    private final JavaPlugin plugin;
    private final Map<String, List<Clipboard>> schematics = new HashMap<>();
    public SchemLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadAll() {

        File dir = new File(plugin.getDataFolder(), "Chunk");
        File bigDir = new File(plugin.getDataFolder(), "ChunkParts");

        if (!dir.exists()) {
            dir.mkdirs();
            plugin.getLogger().info("Created schematics folder: Chunk/");
        }

        load("floor", new File(bigDir, "NormalFloor/normalFloor1.schem"));
        load("floor", new File(bigDir, "NormalFloor/normalFloor2.schem"));
        load("floor", new File(bigDir, "NormalFloor/normalFloor3.schem"));
        load("floor", new File(bigDir, "NormalFloor/normalFloor4.schem"));

        load("ceiling", new File(bigDir, "NormalCeiling/normalCeiling1.schem"));
        load("ceiling", new File(bigDir, "NormalCeiling/normalCeiling2.schem"));

        load("wallNorth", new File(bigDir,  "WallNorth/wallNorth1.schem"));
        load("wallNorth", new File(bigDir,  "WallNorth/wallNorth2.schem"));

        load("wallSouth", new File(bigDir,  "WallSouth/wallSouth1.schem"));
        load("wallSouth", new File(bigDir,  "WallSouth/wallSouth2.schem"));


        load("wallEast", new File(bigDir,   "WallEast/wallEast1.schem"));
        load("wallWest", new File(bigDir,   "WallWest/wallWest1.schem"));

        load("openingNorth", new File(bigDir, "OpeningNorth/openingNorth1.schem"));
        load("openingSouth", new File(bigDir, "OpeningSouth/openingSouth1.schem"));

        load("openingEast",  new File(bigDir, "OpeningEast/openingEast1.schem"));
        load("openingEast",  new File(bigDir, "OpeningEast/openingEast2.schem"));

        load("openingWest",  new File(bigDir, "OpeningWest/openingWest1.schem"));
        load("openingWest",  new File(bigDir, "OpeningWest/openingWest2.schem"));

        load("ceilingHole", new File(bigDir,  "CeilingHole/ceilingHole1.schem"));
        load("floorHole", new File(bigDir,  "FloorHole/floorHole1.schem"));
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

                schematics.computeIfAbsent(key, k -> new ArrayList<>()).add(clipboard);
            }

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load schematic: " + file.getName());
            e.printStackTrace();
        }
    }


    public Clipboard get(String key) {
        List<Clipboard> list = schematics.get(key);

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(RandomGen.range(0,list.size()));
    }}