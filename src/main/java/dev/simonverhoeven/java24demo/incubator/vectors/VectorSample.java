package dev.simonverhoeven.java24demo.incubator.vectors;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorSpecies;

import java.util.Arrays;

// JEP 489 Vector API - Ninth incubation

/*
    Samples: https://github.com/openjdk/jdk/tree/master/test/micro/org/openjdk/bench/jdk/incubator/vector
 */


public class VectorSample {
    public static void main() {
        int[] result = new int[5];
        final var vector1 = new int[]{1, 2, 3, 4, 5};
        final var vector2 = new int[]{100, 200, 300, 400, 500};

        scalarAddition(vector1, vector2, result);
        System.out.println(Arrays.toString(result));
        vectorAddition(vector1, vector2, result);
        System.out.println(Arrays.toString(result));
        vectorAdditionWithoutSIMDSupport(vector1, vector2, result);
        System.out.println(Arrays.toString(result));
    }

    public static void scalarAddition(int[] a, int[] b, int[] result) {
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + b[i];
        }
    }

    public static void vectorAddition(int[] vector1, int[] vector2, int[] result) {
        final VectorSpecies<Integer> species = IntVector.SPECIES_PREFERRED;
        for (int index = 0; index < vector1.length; index += species.length()) {
            VectorMask<Integer> mask = species.indexInRange(index, vector1.length);
            IntVector v1 = IntVector.fromArray(species, vector1, index, mask);
            IntVector v2 = IntVector.fromArray(species, vector2, index, mask);
            IntVector vc = v1.add(v2, mask);
            vc.intoArray(result, index, mask);
        }
    }

    public static void vectorAdditionWithoutSIMDSupport(int[] vector1, int[] vector2, int[] result) {
        final VectorSpecies<Integer> species = IntVector.SPECIES_PREFERRED;
        int index = 0;
        for (;index < species.loopBound(vector1.length); index += species.length()) {
            IntVector v1 = IntVector.fromArray(species, vector1, index);
            IntVector v2 = IntVector.fromArray(species, vector2, index);
            IntVector vc = v1.add(v2);
            vc.intoArray(result, index);
        }

        // Handle remaining elements
        for (int index2 = index; index2 < vector1.length; index2++) {
            result[index2] = vector1[index2] + vector2[index2];
        }
    }
}
