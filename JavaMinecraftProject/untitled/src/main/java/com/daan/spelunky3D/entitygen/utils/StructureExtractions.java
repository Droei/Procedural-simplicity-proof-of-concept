package com.daan.spelunky3D.entitygen.utils;

import com.daan.spelunky3D.entitygen.enums.SpawnableMonstersEnum;
import com.daan.spelunky3D.entitygen.models.MonsterSpawnPoint;
import com.sk89q.worldedit.extent.clipboard.Clipboard;

import java.util.List;

public class StructureExtractions {

    private StructureExtractions() {}

    public static List<MonsterSpawnPoint> extractMonsters(Clipboard clipboard) {

        List<MonsterSpawnPoint> points = ClipboardExtractor.extract(
                clipboard,
                (state, relative) -> {

                    SpawnableMonstersEnum type =
                            MarkerMappers.mapToMonster(state);

                    if (type == null) return null;

                    return new MonsterSpawnPoint(type, relative);
                }
        );

        logSpawnPoints(points);

        return points;
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