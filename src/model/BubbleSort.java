package model;

import java.util.List;

public final class BubbleSort extends SortAlgorithmBase {

    @Override
    public List<Integer> sort(final List<Integer> theList,
                              final SortUpdateListener listener,
                              final int delayMs) {

        comparisonCount = 0;
        final long start = System.nanoTime();

        final int n = theList.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                comparisonCount++;

                if (theList.get(j) > theList.get(j + 1)) {
                    final int temp = theList.get(j);
                    theList.set(j, theList.get(j + 1));
                    theList.set(j + 1, temp);
                    swapped = true;
                }

                if (listener != null) {
                    listener.onUpdate(theList, j, j + 1, comparisonCount);
                    sleep(delayMs);
                }
            }
            if (!swapped) {
                break; // already sorted
            }
        }

        timeDuration = System.nanoTime() - start;

        if (listener != null) {
            listener.onUpdate(theList, -1, -1, comparisonCount);
        }

        return theList;
    }

    //TODO: what is this?
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
