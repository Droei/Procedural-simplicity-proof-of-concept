using System.Collections.Generic;
using System.Text;
using UnityEngine;

public class ChunkManager
{
    int gridSize = 0;
    Chunk[] chunks;

    ChunkHelperFunctions CHF;

    public ChunkManager(int gridSize)
    {
        this.gridSize = gridSize;
        CHF = new(gridSize);
        chunks = new Chunk[gridSize * gridSize * gridSize];
    }

    public void AddChunkAtLocation(Vector3Int location)
    {
        chunks[CHF.ParseLocationToId(location)] = new Chunk(CHF.ParseLocationToId(location), location);
    }

    public void SetChunkType(Vector3Int location, ChunkTypeEnum chunkTypeEnum)
    {
        chunks[CHF.ParseLocationToId(location)].chunkType = chunkTypeEnum;
    }


    public void SetRandomChunkPathDirection(Vector3Int location, int min, int max)
    {
        List<DirectionEnum> availableDirections = FindAvailableOpenings(location);

        StringBuilder sb = new();
        sb.Append($"Available at {location}: ");
        sb.Append(string.Join(", ", availableDirections));

        if (availableDirections.Count == 0)
        {
            sb.Append(" | No available directions");
            Debug.Log(sb.ToString());
            return;
        }

        int maxPossible = Mathf.Min(max, availableDirections.Count);
        int minPossible = Mathf.Min(min, maxPossible);

        int amountToPick = Random.Range(minPossible, maxPossible + 1);

        List<DirectionEnum> chosen = new();

        for (int i = 0; i < amountToPick; i++)
        {
            int index = Random.Range(0, availableDirections.Count);
            chosen.Add(availableDirections[index]);
            availableDirections.RemoveAt(index);
        }

        sb.Append($" | Chosen ({amountToPick}): ");
        sb.Append(string.Join(", ", chosen));

        Debug.Log(sb.ToString());
    }

    public List<DirectionEnum> FindAvailableOpenings(Vector3Int location)
    {
        List<DirectionEnum> availableDirections = new();

        foreach (var (dir, directionEnum) in CHF.directions)
            if (CheckDirection(location, dir))
                availableDirections.Add(directionEnum);

        return availableDirections;
    }

    private bool CheckDirection(Vector3Int origin, Vector3Int direction)
    {
        Vector3Int neighbor = origin + direction;

        if (!CHF.IsInsideGrid(neighbor))
            return false;

        int index = neighbor.x
                  + neighbor.y * gridSize
                  + neighbor.z * gridSize * gridSize;

        if (chunks[index].chunkType == ChunkTypeEnum.Nothing)
        {
            return true;
        }
        return false;
    }

    public void SetRandomStartAndEnd()
    {
        int startX = Random.Range(0, gridSize);
        int startY = Random.Range(0, gridSize);

        int endX = Random.Range(0, gridSize);
        int endY = Random.Range(0, gridSize);

        Vector3Int startPos = new(startX, startY, gridSize - 1);
        Vector3Int endPos = new(endX, endY, 0);

        SetChunkType(startPos, ChunkTypeEnum.Start);
        SetRandomChunkPathDirection(startPos, 2, 2);
        SetChunkType(endPos, ChunkTypeEnum.End);
        SetRandomChunkPathDirection(endPos, 1, 1);
    }

    public void VisualizeChunks()
    {
        ChunkDebugManager.VisualizeChunks(chunks, gridSize, CHF);
    }
}