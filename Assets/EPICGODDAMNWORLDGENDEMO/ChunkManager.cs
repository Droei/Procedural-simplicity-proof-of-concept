using System.Collections.Generic;
using UnityEngine;

public class ChunkManager
{
    private Chunk[] chunks;
    private ChunkHelperFunctions CHF;
    int gridSize;

    public ChunkManager(int gridSize)
    {
        this.gridSize = gridSize;
        CHF = new(gridSize);
        chunks = new Chunk[gridSize * gridSize * gridSize];
    }

    public Chunk GetChunk(Vector3Int location)
        => chunks[CHF.ParseLocationToId(location)];

    public void SetChunk(Vector3Int location, Chunk chunk)
        => chunks[CHF.ParseLocationToId(location)] = chunk;


    public void SetChunkType(Vector3Int location, ChunkTypeEnum type)
        => GetChunk(location).chunkType = type;

    public void SetChunkTypesInDirections(Vector3Int location, List<DirectionEnum> directions, ChunkTypeEnum type)
    {
        foreach (var direction in directions)
        {
            Vector3Int offset = CHF.DirectionToVector(direction);
            Vector3Int target = location + offset;

            GetChunk(target).chunkType = type;
        }
    }

    public void SetChunkDirections(Vector3Int location, List<PathDirectionEnum> pathDirections)
        => GetChunk(location).SetDirections(pathDirections);

    public void VisualizeChunks()
        => ChunkDebugManager.VisualizeChunks(chunks, gridSize, CHF);
}