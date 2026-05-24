package com.daan.spelunky3D.entitygen.utils;

import com.daan.spelunky3D.entitygen.enums.SpawnableMonstersEnum;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;

public class MarkerMappers {

    private MarkerMappers() {}

    public static SpawnableMonstersEnum mapToMonster(
            BlockState state
    ) {

        return switch (state.getBlockType().id()) {

            case "minecraft:skeleton_skull" -> SpawnableMonstersEnum.SKELETON;

            case "minecraft:zombie_head" -> SpawnableMonstersEnum.ZOMBIE;

            case "minecraft:dragon_head" -> SpawnableMonstersEnum.SPIDER;

            case "minecraft:creeper_head" -> SpawnableMonstersEnum.CREEPER;

            default -> null;
        };
    }

    public static BlockVector3 mapToCrimsonPlank(
            BlockState state,
            BlockVector3 relative
    ) {

        if (!state.getBlockType().id()
                .equals("minecraft:crimson_planks")) {

            return null;
        }

        return relative;
    }

    public static BlockVector3 mapToEmeraldBlock(
            BlockState state,
            BlockVector3 relative
    ) {

        if (!state.getBlockType().id()
                .equals("minecraft:emerald_block")) {

            return null;
        }

        return relative;
    }
}