using System.Collections.Generic;
using UnityEngine;

public class ChunkType : MonoBehaviour
{
    [SerializeField] ChunkDesignEnum chunkDesign = ChunkDesignEnum.OneWay;
    [SerializeField] ChunkTypeEnum chunkType = ChunkTypeEnum.Normal;
    [SerializeField] List<DirectionEnum> openings;

    public ChunkDesignEnum GetDesign => chunkDesign;
    public ChunkTypeEnum GetType => chunkType;
    public List<DirectionEnum> GetOpenings => openings;
}
