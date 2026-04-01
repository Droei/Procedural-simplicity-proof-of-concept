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

    public Chunk GetChunk(Vector3Int location)
        => chunks[ChunkHelperFunctions.ParseLocationToId(location)];

    public void SetChunk(Vector3Int location, Chunk chunk)
        => chunks[ChunkHelperFunctions.ParseLocationToId(location)] = chunk;


    public void SetChunkType(Vector3Int location, ChunkTypeEnum type)
        => GetChunk(location).chunkType = type;

    public DirectionEnum GetChunkDirectionToOrigin(Vector3Int location)
        => GetChunk(location).directionToOriginChunk;

    public List<Vector3Int> SetChunkTypesInDirections(Vector3Int location, List<DirectionEnum> directions, ChunkTypeEnum type)
    {
        List<Vector3Int> createdChunks = new();

        foreach (var direction in directions)
        {
            Vector3Int offset = ChunkHelperFunctions.DirectionToVector(direction);
            Vector3Int target = location + offset;

            Chunk chunk = GetChunk(target);
            chunk.chunkType = type;
            chunk.directionToOriginChunk = ChunkHelperFunctions.GetOpposite(direction);

            createdChunks.Add(target);
        }
        return createdChunks;
    }

    public void SetChunkDirections(Vector3Int location, List<PathDirectionEnum> pathDirections)
        => GetChunk(location).SetDirections(pathDirections);

    public void VisualizeChunks()
        => ChunkDebugManager.VisualizeChunks(chunks, gridSize);
}