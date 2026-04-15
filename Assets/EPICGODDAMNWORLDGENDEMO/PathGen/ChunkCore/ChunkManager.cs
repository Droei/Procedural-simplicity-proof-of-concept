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

    public Chunk SetDownHole(Chunk chunk)
    {
        SetChunkTypeByLocation(chunk.location, ChunkTypeEnum.HoleDown);
        Chunk upHole = SetChunkTypeByLocation(new(chunk.location.x, chunk.location.y, chunk.location.z - 1), ChunkTypeEnum.HoleUp);
        return upHole;
    }

    public Chunk SetEndingChunk(Chunk chunk)
    {
        return SetChunkTypeByLocation(chunk.location, ChunkTypeEnum.End);
    }

    public void SetChunkDirections(Chunk chunk, ChunkDesignEnum pathDirections)
        => chunk.SetChunkDesign(pathDirections);

    public void VisualizeChunks()
        => ChunkDebugManager.VisualizeChunks(chunks, gridSize);

    public Chunk[] GetChunks
        => chunks;

    public void EvaluateNeighbors(Vector3Int location, List<DirectionEnum> incoming, List<DirectionEnum> blocked)
    {
        foreach (var (dir, directionEnum) in ChunkHelperFunctions.directions)
        {
            Vector3Int neighborLocation = location + dir;

            if (!ChunkHelperFunctions.IsInsideGrid(neighborLocation))
            {
                blocked.Add(directionEnum);
                continue;
            }

            var neighbor = GetChunkByLocation(neighborLocation);

            if (neighbor.GetChunkType == ChunkTypeEnum.Nothing)
                continue;

            var opposite = ChunkHelperFunctions.GetOpposite(directionEnum);

            if (neighbor.GetOpeningDirections.Contains(opposite))
                incoming.Add(directionEnum);
            else
                blocked.Add(directionEnum);
        }
    }
}