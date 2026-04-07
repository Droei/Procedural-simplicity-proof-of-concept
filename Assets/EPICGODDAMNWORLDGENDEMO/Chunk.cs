using System.Collections.Generic;
using UnityEngine;

public class Chunk
{
    public int id;
    public Vector3Int location;

    public List<DirectionEnum> openingDirections;

    public List<PathDirectionEnum> chunkDirections;
    public ChunkTypeEnum chunkType = ChunkTypeEnum.Nothing;

    public DirectionEnum directionToOriginChunk;

    public Chunk(Vector3Int location)
    {
        id = ChunkHelperFunctions.ParseLocationToId(location);
        this.location = location;
    }

    public void SetDirections(List<PathDirectionEnum> directions)
    {
        chunkDirections = directions;
    }
}