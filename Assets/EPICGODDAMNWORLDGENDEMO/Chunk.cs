using UnityEngine;

public class Chunk
{
    public int id;
    public Vector3 location;
    public PathDirectionEnum chunkDirection;
    public ChunkTypeEnum chunkType = ChunkTypeEnum.Nothing;

    public Chunk(int id, Vector3 location)
    {
        this.id = id;
        this.location = location;
    }
}