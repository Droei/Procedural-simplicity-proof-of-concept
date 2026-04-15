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
        else
        {
            int randomSeed = RandomGen.Range(0, 9999999);
            RandomGen.SetSeed(seed);
            Debug.Log("Using: " + randomSeed);
        }

        ChunkHelperFunctions.SetGridSize(gridSize);

        chunkManager = new ChunkManager(gridSize);
        pathGenerator = new ChunkPathGenerator(chunkManager);
        initializer = new ChunkGameInitializer(chunkManager, pathGenerator, gridSize);

        chunkManager.SetUpEmptyChunks();

        Chunk start = initializer.SetRandomStart();

        Chunk floorChunk = start;

        for (int i = 0; i < gridSize; i++)
        {
            Chunk upChunk = initializer.GenerateFloor(floorChunk, gridSize - 1);
            floorChunk = upChunk;
            initializer.SetupStartChunk(floorChunk, 1, 3);
        }

        chunkManager.VisualizeChunks();
    }

    public ChunkManager GetChunkManager
        => chunkManager;
    public int GetGridSize
        => gridSize;
}