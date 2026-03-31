using System.Collections.Generic;
using UnityEngine;

public class Chunk
{
    public int id;
    public Vector3 location;
    public List<PathDirectionEnum> chunkDirections;
    public ChunkTypeEnum chunkType = ChunkTypeEnum.Nothing;

    public Chunk(int id, Vector3 location)
    {
        this.id = id;
        this.location = location;
    }

    public void SetDirections(List<PathDirectionEnum> directions)
    {
        chunkDirections = directions;
    }
}