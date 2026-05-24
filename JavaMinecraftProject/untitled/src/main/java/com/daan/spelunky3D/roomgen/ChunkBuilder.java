package com.daan.spelunky3D.roomgen;

import com.daan.spelunky3D.entitygen.MonsterSpawner;
import com.daan.spelunky3D.entitygen.PlayerSpawner;
import com.daan.spelunky3D.entitygen.models.MonsterSpawnPoint;
import com.daan.spelunky3D.pathgen.enums.ChunkTypeEnum;
import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.models.Chunk;
import com.daan.spelunky3D.pathgen.models.Vector3Int;
import com.daan.spelunky3D.shopkeeper.shop.ShopSpawner;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
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
    private final ShopSpawner shopSpawner;

    public ChunkBuilder(SchemLoader loader, MonsterSpawner monsterSpawner, PlayerSpawner playerSpawner, ShopSpawner shopSpawner) {
        this.loader = loader;
        this.monsterSpawner = monsterSpawner;
        this.playerSpawner = playerSpawner;
        this.shopSpawner = shopSpawner;
    }

    public void buildChunk(Vector3Int location, World world, Chunk chunk) {
        this.location = location;

        List<DirectionEnum> open = chunk.getOpeningDirections();

        paste(world, prepareClipboard(
                open.contains(DirectionEnum.SOUTH)
                        ? loader.get("openingNorth")
                        : loader.get("wallNorth")
        ));

        paste(world, prepareClipboard(
                open.contains(DirectionEnum.NORTH)
                        ? loader.get("openingSouth")
                        : loader.get("wallSouth")
        ));

        paste(world, prepareClipboard(
                open.contains(DirectionEnum.WEST)
                        ? loader.get("openingWest")
                        : loader.get("wallWest")
        ));

        paste(world, prepareClipboard(
                open.contains(DirectionEnum.EAST)
                        ? loader.get("openingEast")
                        : loader.get("wallEast")
        ));

        Clipboard floor = switch (chunk.getChunkType()) {
            case START -> loader.get("floorStart");
            case END -> loader.get("floorEnd");
            case HOLE_DOWN -> loader.get("floorHole");
            default -> loader.get("floor");
        };

        Clipboard workingFloor = prepareClipboard(floor);

        if (chunk.getChunkType() == ChunkTypeEnum.START) {
            playerSpawner.getAndClearCrimsonPlanks(workingFloor);
            shopSpawner.getAndClearEmeraldBlocks(workingFloor);
        }

        paste(world, workingFloor);

        switch (chunk.getChunkType()) {

            case START -> {
                playerSpawner.teleportPlayers(world, location);
                shopSpawner.spawnShops(world,location);
            }

            case END -> {
                monsterSpawner.spawnMonsters(world, location);
            }

            case HOLE_DOWN -> {
            }

            default -> {
                monsterSpawner.spawnMonsters(world, location);
            }
        }

        paste(world, prepareClipboard(
                chunk.getChunkType() == ChunkTypeEnum.HOLE_UP
                        ? loader.get("ceilingHole")
                        : loader.get("ceiling")
        ));
    }

    private Clipboard prepareClipboard(Clipboard original) {
        BlockArrayClipboard working = new BlockArrayClipboard(original.getRegion());
        working.setOrigin(original.getOrigin());

        for (BlockVector3 pos : original.getRegion()) {
            working.setBlock(pos, original.getBlock(pos));
        }

        applyOreDistribution(working);

        monsterSpawner.getAndClearMonsterIndications(working);

        return working;
    }
    private void applyOreDistribution(BlockArrayClipboard clipboard) {

        RandomPattern stonePattern = new RandomPattern();
        stonePattern.add(BlockTypes.STONE.getDefaultState(), 0.70);
        stonePattern.add(BlockTypes.ANDESITE.getDefaultState(), 0.10);
        stonePattern.add(BlockTypes.COBBLESTONE.getDefaultState(), 0.10);

        stonePattern.add(BlockTypes.IRON_ORE.getDefaultState(), 0.03);
        stonePattern.add(BlockTypes.GOLD_ORE.getDefaultState(), 0.015);
        stonePattern.add(BlockTypes.DIAMOND_ORE.getDefaultState(), 0.005);

        RandomPattern deepslatePattern = new RandomPattern();
        deepslatePattern.add(BlockTypes.DEEPSLATE.getDefaultState(), 0.75);
        deepslatePattern.add(BlockTypes.COBBLED_DEEPSLATE.getDefaultState(), 0.15);

        deepslatePattern.add(BlockTypes.DEEPSLATE_IRON_ORE.getDefaultState(), 0.025);
        deepslatePattern.add(BlockTypes.DEEPSLATE_GOLD_ORE.getDefaultState(), 0.015);
        deepslatePattern.add(BlockTypes.DEEPSLATE_DIAMOND_ORE.getDefaultState(), 0.005);

        for (BlockVector3 pos : clipboard.getRegion()) {
            BlockState current = clipboard.getBlock(pos);
            String id = current.getBlockType().id();

            if (id.equals("minecraft:stone")
                    || id.equals("minecraft:andesite")
                    || id.equals("minecraft:cobblestone")
                    || id.equals("minecraft:gravel")) {

                clipboard.setBlock(pos, stonePattern.applyBlock(pos));
            }

            else if (id.equals("minecraft:cobbled_deepslate")
                    || id.equals("minecraft:deepslate")) {

                clipboard.setBlock(pos, deepslatePattern.applyBlock(pos));
            }
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