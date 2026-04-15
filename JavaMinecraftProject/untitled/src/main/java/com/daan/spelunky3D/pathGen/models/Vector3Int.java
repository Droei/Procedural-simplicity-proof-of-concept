package com.daan.spelunky3D.pathgen.models;

public class Vector3Int {

    public int x;
    public int y;
    public int z;

    public Vector3Int(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3Int add(int dx, int dy, int dz) {
        return new Vector3Int(x + dx, y + dy, z + dz);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}