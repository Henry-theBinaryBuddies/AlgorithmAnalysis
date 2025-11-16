package model;

import java.util.List;

public final class ShellSort extends SortAlgorithmBase {

    @Override
    public List<Integer> sort(final List<Integer> theList,
                              final SortUpdateListener listener,
                              final int delayMs) {

        comparisonCount = 0;
        final long start = System.nanoTime();

        if (theList == null || theList.size() < 2) {
            timeDuration = System.nanoTime() - start;
            if (listener != null) {
                listener.onUpdate(theList, -1, -1, comparisonCount);
            }
            return theList;
        }

        final int n = theList.size();

        // Standard Shell Sort with gap sequence n/2, n/4, ..., 1
        for (int gap = n / 2; gap > 0; gap /= 2) {

            // Gapped insertion sort for this gap size
            for (int i = gap; i < n; i++) {
                final int temp = theList.get(i);
                int j = i;

                // Shift earlier gap-sorted elements up until correct spot for temp
                while (j >= gap) {
                    comparisonCount++;
                    if (theList.get(j - gap) > temp) {
                        theList.set(j, theList.get(j - gap));
                        j -= gap;

                        if (listener != null) {
                            // highlight the two positions involved in the shift
                            listener.onUpdate(theList, j, j + gap, comparisonCount);
                            sleep(delayMs);
                        }
                    } else {
                        break;
                    }
                }

                // Put temp in its correct location
                theList.set(j, temp);
                if (listener != null) {
                    // highlight insertion position vs original index
                    listener.onUpdate(theList, j, i, comparisonCount);
                    sleep(delayMs);
                }
            }
        }

        timeDuration = System.nanoTime() - start;

        if (listener != null) {
            listener.onUpdate(theList, -1, -1, comparisonCount);
        }

        return theList;
    }

    private void sleep(final int delayMs) {
        if (delayMs <= 0) {
            return;
        }
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
