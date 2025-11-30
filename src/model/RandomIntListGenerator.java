package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class RandomIntListGenerator {


    private final Random myRandom;
    private final int myBound; // exclusive upper bound (e.g., 99 for 0..98)


    /**
     * Constructs a generator with the given bound and a new Random.
     *
     * @param theBound exclusive upper bound for generated ints (must be > 0)
     */
    public RandomIntListGenerator(final int theBound) {
        if (theBound <= 0) {
            throw new IllegalArgumentException("Bound must be positive.");
        }
        myBound = theBound;
        myRandom = new Random();
    }

    /**
     * Generates a new list of random integers of the given size.
     *
     * @param theSize number of elements
     * @return a new List<Integer> with random values in [0, bound)
     */
    public List<Integer> generate(final int theSize) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < theSize; i++) {
            list.add(myRandom.nextInt(myBound)+1);
        }
        System.out.println("Unsorted List: " +list);
        return list;
    }
}
