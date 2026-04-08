using System.Collections.Generic;
using System.Text;
using UnityEngine;

public static class ChunkDebugManager
{
    public static void VisualizeChunks(Chunk[] chunks, int gridSize)
    {
        for (int z = gridSize - 1; z >= 0; z--)
        {
            StringBuilder sb = new();

            sb.AppendLine($"<color=yellow><b>Layer Z = {z}</b></color>");

            for (int y = gridSize - 1; y >= 0; y--)
            {
                for (int x = 0; x < gridSize; x++)
                {
                    Chunk chunk = chunks[ChunkHelperFunctions.ParseLocationToId(new Vector3Int(x, y, z))];
                    string symbol = chunk.GetChunkType switch
                    {
                        ChunkTypeEnum.Start => "<color=red>[S]</color>",
                        ChunkTypeEnum.End => "<color=red>[E]</color>",
                        ChunkTypeEnum.HoleDown => "<color=magenta>[↓]</color>",
                        ChunkTypeEnum.HoleUp => "<color=cyan>[↑]</color>",
                        ChunkTypeEnum.Normal => "<color=green>[.]</color>",
                        ChunkTypeEnum.Nothing => "[]"
                    };

                    sb.Append(symbol);
                }

                sb.AppendLine();
            }

            Debug.Log(sb.ToString());
        }
    }

    public static void LogChunks(Chunk[] chunks)
    {
        foreach (Chunk chunk in chunks)
        {
            Debug.Log(chunk.id + " " + chunk.location);
        }
    }

    public static void PrintDirections(List<DirectionEnum> directions)
    {
        Debug.Log(string.Join(", ", directions));
    }
}