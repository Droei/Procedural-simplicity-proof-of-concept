using UnityEngine;

public static class ChunkHelperFunctions
{
    static int gridSize = 3;

    public static void SetGridSize(int g)
    {
        gridSize = g;
    }

    public static readonly (Vector3Int dir, DirectionEnum directionEnum)[] directions =
    {
        (Vector3Int.up, DirectionEnum.Up),
        (Vector3Int.down, DirectionEnum.Down),
        (Vector3Int.left, DirectionEnum.Left),
        (Vector3Int.right, DirectionEnum.Right),
    };

    public static int ParseLocationToId(Vector3Int loc)
    {
        return loc.x * gridSize * gridSize
             + loc.y * gridSize
             + loc.z;
    }

    public static bool IsInsideGrid(Vector3Int pos)
    {
        return pos.x >= 0 && pos.x < gridSize &&
               pos.y >= 0 && pos.y < gridSize &&
               pos.z >= 0 && pos.z < gridSize;
    }

    public static Vector3Int DirectionToVector(DirectionEnum dir)
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

    public static DirectionEnum GetOpposite(DirectionEnum dir)
    {
        return dir switch
        {
            DirectionEnum.Up => DirectionEnum.Down,
            DirectionEnum.Down => DirectionEnum.Up,
            DirectionEnum.Left => DirectionEnum.Right,
            DirectionEnum.Right => DirectionEnum.Left,
            _ => dir
        };
    }


}
