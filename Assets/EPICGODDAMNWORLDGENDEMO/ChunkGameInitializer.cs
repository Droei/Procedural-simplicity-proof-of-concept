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

        Vector3Int startLocation = new(RandomGen.Range(0, gridSize), RandomGen.Range(0, gridSize), gridSize - 1);

        manager.SetChunkTypeByLocation(startLocation, ChunkTypeEnum.Start);

        var startDirs = generator.FindAvailableOpenings(startLocation);

        var startChosen = generator.PickRandomDirections(startDirs, 2, 2);


        manager.AddEmptyChunksInDirections(startLocation, startChosen);
    }
}