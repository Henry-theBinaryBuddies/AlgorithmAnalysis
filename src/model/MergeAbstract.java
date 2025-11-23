package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MergeAbstract extends AbstractAlgorithmBase {

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

        mergeSort(theList, 0, theList.size() - 1, theListener, theVisualDelay, theComparator);

        timeDuration = System.nanoTime() - start;

        if (theListener != null) {
            theListener.onUpdate(theList, -1, -1, comparisonCount);
        }

        return theList;
    }

    private void mergeSort(final List<Integer> list,
                           final int left,
                           final int right,
                           final SortUpdateListener theListener,
                           final int delayMs, final Comparator<Integer> theComparator) {

        if (left >= right) {
            return;
        }

        final int mid = left + (right - left) / 2;

        mergeSort(list, left, mid, theListener, delayMs, theComparator);
        mergeSort(list, mid + 1, right, theListener, delayMs, theComparator);
        merge(list, left, mid, right, theListener, delayMs, theComparator);
    }

    private void merge(final List<Integer> list,
                       final int left,
                       final int mid,
                       final int right,
                       final SortUpdateListener theListener,
                       final int delayMs, final Comparator<Integer> theComparator) {

        final List<Integer> temp = new ArrayList<>(right - left + 1);

        int i = left;
        int j = mid + 1;

        // Merge into temp using comparator
        while (i <= mid && j <= right) {
            comparisonCount++;

            final int leftVal = list.get(i);
            final int rightVal = list.get(j);

            // If leftVal should come before rightVal (or tie), take left
            if (theComparator.compare(leftVal, rightVal) <= 0) {
                temp.add(leftVal);
                i++;
            } else {
                temp.add(rightVal);
                j++;
            }
        }

        while (i <= mid) {
            temp.add(list.get(i));
            i++;
        }

        while (j <= right) {
            temp.add(list.get(j));
            j++;
        }

        // Copy back to original list and notify GUI
        for (int k = 0; k < temp.size(); k++) {
            final int targetIndex = left + k;
            list.set(targetIndex, temp.get(k));

            if (theListener != null) {
                theListener.onUpdate(list, targetIndex, -1, comparisonCount);
                sleep(delayMs);
            }
        }
    }

}
