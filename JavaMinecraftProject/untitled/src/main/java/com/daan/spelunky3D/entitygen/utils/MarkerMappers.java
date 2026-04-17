package com.daan.spelunky3D.entitygen.utils;

import com.daan.spelunky3D.entitygen.enums.SpawnableMonstersEnum;
import com.sk89q.worldedit.world.block.BlockState;

public class MarkerMappers {

    private MarkerMappers() {}

    public static SpawnableMonstersEnum mapToMonster(BlockState state) {
        return switch (state.getBlockType().id()) {
            case "minecraft:skeleton_skull" -> SpawnableMonstersEnum.SKELETON;
            case "minecraft:zombie_head" -> SpawnableMonstersEnum.ZOMBIE;
            case "minecraft:dragon_head" -> SpawnableMonstersEnum.SPIDER;
            case "minecraft:creeper_head" -> SpawnableMonstersEnum.CREEPER;
            default -> null;
        };
    }
}