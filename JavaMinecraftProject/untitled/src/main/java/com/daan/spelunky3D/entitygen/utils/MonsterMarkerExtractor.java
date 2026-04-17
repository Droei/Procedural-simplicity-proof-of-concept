package com.daan.spelunky3D.entitygen.utils;

import com.daan.spelunky3D.entitygen.enums.SpawnableMonstersEnum;
import com.daan.spelunky3D.entitygen.models.MonsterSpawnPoint;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;

import java.util.ArrayList;
import java.util.List;

import static com.sk89q.worldedit.world.block.BlockTypes.AIR;

public class MonsterMarkerExtractor {

    private MonsterMarkerExtractor() {
        // prevent instantiation
    }

    public static List<MonsterSpawnPoint> extract(Clipboard clipboard) {

        List<MonsterSpawnPoint> points = new ArrayList<>();

        BlockVector3 min = clipboard.getMinimumPoint();
        BlockVector3 max = clipboard.getMaximumPoint();

        for (int x = min.x(); x <= max.x(); x++) {
            for (int y = min.y(); y <= max.y(); y++) {
                for (int z = min.z(); z <= max.z(); z++) {

                    BlockVector3 pos = BlockVector3.at(x, y, z);
                    BlockState state = clipboard.getBlock(pos);

                    SpawnableMonstersEnum type = mapToMonster(state);
                    if (type == null) continue;

                    BlockVector3 relative = pos.subtract(min);

                    points.add(new MonsterSpawnPoint(type, relative));

                    removeBlock(clipboard, pos);
                }
            }
        }

        logSpawnPoints(points);

        return points;
    }

    private static SpawnableMonstersEnum mapToMonster(BlockState state) {
        return switch (state.getBlockType().id()) {
            case "minecraft:skeleton_skull" -> SpawnableMonstersEnum.SKELETON;
            case "minecraft:zombie_head" -> SpawnableMonstersEnum.ZOMBIE;
            case "minecraft:dragon_head" -> SpawnableMonstersEnum.SPIDER;
            case "minecraft:creeper_head" -> SpawnableMonstersEnum.CREEPER;
            default -> null;
        };
    }

    private static void removeBlock(Clipboard clipboard, BlockVector3 pos) {
        try {
            clipboard.setBlock(pos, AIR.getDefaultState());
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
    }

    private static void logSpawnPoints(List<MonsterSpawnPoint> points) {
        System.out.println("=== Monster Spawn Points Found ===");

        for (MonsterSpawnPoint point : points) {
            System.out.println(
                    "Monster: " + point.type +
                            " at " + point.position.x() + ", " +
                            point.position.y() + ", " +
                            point.position.z()
            );
        }

        System.out.println("Total spawn points: " + points.size());
    }
}