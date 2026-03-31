using System.Collections.Generic;
using UnityEngine;

public class ChunkPathGenerator
{
    private ChunkManager manager;
    private readonly ChunkHelperFunctions CHF;

    public ChunkPathGenerator(ChunkManager manager, ChunkHelperFunctions chf)
    {
        this.manager = manager;
        CHF = chf;
    }

    public List<DirectionEnum> FindAvailableOpenings(Vector3Int location)
    {
        List<DirectionEnum> available = new();

        foreach (var (dir, directionEnum) in CHF.directions)
        {
            Vector3Int neighbor = location + dir;

            if (!CHF.IsInsideGrid(neighbor))
                continue;

            if (manager.GetChunk(neighbor).chunkType == ChunkTypeEnum.Nothing)
                available.Add(directionEnum);
        }

        return available;
    }

    public List<DirectionEnum> PickRandomDirections(List<DirectionEnum> available, int min, int max)
    {
        int maxPossible = Mathf.Min(max, available.Count);
        int minPossible = Mathf.Min(min, maxPossible);

        int amount = Random.Range(minPossible, maxPossible + 1);

        List<DirectionEnum> chosen = new();

        for (int i = 0; i < amount; i++)
        {
            int index = Random.Range(0, available.Count);
            chosen.Add(available[index]);
            available.RemoveAt(index);
        }
        ChunkDebugManager.PrintDirections(chosen);
        return chosen;
    }
}