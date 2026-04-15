package com.daan.spelunky3D.pathgen.utils;

import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.models.Vector3Int;

public class DirectionEntry {

    public final Vector3Int vector;
    public final DirectionEnum direction;

    public DirectionEntry(Vector3Int vector, DirectionEnum direction) {
        this.vector = vector;
        this.direction = direction;
    }
}