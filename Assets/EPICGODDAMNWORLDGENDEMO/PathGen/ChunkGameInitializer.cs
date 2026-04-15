using System.Collections.Generic;
using System.Linq;
using UnityEngine;

public class ChunkGameInitializer
{
    private ChunkManager manager;
    private ChunkPathGenerator generator;
    private int gridSize;

    public ChunkGameInitializer(ChunkManager manager, ChunkPathGenerator generator, int gridSize)
    {
        this.manager = manager;
        this.generator = generator;
        this.gridSize = gridSize;
    }

    public Chunk SetRandomStart()
    {

        Vector3Int startLocation = new(RandomGen.Range(0, gridSize), RandomGen.Range(0, gridSize), gridSize - 1);

        manager.SetChunkTypeByLocation(startLocation, ChunkTypeEnum.Start);

        List<DirectionEnum> startDirs = generator.FindAvailableOpenings(startLocation);
        List<DirectionEnum> startChosen = generator.PickRandomDirections(startDirs, 2, 2);

        return manager.GetChunkByLocation(startLocation).SetOpeningDirections(startChosen);
    }

    public List<Chunk> GenerateChunksInOpenDirections(Chunk chunk)
        => GenerateChunksInOpenDirections(new List<Chunk>() { chunk });


    public List<Chunk> GenerateChunksInOpenDirections(List<Chunk> chunks)
    {
        return CreateAttachedChunks(chunks);
    }


    public Chunk GenerateChunksInOpenDirectionsWithDownHole(List<Chunk> chunks)
    {
        List<Chunk> latestChunks = CreateAttachedChunks(chunks);
        Chunk downChunk = latestChunks[RandomGen.Range(0, latestChunks.Count)];
        Chunk upChunk = manager.SetDownHole(downChunk);
        upChunk.SetOpeningDirections(generator.PickRandomDirections(generator.FindAvailableOpenings(upChunk.location), 2, 4));
        return upChunk;
    }

    public Chunk GenerateChunksInOpenDirectionsWithEnding(List<Chunk> chunks)
    {
        List<Chunk> latestChunks = CreateAttachedChunks(chunks);
        return manager.SetEndingChunk(latestChunks[RandomGen.Range(0, latestChunks.Count)]);
    }

    private List<Chunk> CreateAttachedChunks(List<Chunk> chunks)
    {
        List<Chunk> newChunks = new();

        foreach (var chunk in chunks)
        {
            //Debug.Log("Chunk " + chunk.location + "has: " + manager.GetNeighborChunksThroughLocation(chunk.location).Length);

            foreach (var direction in chunk.GetOpeningDirections)
            {
                Vector3Int offset = ChunkHelperFunctions.DirectionToVector(direction);
                Vector3Int target = chunk.location + offset;

                Chunk newChunk = manager.GetChunkByLocation(target);
                newChunk.SetChunkType(ChunkTypeEnum.Normal);

                var openings = generator.PickRandomDirections(generator.FindAvailableOpenings(target), 1, 3);
                var incoming = manager.GetIncomingConnections(newChunk.location);

                openings.AddRange(incoming.Where(dir => !openings.Contains(dir)));
                newChunk.SetOpeningDirections(openings);

                newChunks.Add(newChunk);
            }
        }
        return newChunks;
    }
}