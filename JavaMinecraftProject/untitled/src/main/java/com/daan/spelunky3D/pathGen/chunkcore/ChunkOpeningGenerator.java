package com.daan.spelunky3D.pathgen.chunkcore;

import com.daan.spelunky3D.pathgen.enums.ChunkTypeEnum;
import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.models.Vector3Int;
import com.daan.spelunky3D.pathgen.utils.ChunkHelperFunctions;
import com.daan.spelunky3D.pathgen.utils.RandomGen;

import java.util.ArrayList;
import java.util.List;

public class ChunkOpeningGenerator {

    private ChunkManager manager;

    public ChunkOpeningGenerator(ChunkManager manager) {
        this.manager = manager;
    }

    public List<DirectionEnum> findAvailableOpenings(Vector3Int location) {

        List<DirectionEnum> available = new ArrayList<>();

        for (DirectionEnum direction : DirectionEnum.values()) {

            if (direction == DirectionEnum.NONE) continue;

            Vector3Int dir = ChunkHelperFunctions.directionToVector(direction);
            Vector3Int neighbor = location.add(dir.x, dir.y, dir.z);

            if (!ChunkHelperFunctions.isInsideGrid(neighbor))
                continue;

            if (manager.getChunkByLocation(neighbor).getChunkType() == ChunkTypeEnum.NOTHING) {
                available.add(direction);
            }
        }

        return available;
    }

    public List<DirectionEnum> pickRandomDirections(List<DirectionEnum> available, int min, int max) {

        int maxPossible = Math.min(max, available.size());
        int minPossible = Math.min(min, maxPossible);

        int amount = RandomGen.range(minPossible, maxPossible + 1);

        List<DirectionEnum> chosen = new ArrayList<>();

        for (int i = 0; i < amount; i++) {

            int index = RandomGen.range(0, available.size());

            chosen.add(available.get(index));
            available.remove(index);
        }

        return chosen;
    }
}