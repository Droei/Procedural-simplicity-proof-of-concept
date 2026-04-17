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

public class ClipboardExtractor {

    private ClipboardExtractor() {}
    @FunctionalInterface
    public interface MarkerMapper<T> {
        T map(BlockState state, BlockVector3 relative);
    }

    public static <T> List<T> extract(
            Clipboard clipboard,
            MarkerMapper<T> mapper
    ) {
        List<T> results = new ArrayList<>();

        BlockVector3 min = clipboard.getMinimumPoint();
        BlockVector3 max = clipboard.getMaximumPoint();

        for (int x = min.x(); x <= max.x(); x++) {
            for (int y = min.y(); y <= max.y(); y++) {
                for (int z = min.z(); z <= max.z(); z++) {

                    BlockVector3 pos = BlockVector3.at(x, y, z);
                    BlockState state = clipboard.getBlock(pos);
                    BlockVector3 relative = pos.subtract(min);

                    T result = mapper.map(state, relative);
                    if (result == null) continue;

                    results.add(result);
                    removeBlock(clipboard, pos);
                }
            }
        }

        return results;
    }

    private static void removeBlock(Clipboard clipboard, BlockVector3 pos) {
        try {
            clipboard.setBlock(pos, AIR.getDefaultState());
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
    }
}