package model;

/**
 * Simple factory that creates concrete SortAlgorithmFactory instances.
 */
public final class SortFactory {

    private SortFactory() {
        // utility class, no instances
    }

    public static SortAlgorithmBase createSorter(final SortType type) {
        return switch (type) {
            case BUBBLE -> new BubbleSort();
            case SHELL  -> new ShellSort();
            case MERGE  -> new MergeSort();
        };
    }
}