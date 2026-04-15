package com.daan.spelunky3D.pathgen.chunkcore;

import com.daan.spelunky3D.pathgen.enums.ChunkTypeEnum;
import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.models.Chunk;
import com.daan.spelunky3D.pathgen.models.Vector3Int;
import com.daan.spelunky3D.pathgen.utils.ChunkHelperFunctions;
import com.daan.spelunky3D.pathgen.utils.RandomGen;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ChunkGameInitializer {

    private ChunkManager manager;
    private ChunkOpeningGenerator generator;
    private int gridSize;
    private JavaPlugin plugin;

    public ChunkGameInitializer(ChunkManager manager,
                                ChunkOpeningGenerator generator,
                                int gridSize,
                                JavaPlugin plugin) {

        this.manager = manager;
        this.generator = generator;
        this.gridSize = gridSize;
        this.plugin = plugin;
    }

    public Chunk setRandomStart() {

        Vector3Int startLocation = new Vector3Int(
                RandomGen.range(0, gridSize),
                RandomGen.range(0, gridSize),
                gridSize - 1
        );

        Chunk startChunk = manager.setChunkTypeByLocation(startLocation, ChunkTypeEnum.START);

        setupStartChunk(startChunk, 2, 2);

        return startChunk;
    }

    public Chunk setupStartChunk(Chunk chunk, int min, int max) {

        setupOpenings(chunk, min, max, false);

        plugin.getLogger().info(chunk.determineChunkDesign().toString());

        return chunk;
    }

    public Chunk generateFloor(Chunk startChunk, int cycles) {

        List<Chunk> current = new ArrayList<>();
        current.add(startChunk);

        for (int i = 0; i < cycles; i++) {

            boolean isLastCycle = (i == cycles - 1);
            current = createAttachedChunks(current, isLastCycle);
        }

        Chunk chosen = current.get(RandomGen.range(0, current.size()));

        Chunk endChunk;

        if (chosen.location.z != 0)
            endChunk = manager.setDownHole(chosen);
        else
            endChunk = manager.setEndingChunk(chosen);

        setupOpenings(endChunk, 1, 3, false);

        plugin.getLogger().info(endChunk.determineChunkDesign().toString());

        finalizeAllChunks();

        return endChunk;
    }

    private List<Chunk> createAttachedChunks(List<Chunk> chunks, boolean isFinalCycle) {

        List<Chunk> newChunks = new ArrayList<>();

        for (Chunk chunk : chunks) {

            for (DirectionEnum direction : chunk.getOpeningDirections()) {

                Vector3Int dir = ChunkHelperFunctions.directionToVector(direction);
                Vector3Int target = chunk.location.add(dir.x, dir.y, dir.z);

                Chunk newChunk = manager.getChunkByLocation(target);

                if (newChunk.getChunkType() != ChunkTypeEnum.NOTHING)
                    continue;

                newChunk.setChunkType(ChunkTypeEnum.NORMAL);

                setupOpenings(newChunk, 1, 2, isFinalCycle);

                plugin.getLogger().info(newChunk.determineChunkDesign().toString());

                newChunks.add(newChunk);
            }
        }

        return newChunks;
    }

    private void setupOpenings(Chunk chunk, int min, int max, boolean isFinalCycle) {

        List<DirectionEnum> incoming = new ArrayList<>();
        List<DirectionEnum> blocked = new ArrayList<>();

        manager.evaluateNeighbors(chunk.location, incoming, blocked);

        if (isFinalCycle) {
            chunk.setOpeningDirections(incoming);
            return;
        }

        List<DirectionEnum> directions = generator
                .pickRandomDirections(generator.findAvailableOpenings(chunk.location), min, max);

        for (DirectionEnum dir : incoming) {
            if (!directions.contains(dir)) {
                directions.add(dir);
            }
        }
        directions.removeIf(blocked::contains);

        chunk.setOpeningDirections(directions);
    }

    private void finalizeAllChunks() {

        for (Chunk chunk : manager.getChunks()) {

            if (chunk.getChunkType() == ChunkTypeEnum.NOTHING)
                continue;

            List<DirectionEnum> incoming = new ArrayList<>();
            List<DirectionEnum> blocked = new ArrayList<>();

            manager.evaluateNeighbors(chunk.location, incoming, blocked);

            chunk.setOpeningDirections(incoming);
        }
    }
}