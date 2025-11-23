package model;

import java.util.Comparator;
import java.util.List;

public final class ShellAbstract extends AbstractAlgorithmBase {

    @Override
    public List<Integer> sort(final List<Integer> theList,
                              final SortUpdateListener theListener,
                              final int theVisualDelay, final Comparator<Integer> theComparator) {

        if (theList == null || theList.size() < 2) {
            timeDuration = System.nanoTime() - start;
            if (theListener != null) {
                theListener.onUpdate(theList, -1, -1, comparisonCount);
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
                    if (theComparator.compare(theList.get(j - gap), temp) > 0) {
                        theList.set(j, theList.get(j - gap));
                        j -= gap;

                        if (theListener != null) {
                            // highlight the two positions involved in the shift
                            theListener.onUpdate(theList, j, j + gap, comparisonCount);
                            sleep(theVisualDelay);
                        }
                    } else {
                        break;
                    }
                }

                // Put temp in its correct location
                theList.set(j, temp);
                if (theListener != null) {
                    // highlight insertion position vs original index
                    theListener.onUpdate(theList, j, i, comparisonCount);
                    sleep(theVisualDelay);
                }
            }
        }

        timeDuration = System.nanoTime() - start;

        if (theListener != null) {
            theListener.onUpdate(theList, -1, -1, comparisonCount);
        }

        return theList;
    }

}
