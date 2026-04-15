package com.daan.spelunky3D.pathgen.debug;

import com.daan.spelunky3D.pathgen.enums.ChunkTypeEnum;
import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.models.Chunk;
import com.daan.spelunky3D.pathgen.models.Vector3Int;
import com.daan.spelunky3D.pathgen.utils.ChunkHelperFunctions;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ChunkDebugManager {

    public static void visualizeChunks(JavaPlugin plugin, Chunk[] chunks, int gridSize) {

        for (int z = gridSize - 1; z >= 0; z--) {

            plugin.getLogger().info("===== Layer Z = " + z + " =====");

            for (int y = gridSize - 1; y >= 0; y--) {

                StringBuilder sb = new StringBuilder();

                for (int x = 0; x < gridSize; x++) {

                    Chunk chunk = chunks[
                            ChunkHelperFunctions.parseLocationToId(new Vector3Int(x, y, z))
                            ];

                    String symbol = switch (chunk.getChunkType()) {
                        case START -> "[S]";
                        case END -> "[E]";
                        case HOLE_DOWN -> "[↓]";
                        case HOLE_UP -> "[↑]";
                        case NORMAL -> "[.]";
                        case NOTHING -> "[]";
                    };

                    sb.append(symbol);
                }

                plugin.getLogger().info(sb.toString());
            }
        }
    }

    public static void logChunks(JavaPlugin plugin, Chunk[] chunks) {
        for (Chunk chunk : chunks) {
            plugin.getLogger().info(chunk.id + " " + chunk.location);
        }
    }

    public static void printDirections(JavaPlugin plugin, List<DirectionEnum> directions) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < directions.size(); i++) {
            sb.append(directions.get(i).name());
            if (i < directions.size() - 1) {
                sb.append(", ");
            }
        }

        plugin.getLogger().info(sb.toString());
    }
}