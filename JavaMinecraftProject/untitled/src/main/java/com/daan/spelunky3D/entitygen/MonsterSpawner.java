package com.daan.spelunky3D.entitygen;

import com.daan.spelunky3D.entitygen.enums.SpawnableMonstersEnum;
import com.daan.spelunky3D.entitygen.models.MonsterSpawnPoint;
import com.daan.spelunky3D.pathgen.models.Vector3Int;
import com.daan.spelunky3D.pathgen.utils.RandomGen;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

import static com.sk89q.worldedit.world.block.BlockTypes.AIR;

public class MonsterSpawner {

    private final List<MonsterSpawnPoint> spawnPoints = new ArrayList<>();

    public List<MonsterSpawnPoint> getAndClearMonsterIndications(Clipboard clipboard) {

        spawnPoints.clear();

        BlockVector3 min = clipboard.getMinimumPoint();
        BlockVector3 max = clipboard.getMaximumPoint();

        for (int x = min.x(); x <= max.x(); x++) {
            for (int y = min.y(); y <= max.y(); y++) {
                for (int z = min.z(); z <= max.z(); z++) {

                    BlockVector3 pos = BlockVector3.at(x, y, z);
                    BlockState state = clipboard.getBlock(pos);

                    String id = state.getBlockType().id();

                    SpawnableMonstersEnum type = null;

                    switch (id) {
                        case "minecraft:skeleton_skull" -> type = SpawnableMonstersEnum.SKELETON;
                        case "minecraft:zombie_head" -> type = SpawnableMonstersEnum.ZOMBIE;
                        case "minecraft:dragon_head" -> type = SpawnableMonstersEnum.SPIDER;
                        case "minecraft:creeper_head" -> type = SpawnableMonstersEnum.CREEPER;
                    }

                    if (type != null) {
                        BlockVector3 relative = pos.subtract(min);

                        spawnPoints.add(new MonsterSpawnPoint(type, relative));

                        try {
                            clipboard.setBlock(pos, AIR.getDefaultState());
                        } catch (WorldEditException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        System.out.println("=== Monster Spawn Points Found ===");

        for (MonsterSpawnPoint point : spawnPoints) {
            System.out.println(
                    "Monster: " + point.type +
                            " at " + point.position.x() + ", " +
                            point.position.y() + ", " +
                            point.position.z()
            );
        }

        System.out.println("Total spawn points: " + spawnPoints.size());

        return new ArrayList<>(spawnPoints);
    }

    public void spawnMonsters(World world, Vector3Int chunkLocation) {
        for (MonsterSpawnPoint point : spawnPoints) {

            if (RandomGen.value() > point.type.getSpawnChance())
                continue;

            int x = chunkLocation.x - point.position.x();
            int y = chunkLocation.y + point.position.y();
            int z = chunkLocation.z - point.position.z();

            Location loc = new Location(world, x + 0.5, y, z + 0.5);

            System.out.println("SPAWN @ " + loc);

            world.spawnEntity(loc, switch (point.type) {
                case SKELETON -> EntityType.SKELETON;
                case ZOMBIE -> EntityType.ZOMBIE;
                case SPIDER -> EntityType.SPIDER;
                case CREEPER -> EntityType.CREEPER;
            });
        }
    }

}