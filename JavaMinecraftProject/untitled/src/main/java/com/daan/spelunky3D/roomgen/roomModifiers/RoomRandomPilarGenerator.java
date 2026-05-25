package com.daan.spelunky3D.roomgen.roomModifiers;

import com.daan.spelunky3D.pathgen.utils.RandomGen;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;

public class RoomRandomPilarGenerator {

    public void apply(BlockArrayClipboard clipboard) {

        for (BlockVector3 pos : clipboard.getRegion()) {

            String id = clipboard.getBlock(pos)
                    .getBlockType()
                    .id();

            if (!id.equals("minecraft:bricks")) {
                continue;
            }

            if (RandomGen.nextBoolean()) {

                clipboard.setBlock(
                        pos,
                        BlockTypes.STONE.getDefaultState()
                );

                generatePillar(
                        clipboard,
                        pos
                );

            } else {

                clipboard.setBlock(
                        pos,
                        BlockTypes.LAVA.getDefaultState()
                );
            }
        }
    }

    private void generatePillar(
            BlockArrayClipboard clipboard,
            BlockVector3 base
    ) {

        for (int y = 1; y <= 3; y++) {

            if (!RandomGen.nextBoolean()) {
                break;
            }

            BlockVector3 pillarPos = BlockVector3.at(
                    base.x(),
                    base.y() + y,
                    base.z()
            );

            clipboard.setBlock(
                    pillarPos,
                    BlockTypes.STONE.getDefaultState()
            );
        }
    }
}