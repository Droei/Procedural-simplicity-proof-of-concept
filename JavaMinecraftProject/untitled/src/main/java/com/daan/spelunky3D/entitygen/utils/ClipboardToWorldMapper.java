package com.daan.spelunky3D.entitygen.utils;

import com.daan.spelunky3D.pathgen.models.Vector3Int;
import org.bukkit.Location;
import org.bukkit.World;

public class ClipboardToWorldMapper {

    public static Location toWorldLocation(
            World world,
            Vector3Int origin,
            Vector3Int inChunkLocation
    ) {
        int x = origin.x - inChunkLocation.x;
        int y = origin.y + inChunkLocation.y;
        int z = origin.z - inChunkLocation.z;

        return new Location(world, x + 0.5, y, z + 0.5);
    }
}