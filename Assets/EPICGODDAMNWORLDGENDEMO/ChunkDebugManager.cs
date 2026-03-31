using System.Text;
using UnityEngine;

public static class ChunkDebugManager
{
    public static void VisualizeChunks(Chunk[] chunks, int gridSize, ChunkHelperFunctions CHF)
    {
        for (int z = gridSize - 1; z >= 0; z--)
        {
            StringBuilder sb = new();

            sb.AppendLine($"<color=yellow><b>Layer Z = {z}</b></color>");

            for (int y = gridSize - 1; y >= 0; y--)
            {
                for (int x = 0; x < gridSize; x++)
                {
                    Chunk chunk = chunks[CHF.ParseLocationToId(new Vector3Int(x, y, z))];
                    string symbol = chunk.chunkType switch
                    {
                        ChunkTypeEnum.Start => "<color=red>[S" + " " + chunk.chunkDirection.ToString() + "]</color>",
                        ChunkTypeEnum.End => "<color=red>[E]</color>",
                        ChunkTypeEnum.HoleDown => "<color=magenta>[↓]</color>",
                        ChunkTypeEnum.HoleUp => "<color=cyan>[↑]</color>",
                        ChunkTypeEnum.Normal => "<color=green>[.]</color>",
                        ChunkTypeEnum.Nothing => "[]",
                        _ => "[]"
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
}