using UnityEngine;

public class PathGenerator : MonoBehaviour
{
    [SerializeField] int gridSize = 3;

    ChunkManager chunkManager;

    private void Start()
    {
        chunkManager = new ChunkManager(gridSize);

        for (int x = 0; x < gridSize; x++)
            for (int y = 0; y < gridSize; y++)
                for (int z = 0; z < gridSize; z++)
                {
                    chunkManager.AddChunkAtLocation(new(x, y, z));
                }

        chunkManager.SetRandomStartAndEnd();
        chunkManager.VisualizeChunks();
    }
}
