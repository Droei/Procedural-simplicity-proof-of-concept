using System.Collections.Generic;
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
        Chunk start = initializer.SetRandomStart();
        List<Chunk> newChunks = initializer.GenerateChunksInOpenDirections(start);
        Chunk upHole = initializer.GenerateChunksInOpenDirectionsWithDownHole(newChunks);
        newChunks = initializer.GenerateChunksInOpenDirections(upHole);
        upHole = initializer.GenerateChunksInOpenDirectionsWithDownHole(newChunks);
        newChunks = initializer.GenerateChunksInOpenDirections(upHole);
        upHole = initializer.GenerateChunksInOpenDirectionsWithEnding(newChunks);


        chunkManager.VisualizeChunks();
    }
}