package com.daan.spelunky3D.pathgen.utils;

import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.models.Vector3Int;

public class ChunkHelperFunctions {

    private static int gridSize = 3;

    public static void setGridSize(int g) {
        gridSize = g;
    }

    public static final DirectionEntry[] directions = {
            new DirectionEntry(new Vector3Int(0, 1, 0), DirectionEnum.NORTH),
            new DirectionEntry(new Vector3Int(0, -1, 0), DirectionEnum.SOUTH),
            new DirectionEntry(new Vector3Int(-1, 0, 0), DirectionEnum.WEST),
            new DirectionEntry(new Vector3Int(1, 0, 0), DirectionEnum.EAST)
    };

    public static int parseLocationToId(Vector3Int loc) {
        return loc.x * gridSize * gridSize
                + loc.y * gridSize
                + loc.z;
    }

    public static boolean isInsideGrid(Vector3Int pos) {
        return pos.x >= 0 && pos.x < gridSize &&
                pos.y >= 0 && pos.y < gridSize &&
                pos.z >= 0 && pos.z < gridSize;
    }

    public static Vector3Int directionToVector(DirectionEnum dir) {
        return switch (dir) {
            case NORTH -> new Vector3Int(0, 1, 0);
            case SOUTH -> new Vector3Int(0, -1, 0);
            case WEST -> new Vector3Int(-1, 0, 0);
            case EAST -> new Vector3Int(1, 0, 0);
            default -> new Vector3Int(0, 0, 0);
        };
    }

    public static DirectionEnum getOpposite(DirectionEnum dir) {
        return switch (dir) {
            case NORTH -> DirectionEnum.SOUTH;
            case SOUTH -> DirectionEnum.NORTH;
            case WEST -> DirectionEnum.EAST;
            case EAST -> DirectionEnum.WEST;
            default -> dir;
        };
    }
}