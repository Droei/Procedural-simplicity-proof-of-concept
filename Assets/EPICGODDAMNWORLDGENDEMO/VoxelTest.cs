using UnityEngine;

public class VoxelTest : MonoBehaviour
{
    public int chunkSize = 16;
    public int gridSize = 3;
    public float spacing = 1f;

    public GameObject chunkPrefab;

    void Start()
    {
        GenerateOuterShell();
        GenerateChunkGrid();
    }

    void GenerateOuterShell()
    {
        GameObject outer = new GameObject("Outer Shell");
        outer.transform.SetParent(transform);

        int worldSize = gridSize * chunkSize;

        for (int x = 0; x < worldSize; x++)
            for (int y = 0; y < worldSize; y++)
                for (int z = 0; z < worldSize; z++)
                {
                    bool isWall =
                        x == 0 || x == worldSize - 1 ||
                        y == 0 || y == worldSize - 1 ||
                        z == 0 || z == worldSize - 1;

                    if (isWall)
                    {
                        CreateCube(new Vector3(x, y, z), outer.transform);
                    }
                }
    }

    void GenerateChunkGrid()
    {
        for (int cx = 0; cx < gridSize; cx++)
            for (int cy = 0; cy < gridSize; cy++)
                for (int cz = 0; cz < gridSize; cz++)
                {
                    InstantiateChunk(new Vector3Int(cx, cy, cz));
                }
    }

    void InstantiateChunk(Vector3Int coord)
    {
        GameObject chunk = Instantiate(chunkPrefab, transform);

        chunk.name = $"Chunk {coord}";

        chunk.transform.position = new Vector3(
            coord.x * chunkSize * spacing,
            coord.y * chunkSize * spacing,
            coord.z * chunkSize * spacing
        );
    }

    void CreateCube(Vector3 position, Transform parent)
    {
        GameObject cube = GameObject.CreatePrimitive(PrimitiveType.Cube);
        cube.transform.SetParent(parent);
        cube.transform.localPosition = position * spacing;
    }
}