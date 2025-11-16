package model;

import java.util.List;

/**
 * Listener used by sorting algorithms to report progress for visualization.
 */
@FunctionalInterface
public interface SortUpdateListener {

    /**
     * Called whenever the algorithm wants to update the GUI.
     *
     * @param currentState      current list state (mutated list)
     * @param indexA            primary index being operated on, or -1
     * @param indexB            secondary index being operated on, or -1
     * @param comparisonsSoFar  number of comparisons performed so far
     */
    void onUpdate(List<Integer> currentState, int indexA, int indexB, long comparisonsSoFar);
}
