using System.Collections.Generic;
using UnityEngine;

public class ChunkManager
{
    private Chunk[] chunks;

    int gridSize;

    public ChunkManager(int gridSize)
    {
        this.gridSize = gridSize;
        chunks = new Chunk[gridSize * gridSize * gridSize];
    }

    public Chunk GetChunkByLocation(Vector3Int location)
        => chunks[ChunkHelperFunctions.ParseLocationToId(location)];

    public void SetUpEmptyChunks()
    {
        int chunkCount = 0;
        for (int x = 0; x < gridSize; x++)
            for (int y = 0; y < gridSize; y++)
                for (int z = 0; z < gridSize; z++)
                    chunks[chunkCount++] = new(new(x, y, z));
    }

    public Chunk SetChunkTypeByLocation(Vector3Int location, ChunkTypeEnum type)
        => GetChunkByLocation(location).SetChunkType(type);

    public DirectionEnum GetChunkDirectionToOrigin(Vector3Int location)
        => GetChunkByLocation(location).directionToOriginChunk;

    public Chunk SetDownHole(Chunk chunk)
    {
        SetChunkTypeByLocation(chunk.location, ChunkTypeEnum.HoleDown);
        return SetChunkTypeByLocation(new(chunk.location.x, chunk.location.y, chunk.location.z - 1), ChunkTypeEnum.HoleUp);
    }

    public Chunk SetEndingChunk(Chunk chunk)
    {
        return SetChunkTypeByLocation(chunk.location, ChunkTypeEnum.End);
    }

    public void SetChunkDirections(Chunk chunk, List<ChunkDesignEnum> pathDirections)
        => chunk.SetChunkDesign(pathDirections);

    public void VisualizeChunks()
        => ChunkDebugManager.VisualizeChunks(chunks, gridSize);
}