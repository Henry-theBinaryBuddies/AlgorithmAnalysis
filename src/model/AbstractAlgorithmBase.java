package model;

import java.util.Comparator;
import java.util.List;

/**
 * Abstract base class for sorting algorithms that track time and comparisons.
 *
 * Concrete algorithms (e.g., BubbleSort, ShellSort) should extend this class
 * and implement the sort method.
 */

public abstract class AbstractAlgorithmBase {

    /** Time taken for the last sort run (in nanoseconds). */
    protected long timeDuration;
    /** Comparison count for the last sort run. */
    protected long comparisonCount;
    /** Start time for the timer, should always start at 0, hence being final. */
    final long start = System.nanoTime();

    /** Protected constructor to prevent instantiation. */
    protected AbstractAlgorithmBase() {
        // no instances
    }

    /**
     * Non-visual sort entry point (e.g., for Controller.AlgorithmAnalysisDriver).
     * This just delegates to the visual version with no listener and no delay.
     *
     * @param theList list to sort in-place
     * @return the same list, sorted
     */
    public final List<Integer> sort(final List<Integer> theList) {
        return sort(theList, null, 0, Comparator.naturalOrder());
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
     * @param listener  optional listener for visualization (maybe null)
     * @param delayMs   optional delay per update (for animation speed)
     * @return the same list, sorted
     */
    public abstract List<Integer> sort(final List<Integer> theList,
                                       final SortUpdateListener listener,
                                       final int delayMs, final Comparator<Integer> theComparator);

    /**
     * @return human-readable name, default is the class's simple name.
     */
    public String name(){
        return getClass().getSimpleName();
    };

    /**
     * @return time duration, default is 0.
     */
    public long getTimeDuration() {
        return timeDuration;
    }

    /**
     * @return comparison count, default is 0.
     */
    public long getComparisonCount() {
        return comparisonCount;
    }

    /**
     * Pauses the current sorting thread for a short time to slow down visualization.
     * <p>
     * This is used by the GUI-enabled sorts so that bar updates are visible in real time.
     * If {@code delayMs} is zero or negative, no pause is performed.
     * <p>
     * If the thread is interrupted while sleeping, the interrupt status is restored
     * via {@link Thread#currentThread()} and {@link Thread#interrupt()}, allowing
     * higher-level code to detect the interruption.
     *
     * @param delayMs the delay in milliseconds between visual updates
     */
    protected void sleep(final int delayMs) {
        if (delayMs <= 0) {
            return;
        }
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
