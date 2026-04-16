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

        boolean finalCycle = false;
        List<Chunk> current = new ArrayList<>();
        current.add(startChunk);

        for (int i = 0; i < cycles; i++) {

            finalCycle = (i == cycles - 1);
            current = createAttachedChunks(current, finalCycle);
        }

        Chunk chosen = current.get(RandomGen.range(0, current.size()));

        Chunk endChunk;

        if (chosen.location.z != 0)
        {
            endChunk = manager.setDownHole(chosen);
            setupOpenings(endChunk, 1, 3, finalCycle);

        }
        else
        {
            endChunk = manager.setEndingChunk(chosen);
        }


        plugin.getLogger().info(endChunk.determineChunkDesign().toString());

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

        if (chunk.getChunkType() == ChunkTypeEnum.END ||
                chunk.getChunkType() == ChunkTypeEnum.HOLE_DOWN) {

            chunk.setOpeningDirections(incoming);
            return;
        }

        List<DirectionEnum> finalDirections = new ArrayList<>(incoming);

        if (!isFinalCycle) {

            List<DirectionEnum> available =
                    new ArrayList<>(generator.findAvailableOpenings(chunk.location));

            available.removeIf(dir ->
                    blocked.contains(dir) || finalDirections.contains(dir)
            );

            int amount = RandomGen.range(min, Math.min(max, available.size()) + 1);

            for (int i = 0; i < amount && !available.isEmpty(); i++) {
                int index = RandomGen.range(0, available.size());
                DirectionEnum chosen = available.remove(index);

                finalDirections.add(chosen);
            }
        }

        chunk.setOpeningDirections(finalDirections);
    }
}