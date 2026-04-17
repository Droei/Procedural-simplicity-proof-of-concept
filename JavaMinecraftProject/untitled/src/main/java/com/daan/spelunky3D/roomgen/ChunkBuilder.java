package com.daan.spelunky3D.roomgen;

import com.daan.spelunky3D.entitygen.MonsterSpawner;
import com.daan.spelunky3D.entitygen.PlayerSpawner;
import com.daan.spelunky3D.entitygen.models.MonsterSpawnPoint;
import com.daan.spelunky3D.pathgen.enums.ChunkTypeEnum;
import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.models.Chunk;
import com.daan.spelunky3D.pathgen.models.Vector3Int;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class ChunkBuilder {

    private final SchemLoader loader;

    private final int chunkSize = 16;
    private final int originX = 0;
    private final int originY = 64;
    private final int originZ = 0;

    Vector3Int location;
    MonsterSpawner monsterSpawner;
    PlayerSpawner playerSpawner;
    public ChunkBuilder(SchemLoader loader, MonsterSpawner monsterSpawner, PlayerSpawner playerSpawner) {
        this.loader = loader;
        this.monsterSpawner = monsterSpawner;
        this.playerSpawner = playerSpawner;
    }

    public void buildChunk(Vector3Int location, World world, Chunk chunk) {
        this.location = location;

        List<DirectionEnum> open = chunk.getOpeningDirections();

        if (open.contains(DirectionEnum.SOUTH)) {
            paste(world, loader.get("openingNorth"));
        } else {
            paste(world, loader.get("wallNorth"));
        }

        if (open.contains(DirectionEnum.NORTH)) {
            paste(world, loader.get("openingSouth"));
        } else {
            paste(world, loader.get("wallSouth"));
        }

        if (open.contains(DirectionEnum.WEST)) {
            paste(world, loader.get("openingWest"));
        } else {
            paste(world, loader.get("wallWest"));
        }

        if (open.contains(DirectionEnum.EAST)) {
            paste(world, loader.get("openingEast"));
        } else {
            paste(world, loader.get("wallEast"));
        }

        if(chunk.getChunkType() == ChunkTypeEnum.HOLE_DOWN){
            paste(world, loader.get("floorHole"));
        }
        else if(chunk.getChunkType() == ChunkTypeEnum.END){
            paste(world, loader.get("floorEnd"));
        }
        else if (chunk.getChunkType() == ChunkTypeEnum.START) {

            Clipboard original = loader.get("floorStart");

            BlockArrayClipboard working = new BlockArrayClipboard(original.getRegion());
            working.setOrigin(original.getOrigin());

            for (BlockVector3 pos : original.getRegion()) {
                working.setBlock(pos, original.getBlock(pos));
            }

            playerSpawner.getAndClearCrimsonPlanks(working);
            paste(world, working);
            playerSpawner.teleportPlayers(world, location);
        } else {
            Clipboard original = loader.get("floor");

            BlockArrayClipboard working = new BlockArrayClipboard(original.getRegion());
            working.setOrigin(original.getOrigin());

            for (BlockVector3 pos : original.getRegion()) {
                working.setBlock(pos, original.getBlock(pos));
            }

            monsterSpawner.getAndClearMonsterIndications(working);
            paste(world, working);
            monsterSpawner.spawnMonsters(world, location);
        }

        if(chunk.getChunkType() == ChunkTypeEnum.HOLE_UP){
            paste(world, loader.get("ceilingHole"));
        } else {
            paste(world, loader.get("ceiling"));
        }
    }

    private void paste(World world, Clipboard clipboard) {

        if (clipboard == null) {
            System.out.println("Missing clipboard for paste");
            return;
        }

        try (EditSession session = WorldEdit.getInstance()
                .newEditSession(BukkitAdapter.adapt(world))) {

            BlockVector3 pos = BlockVector3.at(location.x, location.y, location.z);

            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(session)
                    .to(pos)
                    .ignoreAirBlocks(true)
                    .build();

            Operations.complete(operation);

        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }
}