using UnityEngine;

public class PathGenerator : MonoBehaviour
{
    [SerializeField] int gridSize = 4;

    [SerializeField] bool debugMode = false;
    [SerializeField] int seed = 0;

    ChunkManager chunkManager;
    ChunkPathGenerator pathGenerator;
    ChunkGameInitializer initializer;

    private void Awake()
    {
        if (debugMode)
            RandomGen.SetSeed(seed);

        ChunkHelperFunctions.SetGridSize(gridSize);

        chunkManager = new ChunkManager(gridSize);
        pathGenerator = new ChunkPathGenerator(chunkManager);
        initializer = new ChunkGameInitializer(chunkManager, pathGenerator, gridSize);

        chunkManager.SetUpEmptyChunks();
        Chunk start = initializer.SetRandomStart();
        initializer.GenerateFloor(start, 3);

        //List<Chunk> newChunks = initializer.GenerateChunksInOpenDirections(start);



        //Chunk upHole = initializer.GenerateChunksInOpenDirectionsWithDownHole(newChunks);
        //newChunks = initializer.GenerateChunksInOpenDirections(upHole);
        //upHole = initializer.GenerateChunksInOpenDirectionsWithDownHole(newChunks);
        //newChunks = initializer.GenerateChunksInOpenDirections(upHole);
        //upHole = initializer.GenerateChunksInOpenDirectionsWithEnding(newChunks);


        chunkManager.VisualizeChunks();
    }

    public ChunkManager GetChunkManager
        => chunkManager;
    public int GetGridSize
        => gridSize;
}