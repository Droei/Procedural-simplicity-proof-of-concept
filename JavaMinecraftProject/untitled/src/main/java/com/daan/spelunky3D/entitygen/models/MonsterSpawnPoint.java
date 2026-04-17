package com.daan.spelunky3D.entitygen.models;

import com.daan.spelunky3D.entitygen.enums.SpawnableMonstersEnum;
import com.sk89q.worldedit.math.BlockVector3;

public class MonsterSpawnPoint {

    public final SpawnableMonstersEnum type;
    public final BlockVector3 position;

    public MonsterSpawnPoint(SpawnableMonstersEnum type, BlockVector3 position) {
        this.type = type;
        this.position = position;
    }
}