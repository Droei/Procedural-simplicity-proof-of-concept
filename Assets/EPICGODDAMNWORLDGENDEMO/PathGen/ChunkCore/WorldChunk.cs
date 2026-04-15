using System.Collections.Generic;
using UnityEngine;

public class WorldChunk
{
    int id;
    Vector3Int location;

    List<DirectionEnum> openingDirections = new();
    ChunkDesignEnum chunkDesign = ChunkDesignEnum.None;
    ChunkTypeEnum chunkType = ChunkTypeEnum.Nothing;

    DirectionEnum directionToOriginChunk = DirectionEnum.None;

    public WorldChunk(Vector3Int location)
    {
        id = ChunkHelperFunctions.ParseLocationToId(location);
        this.location = location;
    }

    public int GetId => id;

    public Vector3Int GetLocation => location;
    public void SetLocation(Vector3Int location) => this.location = location;

    public List<DirectionEnum> OpeningDirections => openingDirections;
    public void SetOpeningDirections(List<DirectionEnum> openingDirections) => this.openingDirections = openingDirections;

    public ChunkDesignEnum GetChunkDesign => chunkDesign;
    public void SetChunkDesign(ChunkDesignEnum chunkDesignEnum) => chunkDesign = chunkDesignEnum;

    public ChunkTypeEnum GetChunkType => chunkType;
    public void SetChunkType(ChunkTypeEnum chunkTypeEnum) => chunkType = chunkTypeEnum;

    public DirectionEnum GetDirectionToOriginChunk = DirectionEnum.None;
    public void SetDirectionToOriginChunk(DirectionEnum directionEnum) => directionToOriginChunk = directionEnum;

    public override string ToString()
    {
        string opening = OpeningDirections != null && OpeningDirections.Count > 0
            ? string.Join(", ", OpeningDirections)
            : "None";

        return base.ToString();
    }


}
