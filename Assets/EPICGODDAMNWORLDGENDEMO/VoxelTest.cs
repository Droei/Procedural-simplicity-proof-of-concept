using System.Collections.Generic;
using UnityEngine;

public class VoxelTest : MonoBehaviour
{
    public int chunkSize = 16;
    int gridSize = 5;
    public float spacing = 1f;

    [SerializeField] PathGenerator pathGenerator;

    [SerializeField] List<ChunkType> chunkTypes;

    ChunkManager chunkManager;
    void Start()
    {
        chunkManager = pathGenerator.GetChunkManager;
        gridSize = pathGenerator.GetGridSize;
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
                    Chunk chunk = chunkManager.GetChunkByLocation(new(cx, cy, cz))
                                                .DetermineChunkDesign();

                    ChunkTypeEnum chunkType = chunk.GetChunkType;
                    ChunkDesignEnum chunkDesign = chunk.GetChunkDesign;

                    var openings = new HashSet<DirectionEnum>(chunk.GetOpeningDirections);

                    ChunkType match = FindMatchingChunk(chunkType, chunkDesign, openings);

                    if (match != null)
                    {
                        InstantiateChunk(new(cx, cy, cz), match);
                    }
                    else
                    {
                        Debug.LogWarning($"No matching chunk for {chunk}");
                    }
                }
    }

    private ChunkType FindMatchingChunk(ChunkTypeEnum type, ChunkDesignEnum design, HashSet<DirectionEnum> requiredOpenings)
    {
        foreach (var chunkType in chunkTypes)
        {
            if (type == ChunkTypeEnum.Nothing || design == ChunkDesignEnum.None)
                return chunkType;

            if (chunkType.GetType != type)
                continue;

            if (chunkType.GetDesign != design)
                continue;

            var prefabOpenings = new HashSet<DirectionEnum>(chunkType.GetOpenings);

            if (prefabOpenings.SetEquals(requiredOpenings))
                return chunkType;
        }

        return null;
    }

    void InstantiateChunk(Vector3Int coord, ChunkType c)
    {
        GameObject chunk = Instantiate(c.gameObject, transform);

        chunk.name = $"Chunk {coord}";

        chunk.transform.position = new Vector3(
            coord.x * chunkSize * spacing,
            coord.z * chunkSize * spacing,
            coord.y * chunkSize * spacing
        );
    }

    void CreateCube(Vector3 position, Transform parent)
    {
        GameObject cube = GameObject.CreatePrimitive(PrimitiveType.Cube);
        cube.transform.SetParent(parent);
        cube.transform.localPosition = position * spacing;
    }
}