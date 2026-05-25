package com.daan.spelunky3D.pathgen.models;

import com.daan.spelunky3D.pathgen.enums.ChunkDesignEnum;
import com.daan.spelunky3D.pathgen.enums.ChunkTypeEnum;
import com.daan.spelunky3D.pathgen.enums.DirectionEnum;
import com.daan.spelunky3D.pathgen.utils.ChunkHelperFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chunk {

    public int id;
    public Vector3Int location;

    private List<DirectionEnum> openingDirections = new ArrayList<>();

    public ChunkDesignEnum chunkDesign;
    private ChunkTypeEnum chunkType = ChunkTypeEnum.NOTHING;

    // New variable that doesn't change any core logic, it holds all opening directions that won't have a door
    private List<DirectionEnum> emptyOpeningDirections = new ArrayList<>();


    public Chunk(Vector3Int location) {
        this.id = ChunkHelperFunctions.parseLocationToId(location);
        this.location = location;
    }

    public Chunk setOpeningDirections(List<DirectionEnum> openingDirections) {
        this.openingDirections = openingDirections;
        return this;
    }
    public Chunk setEmptyOpeningDirections(List<DirectionEnum> emptyOpeningDirections) {
        this.emptyOpeningDirections = emptyOpeningDirections;
        return this;
    }

    public Chunk setChunkType(ChunkTypeEnum chunkType) {
        this.chunkType = chunkType;
        return this;
    }

    public ChunkTypeEnum getChunkType() {
        return chunkType;
    }

    public List<DirectionEnum> getEmptyOpeningDirections(){
        return emptyOpeningDirections;
    }

    public List<DirectionEnum> getOpeningDirections() {
        return openingDirections;
    }

    public void setChunkDesign(ChunkDesignEnum design) {
        this.chunkDesign = design;
    }

    public ChunkDesignEnum getChunkDesign() {
        return chunkDesign;
    }

    @Override
    public String toString() {

        String openings = (openingDirections != null && !openingDirections.isEmpty())
                ? openingDirections.stream().map(Enum::name).collect(Collectors.joining(", "))
                : "None";
        String emptyOpenings = (emptyOpeningDirections != null && !emptyOpeningDirections.isEmpty())
                ? emptyOpeningDirections.stream().map(Enum::name).collect(Collectors.joining(", "))
                : "None";

        return "Chunk [" +
                "ID: " + id + ", " +
                "Loc: (" + location.x + ", " + location.y + ", " + location.z + "), " +
                "Type: " + chunkType + ", " +
                "Design: " + chunkDesign + ", " +
                "Openings: [" + openings + "]" +
                "EmptyOpenings: [" + emptyOpenings + "]" +
                "]";
    }

    public Chunk determineChunkDesign() {

        List<DirectionEnum> openings = new ArrayList<>(openingDirections);

        if (!openingDirections.isEmpty()) {

            int count = openings.size();

            switch (count) {
                case 0 -> chunkDesign = ChunkDesignEnum.NONE;

                case 1 -> chunkDesign = ChunkDesignEnum.ONE_WAY;

                case 2 -> {
                    if (ChunkHelperFunctions.getOpposite(openings.get(0)) == openings.get(1)) {
                        chunkDesign = ChunkDesignEnum.STRAIGHT;
                    } else {
                        chunkDesign = ChunkDesignEnum.CORNER;
                    }
                }

                case 3 -> chunkDesign = ChunkDesignEnum.T_SHAPE;

                case 4 -> chunkDesign = ChunkDesignEnum.CROSS;

                default -> chunkDesign = ChunkDesignEnum.NONE;
            }

        } else {
            chunkDesign = ChunkDesignEnum.NONE;
        }

        return this;
    }
}