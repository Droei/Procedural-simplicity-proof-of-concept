using System.Collections.Generic;
using UnityEngine;

public class Chunk
{
    public int id;
    public Vector3Int location;
    public List<PathDirectionEnum> chunkDirections;
    public ChunkTypeEnum chunkType = ChunkTypeEnum.Nothing;

    public DirectionEnum directionToOriginChunk;

    public Chunk(int id, Vector3Int location)
    {
        this.id = id;
        this.location = location;
    }

    public void SetDirections(List<PathDirectionEnum> directions)
    {
        chunkDirections = directions;
    }
}