using System.Collections.Generic;
using System.Linq;
using UnityEngine;

public class ChunkGameInitializer
{
    private ChunkManager manager;
    private ChunkOpeningGenerator generator;
    private int gridSize;

    public ChunkGameInitializer(ChunkManager manager, ChunkOpeningGenerator generator, int gridSize)
    {
        this.manager = manager;
        this.generator = generator;
        this.gridSize = gridSize;
    }

    public Chunk SetRandomStart()
    {
        Vector3Int startLocation = new(RandomGen.Range(0, gridSize), RandomGen.Range(0, gridSize), gridSize - 1);
        Chunk startChunk = manager.SetChunkTypeByLocation(startLocation, ChunkTypeEnum.Start);

        SetupStartChunk(startChunk, 2, 2);

        return startChunk;
    }

    public Chunk SetupStartChunk(Chunk chunk, int min, int max)
    {
        SetupOpenings(chunk, min, max, false);

        Debug.Log(chunk.DetermineChunkDesign().ToString());

        return chunk;
    }

    public Chunk GenerateFloor(Chunk startChunk, int cycles)
    {
        List<Chunk> current = new() { startChunk };

        for (int i = 0; i < cycles; i++)
        {
            bool isLastCycle = i == cycles - 1;
            current = CreateAttachedChunks(current, isLastCycle);
        }

        Chunk chosen = current[RandomGen.Range(0, current.Count)];

        Chunk endChunk = null;

        if (chosen.location.z != 0)
            endChunk = manager.SetDownHole(chosen);
        else
            endChunk = manager.SetEndingChunk(chosen);


        SetupOpenings(endChunk, 1, 3, false);
        Debug.Log(endChunk.DetermineChunkDesign().ToString());
        FinalizeAllChunks();
        return endChunk;
    }

    private List<Chunk> CreateAttachedChunks(List<Chunk> chunks, bool isFinalCycle)
    {
        List<Chunk> newChunks = new();

        foreach (var chunk in chunks)
        {
            foreach (var direction in chunk.GetOpeningDirections)
            {
                Vector3Int target = chunk.location + ChunkHelperFunctions.DirectionToVector(direction);

                Chunk newChunk = manager.GetChunkByLocation(target);

                if (newChunk.GetChunkType != ChunkTypeEnum.Nothing)
                    continue;

                newChunk.SetChunkType(ChunkTypeEnum.Normal);

                SetupOpenings(newChunk, 1, 2, isFinalCycle);

                Debug.Log(newChunk.DetermineChunkDesign().ToString());

                newChunks.Add(newChunk);
            }
        }

        return newChunks;
    }

    private void SetupOpenings(Chunk chunk, int min, int max, bool isFinalCycle)
    {
        List<DirectionEnum> incoming = new();
        List<DirectionEnum> blocked = new();

        manager.EvaluateNeighbors(chunk.location, incoming, blocked);

        if (isFinalCycle)
        {
            chunk.SetOpeningDirections(incoming);
            return;
        }

        List<DirectionEnum> directions = generator
            .PickRandomDirections(generator.FindAvailableOpenings(chunk.location), min, max);

        directions.AddRange(incoming.Where(dir => !directions.Contains(dir)));

        directions.RemoveAll(dir => blocked.Contains(dir));

        chunk.SetOpeningDirections(directions);
    }

    private void FinalizeAllChunks()
    {
        foreach (var chunk in manager.GetChunks)
        {
            if (chunk.GetChunkType == ChunkTypeEnum.Nothing)
                continue;

            List<DirectionEnum> incoming = new();
            List<DirectionEnum> blocked = new();

            manager.EvaluateNeighbors(chunk.location, incoming, blocked);

            chunk.SetOpeningDirections(incoming);
        }
    }
}