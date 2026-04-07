using UnityEngine;

public class PathGenerator : MonoBehaviour
{
    [SerializeField] int gridSize = 3;

    [SerializeField] bool debugMode = false;
    [SerializeField] int seed = 0;

    ChunkManager chunkManager;
    ChunkPathGenerator pathGenerator;
    ChunkGameInitializer initializer;

    private void Start()
    {
        if (debugMode)
            RandomGen.SetSeed(seed);

        chunkManager = new ChunkManager(gridSize);
        pathGenerator = new ChunkPathGenerator(chunkManager);
        initializer = new ChunkGameInitializer(chunkManager, pathGenerator, gridSize);

        chunkManager.SetUpEmptyChunks();
        initializer.SetRandomStart();
        chunkManager.VisualizeChunks();
    }
}