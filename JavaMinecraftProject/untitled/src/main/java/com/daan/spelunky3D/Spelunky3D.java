package com.daan.spelunky3D;

import com.daan.spelunky3D.pathgen.chunkcore.ChunkGameInitializer;
import com.daan.spelunky3D.pathgen.chunkcore.ChunkManager;
import com.daan.spelunky3D.pathgen.chunkcore.ChunkOpeningGenerator;
import com.daan.spelunky3D.pathgen.models.Chunk;
import com.daan.spelunky3D.pathgen.utils.ChunkHelperFunctions;
import com.daan.spelunky3D.pathgen.utils.RandomGen;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spelunky3D extends JavaPlugin {

    private int gridSize = 5;
    private boolean debugMode = false;
    private int seed = 0;

    private ChunkManager chunkManager;

    @Override
    public void onEnable() {

        getLogger().warning("Spelunky3D started!");

        if (debugMode) {
            RandomGen.setSeed(seed);
            getLogger().info("Using debug seed: " + seed);
        } else {
            int randomSeed = RandomGen.range(0, 9999999);
            RandomGen.setSeed(randomSeed);
            getLogger().info("Using random seed: " + randomSeed);
        }

        ChunkHelperFunctions.setGridSize(gridSize);

        chunkManager = new ChunkManager(gridSize);
        ChunkOpeningGenerator pathGenerator = new ChunkOpeningGenerator(chunkManager);
        ChunkGameInitializer initializer =
                new ChunkGameInitializer(chunkManager, pathGenerator, gridSize, this);

        chunkManager.setUpEmptyChunks();

        Chunk start = initializer.setRandomStart();

        Chunk floorChunk = start;

        for (int i = 0; i < gridSize; i++) {

            Chunk upChunk = initializer.generateFloor(floorChunk, gridSize - 1);

            floorChunk = upChunk;

            initializer.setupStartChunk(floorChunk, 1, 3);
        }

        chunkManager.visualizeChunks(this);
    }

    @Override
    public void onDisable() {
        getLogger().warning("Spelunky3D stopped!");
    }
}