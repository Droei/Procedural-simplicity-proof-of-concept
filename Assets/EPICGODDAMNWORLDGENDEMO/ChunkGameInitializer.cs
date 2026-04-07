using System.Collections.Generic;
using UnityEngine;

public class ChunkGameInitializer
{
    private ChunkManager manager;
    private ChunkPathGenerator generator;
    private int gridSize;

    public ChunkGameInitializer(ChunkManager manager, ChunkPathGenerator generator, int gridSize)
    {
        this.manager = manager;
        this.generator = generator;
        this.gridSize = gridSize;
    }

    public void SetRandomStart()
    {
        Vector3Int start = new(RandomGen.Range(0, gridSize), RandomGen.Range(0, gridSize), gridSize - 1);

        manager.SetChunkType(start, ChunkTypeEnum.Start);

        var startDirs = generator.FindAvailableOpenings(start);

        var startChosen = generator.PickRandomDirections(startDirs, 2, 2);
        SetUpOtherChunks(manager.SetChunkTypesInDirections(start, startChosen, ChunkTypeEnum.Normal));
    }


    void SetUpOtherChunks(List<Vector3Int> newChunks)
    {
        List<Vector3Int> chunks = new List<Vector3Int>();

        foreach (var chunk in newChunks)
        {
            var potentialChunkDirs = generator.FindAvailableOpenings(chunk);
            var chosenDirs = generator.PickRandomDirections(potentialChunkDirs, 1, 4);

            chunks.Add(manager.SetChunkTypesInDirections(chunk, chosenDirs, ChunkTypeEnum.Normal)[0]);
        }

        Vector3Int upHole = manager.SetDownHole(chunks[chunks.Count - 1]);

        var upholeDirs = generator.FindAvailableOpenings(upHole);
        var upholeChosen = generator.PickRandomDirections(upholeDirs, 2, 3);
        manager.SetChunkTypesInDirections(upHole, upholeChosen, ChunkTypeEnum.Normal);

    }

    public void SetChunkPath(Vector3Int chunk)
    {
        var dirs = generator.FindAvailableOpenings(chunk);
        manager.SetChunkTypesInDirections(chunk, dirs, ChunkTypeEnum.Normal);
    }
}