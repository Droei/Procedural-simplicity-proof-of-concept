using System.Collections.Generic;
using UnityEngine;

public class Chunk
{
    public int id;
    public Vector3Int location;

    private List<DirectionEnum> openingDirections;

    public List<ChunkDesignEnum> chunkDesign;
    private ChunkTypeEnum chunkType = ChunkTypeEnum.Nothing;

    public DirectionEnum directionToOriginChunk;

    public Chunk(Vector3Int location)
    {
        id = ChunkHelperFunctions.ParseLocationToId(location);
        this.location = location;
    }

    public Chunk SetOpeningDirections(List<DirectionEnum> openingDirections)
    {
        this.openingDirections = openingDirections;
        return this;
    }

    public Chunk SetChunkType(ChunkTypeEnum chunkType)
    {
        this.chunkType = chunkType;
        return this;
    }

    public ChunkTypeEnum GetChunkType
        => chunkType;

    public List<DirectionEnum> GetOpeningDirections
        => openingDirections;


    public void SetChunkDesign(List<ChunkDesignEnum> design)
    {
        chunkDesign = design;
    }
}