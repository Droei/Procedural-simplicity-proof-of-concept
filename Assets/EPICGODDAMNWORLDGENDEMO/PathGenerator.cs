using UnityEngine;

public class PathGenerator : MonoBehaviour
{
    [SerializeField] int gridSize = 3;

    ChunkManager chunkManager;
    ChunkHelperFunctions chf;
    ChunkPathGenerator pathGenerator;
    ChunkGameInitializer initializer;

    private void Start()
    {
        chunkManager = new ChunkManager(gridSize);
        chf = new ChunkHelperFunctions(gridSize);
        pathGenerator = new ChunkPathGenerator(chunkManager, chf);
        initializer = new ChunkGameInitializer(chunkManager, pathGenerator, gridSize);

        for (int x = 0; x < gridSize; x++)
            for (int y = 0; y < gridSize; y++)
                for (int z = 0; z < gridSize; z++)
                {
                    chunkManager.SetChunk(new Vector3Int(x, y, z), new Chunk(0, new Vector3Int(x, y, z)));
                }

        initializer.SetRandomStartAndEnd();
        chunkManager.VisualizeChunks();
    }
}