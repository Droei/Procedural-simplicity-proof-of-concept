package com.daan.spelunky3D.roomgen;

import com.daan.spelunky3D.pathgen.chunkcore.ChunkManager;
import com.daan.spelunky3D.pathgen.enums.ChunkTypeEnum;
import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.models.Chunk;
import com.daan.spelunky3D.pathgen.models.Vector3Int;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class DungeonWorldBuilder {

    private final World world;
    private final ChunkManager manager;

    ChunkBuilder builder;
    private final int chunkSize = 16;

    private final int originX = 0;
    private final int originY = 64;
    private final int originZ = 0;

    public DungeonWorldBuilder(World world, ChunkManager manager, ChunkBuilder builder) {
        this.world = world;
        this.manager = manager;
        this.builder = builder;
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

        for (int x = -worldSize; x < worldSize; x++)
            for (int y = 0; y < worldSize; y++)
                for (int z = -worldSize; z < worldSize; z++) {

                    world.getBlockAt(originX + x, originY + y, originZ + z)
                            .setType(Material.AIR);
                }
    }

    private void generateChunk(Chunk chunk) {

        int startX = originX + chunk.location.x * chunkSize;
        int startY = originY + chunk.location.z * chunkSize;
        int startZ = originZ + chunk.location.y * chunkSize;

        Vector3Int vector3Int = new Vector3Int(startX, startY, startZ);

        System.out.println("Pasting chunk at: " + startX + ", " + startY + ", " + startZ);

        builder.buildChunk(vector3Int, world, chunk);

        //pasteClipboard(startX, startY, startZ);
    }
}