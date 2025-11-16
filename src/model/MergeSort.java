package model;

import java.util.ArrayList;
import java.util.List;

public final class MergeSort extends SortAlgorithmBase {

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

        mergeSort(theList, 0, theList.size() - 1, listener, delayMs);

        timeDuration = System.nanoTime() - start;

        if (listener != null) {
            listener.onUpdate(theList, -1, -1, comparisonCount);
        }

        return theList;
    }

    private void mergeSort(final List<Integer> list,
                           final int left,
                           final int right,
                           final SortUpdateListener listener,
                           final int delayMs) {

        if (left >= right) {
            return;
        }

        final int mid = left + (right - left) / 2;

        mergeSort(list, left,      mid,     listener, delayMs);
        mergeSort(list, mid + 1,   right,   listener, delayMs);
        merge(list, left, mid, right, listener, delayMs);
    }

    private void merge(final List<Integer> list,
                       final int left,
                       final int mid,
                       final int right,
                       final SortUpdateListener listener,
                       final int delayMs) {

        final List<Integer> temp = new ArrayList<>(right - left + 1);

        int i = left;
        int j = mid + 1;

        // Merge into temp
        while (i <= mid && j <= right) {
            comparisonCount++;
            if (list.get(i) <= list.get(j)) {
                temp.add(list.get(i));
                i++;
            } else {
                temp.add(list.get(j));
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

            if (listener != null) {
                listener.onUpdate(list, targetIndex, -1, comparisonCount);
                sleep(delayMs);
            }
        }
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
