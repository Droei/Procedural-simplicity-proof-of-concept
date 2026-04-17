package com.daan.spelunky3D.entitygen.utils;

import com.daan.spelunky3D.entitygen.enums.SpawnableMonstersEnum;
import com.daan.spelunky3D.entitygen.models.MonsterSpawnPoint;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;

import java.util.List;

public class StructureExtractions {

    private StructureExtractions() {}

    public static List<MonsterSpawnPoint> extractMonsters(Clipboard clipboard) {

        List<MonsterSpawnPoint> points = ClipboardExtractor.extract(
                clipboard, (state, relative) -> {
                    SpawnableMonstersEnum type =
                            MarkerMappers.mapToMonster(state);

                    if (type == null) return null;

                    return new MonsterSpawnPoint(type, relative);
                }
        );
        return points;
    }
    public static List<BlockVector3> extractCrimsonPlanks(Clipboard clipboard) {
        return ClipboardExtractor.extract(clipboard, MarkerMappers::mapToCrimsonPlank);
    }
}