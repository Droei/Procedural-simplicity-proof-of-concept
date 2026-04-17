package com.daan.spelunky3D.entitygen;

import com.daan.spelunky3D.entitygen.utils.ClipboardToWorldMapper;
import com.daan.spelunky3D.entitygen.utils.StructureExtractions;
import com.daan.spelunky3D.pathgen.models.Vector3Int;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerSpawner {

    private final List<Vector3Int> crimsonPlankLocations = new ArrayList<>();

    public List<Vector3Int> getAndClearCrimsonPlanks(Clipboard clipboard) {

        crimsonPlankLocations.clear();

        List<BlockVector3> found = StructureExtractions.extractCrimsonPlanks(clipboard);

        for (BlockVector3 vec : found) {
            crimsonPlankLocations.add(new Vector3Int(
                    vec.x(),
                    vec.y(),
                    vec.z()
            ));
        }

        return new ArrayList<>(crimsonPlankLocations);
    }

    public void teleportPlayers(World world, Vector3Int chunkLocation) {

        if (crimsonPlankLocations.isEmpty()) return;

        Vector3Int relative = crimsonPlankLocations.get(
                (int) (Math.random() * crimsonPlankLocations.size())
        );

        Location loc = ClipboardToWorldMapper.toWorldLocation(
                world,
                chunkLocation,
                relative
        );

        for (Player player : world.getPlayers()) {
            player.teleport(loc);
        }
    }
}