using UnityEngine;

public class ChunkHelperFunctions
{
    int gridSize = 3;

    public ChunkHelperFunctions(int gridSize)
    {
        this.gridSize = gridSize;
    }

    public readonly (Vector3Int dir, DirectionEnum directionEnum)[] directions =
    {
        (Vector3Int.up, DirectionEnum.Up),
        (Vector3Int.down, DirectionEnum.Down),
        (Vector3Int.left, DirectionEnum.Left),
        (Vector3Int.right, DirectionEnum.Right),
    };

    public int ParseLocationToId(Vector3Int location)
    {
        return location.x + location.y * gridSize + location.z * gridSize * gridSize;

    }

    public bool IsInsideGrid(Vector3Int pos)
    {
        return pos.x >= 0 && pos.x < gridSize &&
               pos.y >= 0 && pos.y < gridSize &&
               pos.z >= 0 && pos.z < gridSize;
    }

    public Vector3Int DirectionToVector(DirectionEnum dir)
    {
        return dir switch
        {
            DirectionEnum.Up => Vector3Int.up,
            DirectionEnum.Down => Vector3Int.down,
            DirectionEnum.Left => Vector3Int.left,
            DirectionEnum.Right => Vector3Int.right,
            _ => Vector3Int.zero
        };
    }

}
