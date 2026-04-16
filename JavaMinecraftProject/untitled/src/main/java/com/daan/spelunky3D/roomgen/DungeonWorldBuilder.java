package com.daan.spelunky3D.roomgen;

import com.daan.spelunky3D.pathgen.chunkcore.ChunkManager;
import com.daan.spelunky3D.pathgen.enums.ChunkTypeEnum;
import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.models.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class DungeonWorldBuilder {

    private final World world;
    private final ChunkManager manager;

    private final int chunkSize = 16;
    private final int baseY = 64;

    public DungeonWorldBuilder(World world, ChunkManager manager) {
        this.world = world;
        this.manager = manager;
    }

    public void generate() {

        clearArea();

        for (Chunk chunk : manager.getChunks()) {

            if (chunk.getChunkType() == ChunkTypeEnum.NOTHING)
                continue;

            generateChunk(chunk);
        }
    }

    private void clearArea() {

        int worldSize = 5 * chunkSize;

        for (int x = 0; x < worldSize; x++)
            for (int y = 0; y < worldSize; y++)
                for (int z = 0; z < worldSize; z++) {

                    world.getBlockAt(x, baseY + y, z).setType(Material.AIR);
                }
    }

    private void generateChunk(Chunk chunk) {

        int startX = chunk.location.x * chunkSize;
        int startY = baseY + chunk.location.y * chunkSize;
        int startZ = chunk.location.z * chunkSize;

        int size = chunkSize;

        for (int y = 0; y < size; y++)
            for (int z = 0; z < size; z++)
                world.getBlockAt(startX, startY + y, startZ + z)
                        .setType(Material.STONE);

        for (int y = 0; y < size; y++)
            for (int z = 0; z < size; z++)
                world.getBlockAt(startX + size - 1, startY + y, startZ + z)
                        .setType(Material.STONE);

        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                world.getBlockAt(startX + x, startY + y, startZ)
                        .setType(Material.STONE);

        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                world.getBlockAt(startX + x, startY + y, startZ + size - 1)
                        .setType(Material.STONE);

        for (int x = 0; x < size; x++)
            for (int z = 0; z < size; z++)
                world.getBlockAt(startX + x, startY, startZ + z)
                        .setType(Material.STONE);

        for (int x = 0; x < size; x++)
            for (int z = 0; z < size; z++)
                world.getBlockAt(startX + x, startY + size - 1, startZ + z)
                        .setType(Material.STONE);
    }
}