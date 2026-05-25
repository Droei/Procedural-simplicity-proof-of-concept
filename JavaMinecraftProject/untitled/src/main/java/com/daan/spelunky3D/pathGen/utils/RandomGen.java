package com.daan.spelunky3D.pathgen.utils;

import java.util.Random;

public class RandomGen {

    private static Random rng = new Random();

    public static void setSeed(int seed) {

        rng = new Random(seed);
    }

    public static int range(int min, int max) {

        if (max <= min) return min;

        return rng.nextInt(max - min) + min;
    }

    public static float value() {

        return (float) rng.nextDouble();
    }

    public static boolean nextBoolean() {

        return rng.nextBoolean();
    }
}