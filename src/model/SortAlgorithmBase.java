package model;

import java.util.List;

/**
 * Abstract base class for sorting algorithms that track time and comparisons.
 *
 * Concrete algorithms (e.g., BubbleSort, ShellSort) should extend this class
 * and implement the sort method.
 */

public abstract class SortAlgorithmBase {

    /** Time taken for the last sort run (in nanoseconds). */
    protected long timeDuration;
    /** Comparison count for the last sort run. */
    protected long comparisonCount;

    /** Protected constructor to prevent instantiation. */
    protected SortAlgorithmBase() {
        // no instances
    }

    /**
     * Non-visual sort entry point (e.g., for AlgorithmAnalysisDriver).
     * This just delegates to the visual version with no listener and no delay.
     *
     * @param theList list to sort in-place
     * @return the same list, sorted
     */
    public final List<Integer> sort(final List<Integer> theList) {
        return sort(theList, null, 0);
    }

    /**
     * Visual sort entry point. Implementations should:
     *  - reset comparisonCount/timeDuration
     *  - record start time
     *  - do the sorting
     *  - call listener.onUpdate(...) as desired (if listener != null)
     *  - set timeDuration at the end
     *
     * @param theList   list to sort in-place
     * @param listener  optional listener for visualization (may be null)
     * @param delayMs   optional delay per update (for animation speed)
     * @return the same list, sorted
     */
    public abstract List<Integer> sort(List<Integer> theList,
                                       SortUpdateListener listener,
                                       int delayMs);

    /**
     * @return human-readable name, default is the class's simple name.
     */
    public String name(){
        return getClass().getSimpleName();
    };

    public long getTimeDuration() {
        return timeDuration;
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName())
                .append(" Time: ")
                .append(timeDuration)
                .append(" Comparisons: ")
                .append(comparisonCount);
        return sb.toString();
    }


}
