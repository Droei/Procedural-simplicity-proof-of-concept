using System;

public static class RandomGen
{
    private static Random rng = new Random();

    public static void SetSeed(int seed)
    {
        rng = new Random(seed);
    }

    public static int Range(int min, int max)
    {
        return rng.Next(min, max);
    }

    public static float Value()
    {
        return (float)rng.NextDouble();
    }
}