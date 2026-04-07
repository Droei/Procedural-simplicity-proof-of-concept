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

    public void SetChunkTypeByLocation(Vector3Int location, ChunkTypeEnum type)
        => GetChunkByLocation(location).chunkType = type;

    public DirectionEnum GetChunkDirectionToOrigin(Vector3Int location)
        => GetChunkByLocation(location).directionToOriginChunk;

    public void SetDownHole(Chunk chunk)
    {
        SetChunkTypeByLocation(chunk.location, ChunkTypeEnum.HoleDown);
        SetChunkTypeByLocation(new(chunk.location.x, chunk.location.y, chunk.location.z - 1), ChunkTypeEnum.HoleUp);
    }

    public List<Chunk> AddEmptyChunksInDirections(Vector3Int location, List<DirectionEnum> directions)
    {
        List<Chunk> activeChunks = new();

        foreach (var direction in directions)
        {
            Vector3Int offset = ChunkHelperFunctions.DirectionToVector(direction);
            Vector3Int target = location + offset;

            Chunk chunk = GetChunkByLocation(target);
            chunk.chunkType = ChunkTypeEnum.Normal;
            chunk.directionToOriginChunk = ChunkHelperFunctions.GetOpposite(direction);

            activeChunks.Add(chunk);
        }
        return activeChunks;
    }

    public void SetChunkDirections(Chunk chunk, List<PathDirectionEnum> pathDirections)
        => chunk.SetDirections(pathDirections);

    public void VisualizeChunks()
        => ChunkDebugManager.VisualizeChunks(chunks, gridSize);
}