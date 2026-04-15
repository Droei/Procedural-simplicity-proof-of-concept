using System.Collections.Generic;
using UnityEngine;

public class ChunkPathGenerator
{
    private ChunkManager manager;

    public ChunkPathGenerator(ChunkManager manager)
    {
        this.manager = manager;
    }

    public List<DirectionEnum> FindAvailableOpenings(Vector3Int location)
    {
        List<DirectionEnum> available = new();

        foreach (var (dir, directionEnum) in ChunkHelperFunctions.directions)
        {
            Vector3Int neighbor = location + dir;

            if (!ChunkHelperFunctions.IsInsideGrid(neighbor))
                continue;

            if (manager.GetChunkByLocation(neighbor).GetChunkType == ChunkTypeEnum.Nothing)
                available.Add(directionEnum);
        }

        return available;
    }

    public List<DirectionEnum> PickRandomDirections(List<DirectionEnum> available, int min, int max)
    {
        int maxPossible = Mathf.Min(max, available.Count);
        int minPossible = Mathf.Min(min, maxPossible);

        int amount = RandomGen.Range(minPossible, maxPossible + 1);

        List<DirectionEnum> chosen = new();


        for (int i = 0; i < amount; i++)
        {
            int index = RandomGen.Range(0, available.Count);
            chosen.Add(available[index]);
            available.RemoveAt(index);
        }
        //ChunkDebugManager.PrintDirections(chosen);
        return chosen;
    }
}