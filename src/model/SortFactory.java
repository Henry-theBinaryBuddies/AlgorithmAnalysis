package model;

/**
 * Simple factory that creates concrete SortAlgorithmFactory instances.
 */
public final class SortFactory {

    private SortFactory() {
        // utility class, no instances
    }

    public static AbstractAlgorithmBase createSorter(final SortType type) {
        return switch (type) {
            case BUBBLE -> new BubbleAbstract();
            case SHELL  -> new ShellAbstract();
            case MERGE  -> new MergeAbstract();
        };
    }
}