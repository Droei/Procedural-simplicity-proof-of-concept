package com.daan.spelunky3D.entitygen.enums;

public enum SpawnableMonstersEnum {

    SKELETON(0.3f),
    ZOMBIE(0.4f),
    SPIDER(0.2f),
    CREEPER(0.1f);

    private final float spawnChance;

    SpawnableMonstersEnum(float spawnChance) {
        this.spawnChance = spawnChance;
    }

    public float getSpawnChance() {
        return spawnChance;
    }
}