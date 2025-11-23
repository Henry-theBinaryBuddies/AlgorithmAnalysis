package model;

import java.util.Comparator;
import java.util.List;

public final class BubbleAbstract extends AbstractAlgorithmBase {

    @Override
    public List<Integer> sort(final List<Integer> theList,
                              final SortUpdateListener theListener,
                              final int theVisualDelay, final Comparator<Integer> theComparator) {

        final int n = theList.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                comparisonCount++;

                if (theComparator.compare(theList.get(j), theList.get(j + 1)) > 0) {
                    final int temp = theList.get(j);
                    theList.set(j, theList.get(j + 1));
                    theList.set(j + 1, temp);
                    swapped = true;
                }

                if (theListener != null) {
                    theListener.onUpdate(theList, j, j + 1, comparisonCount);
                    super.sleep(theVisualDelay);
                }
            }
            if (!swapped) {
                break; // already sorted
            }
        }

        timeDuration = System.nanoTime() - start;

        if (theListener != null) {
            theListener.onUpdate(theList, -1, -1, comparisonCount);
        }
        return theList;
    }

}
