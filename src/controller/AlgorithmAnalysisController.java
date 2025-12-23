package controller;
import model.*;
import view.AlgorithmAnalysisGUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AlgorithmAnalysisController {
    private final AlgorithmAnalysisModel model;
    private final AlgorithmAnalysisGUI view;

    private int delayMS;   // animation speed

    /** Snapshot of current list state to draw (always touched on EDT) */
    private List<Integer> currentSnapshot;

    /** A saved baseline dataset, kept unsorted */
    private List<Integer> baselineDataset;
    private boolean baselineFrozen;

    private boolean isSorting;
    private Comparator<Integer> comparator;

    /** Constructor */

    public AlgorithmAnalysisController(final AlgorithmAnalysisModel theModel, final AlgorithmAnalysisGUI theView) {
        model = theModel;
        view = theView;

        delayMS = view.getSelectedSpeed();
        view.setDelayLabel(delayMS);
        currentSnapshot = null;
        baselineDataset = null;
        baselineFrozen = false;

        isSorting = false;
        comparator = Comparator.naturalOrder();
    }

    /**
     * Generate a new random list .
     */
    public void handleRandomizeRequested() {
        //If currently sorting, do nothing.
        if(isSorting) {
            return;
        }
        int size = view.getSelectedSize();
        RandomIntListGenerator generator = new RandomIntListGenerator(size);
        currentSnapshot = generator.generate(size);

        view.setSnapshot(currentSnapshot);
        view.resetStatsAfterRandomize(size);
    }

    //TODO: This might want to be merged with the above.
    public void handleSizeChanged() {

    }

    /** TODO: This method has way too much going on. Refactor.
     * Start the selected sort on a background thread and animate bars.
     */
    public void handleSortRequested() {
        SortType type = view.getSelectedAlgorithm();

        //If already sorting, do nothing.
        if(isSorting || type == null) {
            return;
        }

        final AbstractAlgorithmBase sorter = SortFactory.getAlgorithm(type);

        final boolean useBaseLine =
                baselineFrozen && baselineDataset != null
                && baselineDataset.size() == currentSnapshot.size();

        final List<Integer> source = useBaseLine
                ? baselineDataset
                : currentSnapshot;

        //Copy so we never mutate the stored baseline
        final List<Integer> working = new ArrayList<>(source);
        isSorting = true;
        view.setControlsEnabled(false);
        view.setStatus("Sorting with " +sorter.name() + ". . .");

        long start = System.nanoTime();

        Thread worker = new Thread(() -> {
            SortUpdateListener listener = (list, indexA, indexB, comparisons) -> {
                //Make a copy for the GUI
                final List<Integer> snapshotCopy = new ArrayList<>(list);
                final long elapsedNs = System.nanoTime() - start;
                final long elapsedMs = elapsedNs / 1000000;

                SwingUtilities.invokeLater(() -> {
                    currentSnapshot = snapshotCopy;
                    view.setSnapshot(snapshotCopy);
                    view.setHighlights(indexA, indexB);
                    view.setComparisons(comparisons);
                    view.setTimeLabel(elapsedMs);
                });
            };

            //Run the visual sort
            sorter.sort(working, listener, delayMS, comparator);

            final long finalTimeMs = sorter.getTimeDuration() / 1_000_000;
            final long finalComps = sorter.getComparisonCount();

            SwingUtilities.invokeLater(() -> {
                isSorting = false;
                view.setControlsEnabled(true);
                view.setHighlights(-1, -1);
                view.setTimeLabel(finalTimeMs);
                view.setComparisons(finalComps);
                view.setStatus("Done: " + sorter.name());
            });
        });

        worker.setDaemon(true);
        worker.start();

    }

    /**
     * Saves a dataset to a bank to be restored across different algorithms.
     */
    public void handleSaveBaseline() {
        if (isSorting || currentSnapshot == null || currentSnapshot.isEmpty()) {
            return;
        }

        //get the current data from the view
        List<Integer> snapshot = view.getCurrentSnapshot();
        if (snapshot == null || snapshot.isEmpty()) {
            return;
        }

        //save to the controller's baseline state
        baselineDataset = new ArrayList<>(snapshot);
        baselineFrozen = true;

        //let the view know a baseline is available and update the status
        view.setRestoreBaselineEnabled(true);
        view.showBaselineSavedStatus(baselineDataset.size());
    }

    /**
     * Restores the saved baseline into the current snapshot for visualization.
     */
    public void handleRestoreBaseLine() {
        //Guard conditions: must have a baseline, and not be sorting
        if (isSorting || !baselineFrozen || baselineDataset == null || baselineDataset.isEmpty()) {
            return;
        }

        //copy baseline into current snapshot
        currentSnapshot = new ArrayList<>(baselineDataset);

        //push into the view and reset stats
        view.setSnapshot(currentSnapshot);
        view.resetStatsAfterRandomize(currentSnapshot.size());
        view.showBaselineRestoredStatus(currentSnapshot.size());
    }

    public void handleCompareModeToggled(final boolean theDescending) {
        if (theDescending) {
            comparator = Comparator.reverseOrder();
        }
        else {
            comparator = Comparator.naturalOrder();
        }

        view.setCompareToggleLabel(theDescending);
    }

    public void handleSpeedChanged(int theNewDelayMs) {
        delayMS = theNewDelayMs;
        view.setDelayLabel(delayMS);
    }

}
