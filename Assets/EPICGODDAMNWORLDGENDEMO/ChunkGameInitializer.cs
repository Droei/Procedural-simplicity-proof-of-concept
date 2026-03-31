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

    public void SetRandomStartAndEnd()
    {
        Vector3Int start = new(RandomGen.Range(0, gridSize), RandomGen.Range(0, gridSize), gridSize - 1);
        Vector3Int end = new(RandomGen.Range(0, gridSize), RandomGen.Range(0, gridSize), 0);

        manager.SetChunkType(start, ChunkTypeEnum.Start);
        manager.SetChunkType(end, ChunkTypeEnum.End);

        var startDirs = generator.FindAvailableOpenings(start);
        var endDirs = generator.FindAvailableOpenings(end);

        var startChosen = generator.PickRandomDirections(startDirs, 2, 2);
        manager.SetChunkTypesInDirections(start, startChosen, ChunkTypeEnum.Normal);

        var endChosen = generator.PickRandomDirections(endDirs, 1, 1);
        manager.SetChunkTypesInDirections(end, endChosen, ChunkTypeEnum.Normal);
    }
}