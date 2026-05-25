package com.daan.spelunky3D.pathgen.chunkcore;

import com.daan.spelunky3D.pathgen.debug.ChunkDebugManager;
import com.daan.spelunky3D.pathgen.enums.ChunkDesignEnum;
import com.daan.spelunky3D.pathgen.enums.ChunkTypeEnum;
import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.models.Chunk;
import com.daan.spelunky3D.pathgen.models.Vector3Int;
import com.daan.spelunky3D.pathgen.utils.ChunkHelperFunctions;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ChunkManager {

    private Chunk[] chunks;
    private int gridSize;

    public ChunkManager(int gridSize) {
        this.gridSize = gridSize;
        this.chunks = new Chunk[gridSize * gridSize * gridSize];
    }

    public Chunk getChunkByLocation(Vector3Int location) {
        return chunks[ChunkHelperFunctions.parseLocationToId(location)];
    }

    public void setUpEmptyChunks() {
        int chunkCount = 0;

        for (int x = 0; x < gridSize; x++)
            for (int y = 0; y < gridSize; y++)
                for (int z = 0; z < gridSize; z++)
                    chunks[chunkCount++] = new Chunk(new Vector3Int(x, y, z));
    }

    public Chunk setChunkTypeByLocation(Vector3Int location, ChunkTypeEnum type) {
        return getChunkByLocation(location).setChunkType(type);
    }

    public Chunk setDownHole(Chunk chunk) {
        setChunkTypeByLocation(chunk.location, ChunkTypeEnum.HOLE_DOWN);

        Chunk upHole = setChunkTypeByLocation(
                new Vector3Int(chunk.location.x, chunk.location.y, chunk.location.z - 1),
                ChunkTypeEnum.HOLE_UP
        );

        return upHole;
    }

    public Chunk setEndingChunk(Chunk chunk) {
        return setChunkTypeByLocation(chunk.location, ChunkTypeEnum.END);
    }

    public void setChunkDirections(Chunk chunk, ChunkDesignEnum pathDirections) {
        chunk.setChunkDesign(pathDirections);
    }

    public void visualizeChunks(JavaPlugin plugin) {
        ChunkDebugManager.visualizeChunks(plugin, chunks, gridSize);
    }

    public Chunk[] getChunks() {
        return chunks;
    }

    public void evaluateNeighbors(Vector3Int location,
                                  List<DirectionEnum> incoming,
                                  List<DirectionEnum> blocked) {

        for (DirectionEnum direction : DirectionEnum.values()) {

            if (direction == DirectionEnum.NONE) continue;

            Vector3Int dirVector = ChunkHelperFunctions.directionToVector(direction);
            Vector3Int neighborLocation = location.add(dirVector.x, dirVector.y, dirVector.z);

            if (!ChunkHelperFunctions.isInsideGrid(neighborLocation)) {
                blocked.add(direction);
                continue;
            }

            Chunk neighbor = getChunkByLocation(neighborLocation);

            if (neighbor.getChunkType() == ChunkTypeEnum.NOTHING)
                continue;

            DirectionEnum opposite = ChunkHelperFunctions.getOpposite(direction);

            if (neighbor.getOpeningDirections().contains(opposite)) {
                incoming.add(direction);
            } else {
                blocked.add(direction);
            }
        }
    }
}