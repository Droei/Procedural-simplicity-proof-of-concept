package com.daan.spelunky3D.pathgen;

import com.daan.spelunky3D.pathgen.chunkcore.ChunkGameInitializer;
import com.daan.spelunky3D.pathgen.chunkcore.ChunkManager;
import com.daan.spelunky3D.pathgen.chunkcore.ChunkOpeningGenerator;
import com.daan.spelunky3D.pathgen.models.Chunk;
import com.daan.spelunky3D.pathgen.utils.ChunkHelperFunctions;
import com.daan.spelunky3D.pathgen.utils.RandomGen;
import org.bukkit.plugin.java.JavaPlugin;

public class DungeonGenerator {

    private final JavaPlugin plugin;

    private int gridSize;
    private boolean debugMode;
    private int seed;

    private ChunkManager chunkManager;

    public DungeonGenerator(JavaPlugin plugin, int gridSize, boolean debugMode, int seed) {
        this.plugin = plugin;
        this.gridSize = gridSize;
        this.debugMode = debugMode;
        this.seed = seed;
    }

    public void generateDungeon() {

        plugin.getLogger().info("Starting dungeon generation...");

        if (debugMode) {
            RandomGen.setSeed(seed);
            plugin.getLogger().info("Using debug seed: " + seed);
        } else {
            int randomSeed = RandomGen.range(0, 9999999);
            RandomGen.setSeed(randomSeed);
            plugin.getLogger().info("Using random seed: " + randomSeed);
        }

        ChunkHelperFunctions.setGridSize(gridSize);

        chunkManager = new ChunkManager(gridSize);
        ChunkOpeningGenerator pathGenerator = new ChunkOpeningGenerator(chunkManager);
        ChunkGameInitializer initializer =
                new ChunkGameInitializer(chunkManager, pathGenerator, gridSize, plugin);

        chunkManager.setUpEmptyChunks();

        Chunk start = initializer.setRandomStart();

        Chunk floorChunk = start;

        for (int i = 0; i < gridSize; i++) {

            Chunk upChunk = initializer.generateFloor(floorChunk, gridSize - 1);

            floorChunk = upChunk;

            initializer.setupStartChunk(floorChunk, 1, 3);
        }

        chunkManager.visualizeChunks(plugin);

        plugin.getLogger().info("Dungeon generation complete!");
    }

    public ChunkManager getChunkManager() {
        return chunkManager;
    }
}