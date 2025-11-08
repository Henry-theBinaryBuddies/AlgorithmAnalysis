import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class AlgorithmAnalysis {

    static Random random = new Random();

    /** Generate array list with 10 elements of random integers.*/
     private static List<Integer> listSize10() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(random.nextInt());
        }
        System.out.println(list);
        return list;
    }

    /** Generate array list with 100 elements of random integers.*/

    /**
     * Generate array list with 1000 elements of random integers.
     *
     * @return
     */


    private static List<Integer> bubbleSort(List<Integer> theList) {
        int n = theList.size();
        boolean swapped; // Flag to optimize: if no swaps in a pass, array is sorted

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                // Compare adjacent elements
                if (theList.get(j) > theList.get(j + 1)) {
                    // Swap them if they are in the wrong order
                    final int temp = theList.get(j);
                    theList.set(j, theList.get(j+1));
                    theList.set(j+1, temp);
                }
            }
        }
        System.out.println(theList.toString());
        return theList;
    }

    public static void main(String[] args) {
        List<Integer> listSize10 = listSize10();
        List<Integer> sortedListSize10BS = bubbleSort(listSize10);

    }
}
