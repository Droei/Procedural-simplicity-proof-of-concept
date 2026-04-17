package com.daan.spelunky3D.entitygen;

import com.daan.spelunky3D.entitygen.enums.SpawnableMonstersEnum;
import com.daan.spelunky3D.entitygen.models.MonsterSpawnPoint;
import com.daan.spelunky3D.entitygen.utils.ClipboardToWorldMapper;
import com.daan.spelunky3D.entitygen.utils.MonsterMarkerExtractor;
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

        List<MonsterSpawnPoint> found = MonsterMarkerExtractor.extract(clipboard);
        spawnPoints.addAll(found);

        return new ArrayList<>(spawnPoints);
    }


    public void spawnMonsters(World world, Vector3Int chunkLocation) {
        for (MonsterSpawnPoint point : spawnPoints) {

            if (RandomGen.value() > point.type.getSpawnChance())
                continue;

            Vector3Int monsterInChunkLocation = new Vector3Int(
                    point.position.x(),
                    point.position.y(),
                    point.position.z()
            );

            Location loc = ClipboardToWorldMapper.toWorldLocation(
                    world,
                    chunkLocation,
                    monsterInChunkLocation
            );

            world.spawnEntity(loc, switch (point.type) {
                case SKELETON -> EntityType.SKELETON;
                case ZOMBIE -> EntityType.ZOMBIE;
                case SPIDER -> EntityType.SPIDER;
                case CREEPER -> EntityType.CREEPER;
            });
        }
    }

}