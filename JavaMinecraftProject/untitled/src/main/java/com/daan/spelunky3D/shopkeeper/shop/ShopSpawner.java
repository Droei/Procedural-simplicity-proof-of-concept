package com.daan.spelunky3D.shopkeeper.shop;

import com.daan.spelunky3D.entitygen.utils.ClipboardToWorldMapper;
import com.daan.spelunky3D.entitygen.utils.StructureExtractions;
import com.daan.spelunky3D.pathgen.models.Vector3Int;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class ShopSpawner {

    private final List<Vector3Int> emeraldLocations =
            new ArrayList<>();

    private final ShopManager shopManager;

    public ShopSpawner(ShopManager shopManager) {

        this.shopManager = shopManager;
    }

    public void getAndClearEmeraldBlocks(
            Clipboard clipboard
    ) {

        emeraldLocations.clear();

        List<BlockVector3> found =
                StructureExtractions.extractEmeraldBlocks(
                        clipboard
                );

        for (BlockVector3 vec : found) {

            // Store relative position
            emeraldLocations.add(
                    new Vector3Int(
                            vec.x(),
                            vec.y(),
                            vec.z()
                    )
            );

            try {
                clipboard.setBlock(
                        vec,
                        BlockTypes.AIR.getDefaultState()
                );
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void spawnShops(World world,
                           Vector3Int chunkLocation) {

        for (Vector3Int relative : emeraldLocations) {

            Location location =
                    ClipboardToWorldMapper.toWorldLocation(
                            world,
                            chunkLocation,
                            relative
                    );

            shopManager.spawnShopVillager(
                    location.clone().add(0.5, 0, 0.5)
            );
        }
    }
}