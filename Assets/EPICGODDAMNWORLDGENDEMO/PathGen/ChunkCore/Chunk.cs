using System.Collections.Generic;
using UnityEngine;

public class Chunk
{
    public int id;
    public Vector3Int location;

    private List<DirectionEnum> openingDirections = new();

    public ChunkDesignEnum chunkDesign;
    private ChunkTypeEnum chunkType = ChunkTypeEnum.Nothing;

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


    public void SetChunkDesign(ChunkDesignEnum design)
    {
        chunkDesign = design;
    }

    public ChunkDesignEnum GetChunkDesign
        => chunkDesign;

    public override string ToString()
    {
        string openings = openingDirections != null && openingDirections.Count > 0
            ? string.Join(", ", openingDirections)
            : "None";

        return $"Chunk [" +
               $"ID: {id}, " +
               $"Loc: ({location.x}, {location.y}, {location.z}), " +
               $"Type: {chunkType}, " +
               $"Design: {chunkDesign}, " +
               $"Openings: [{openings}]" +
               $"]";
    }

    public Chunk DetermineChunkDesign()
    {
        List<DirectionEnum> openings = new(openingDirections);

        if (openingDirections != null && openingDirections.Count > 0)
        {
            int count = openings.Count;

            chunkDesign = count switch
            {
                0 => ChunkDesignEnum.None,

                1 => ChunkDesignEnum.OneWay,

                2 => ChunkHelperFunctions.GetOpposite(openings[0]) == openings[1]
                        ? ChunkDesignEnum.Straight
                        : ChunkDesignEnum.Corner,

                3 => ChunkDesignEnum.TShape,

                4 => ChunkDesignEnum.Cross,

                _ => ChunkDesignEnum.None,
            };
        }
        else
        {
            chunkDesign = ChunkDesignEnum.None;
        }
        return this;
    }
}