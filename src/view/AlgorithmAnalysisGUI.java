package model;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI to visualize your sorting algorithms using bars.
 */
public final class AlgorithmAnalysisGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final int DELAY_MS = 5;   // animation speed
    private static final int BOUND = 99;     // matches your random int boundary

    private final RandomIntListGenerator myGenerator =
            new RandomIntListGenerator(BOUND);

    // Snapshot of current list state to draw (always touched on EDT)
    private List<Integer> myCurrentSnapshot = new ArrayList<>();

    // A saved baseline dataset, kept unsorted
    private List<Integer> myBaselineDataset = null;
    private boolean myBaselineFrozen = false;

    // Indices currently being operated on
    private int myHighlightA = -1;
    private int myHighlightB = -1;

    private boolean myIsSorting = false;

    // GUI components
    private final JComboBox<SortType> myAlgorithmCombo;
    private final JComboBox<Integer> mySizeCombo;
    private final JButton myRandomizeButton;
    private final JButton mySortButton;
    private final JButton mySaveBaselineButton = new JButton("Save Dataset");
    private final JButton myRestoreBaselineButton = new JButton("Restore Dataset"); // NEW

    private final JLabel myStatusLabel;
    private final JLabel myComparisonsLabel;
    private final JLabel myTimeLabel;

    private final BarPanel myBarPanel;

    public AlgorithmAnalysisGUI() {
        super("Algorithm Analysis – Sorting Visualizer");

        myAlgorithmCombo = new JComboBox<>(SortType.values());
        mySizeCombo = new JComboBox<>(new Integer[]{10, 100});
        mySizeCombo.setSelectedItem(100);

        myRandomizeButton = new JButton("Randomize");
        mySortButton = new JButton("Sort");

        myStatusLabel = new JLabel("Ready.");
        myComparisonsLabel = new JLabel("Comparisons: 0");
        myTimeLabel = new JLabel("Time: 0 ms");

        myBarPanel = new BarPanel();

        // --- Layout ---
        setLayout(new BorderLayout());

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(new JLabel("Algorithm:"));
        controls.add(myAlgorithmCombo);
        controls.add(Box.createHorizontalStrut(10));
        controls.add(new JLabel("Size:"));
        controls.add(mySizeCombo);
        controls.add(Box.createHorizontalStrut(10));

        // Cleaned up – each button added once, in a logical order
        controls.add(myRandomizeButton);
        controls.add(mySaveBaselineButton);
        controls.add(myRestoreBaselineButton); // NEW
        controls.add(mySortButton);

        add(controls, BorderLayout.NORTH);
        add(myBarPanel, BorderLayout.CENTER);

        JPanel status = new JPanel(new FlowLayout(FlowLayout.LEFT));
        status.add(myStatusLabel);
        status.add(Box.createHorizontalStrut(20));
        status.add(myComparisonsLabel);
        status.add(Box.createHorizontalStrut(20));
        status.add(myTimeLabel);

        add(status, BorderLayout.SOUTH);

        // --- Listeners ---
        myRandomizeButton.addActionListener(e -> randomizeData());
        mySizeCombo.addActionListener(e -> randomizeData());
        mySortButton.addActionListener(e -> startSort());
        mySaveBaselineButton.addActionListener(e -> saveBaseline());
        myRestoreBaselineButton.addActionListener(e -> restoreBaseline()); // NEW

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        // initial dataset
        randomizeData();
    }

    /** Generate a new random list and repaint. */
    private void randomizeData() {
        if (myIsSorting) {
            return;
        }
        int size = (Integer) mySizeCombo.getSelectedItem();
        myCurrentSnapshot = myGenerator.generate(size);

        // Reset highlights/stats
        myHighlightA = -1;
        myHighlightB = -1;
        myStatusLabel.setText("Ready. Size = " + size);
        myComparisonsLabel.setText("Comparisons: 0");
        myTimeLabel.setText("Time: 0 ms");

        // Invalidate previous baseline
        myBaselineDataset = null;
        myBaselineFrozen = false;

        myBarPanel.repaint();
    }

    /** Saves a dataset to a bank to be restored across different algorithms. */
    private void saveBaseline() {
        if (myIsSorting || myCurrentSnapshot == null || myCurrentSnapshot.isEmpty()) {
            return;
        }
        myBaselineDataset = new ArrayList<>(myCurrentSnapshot);
        myBaselineFrozen = true;
        myStatusLabel.setText("Baseline saved (size = " + myBaselineDataset.size() + ")");
    }

    /** Restores the saved baseline into the current snapshot for visualization. */
    private void restoreBaseline() {
        if (myIsSorting || !myBaselineFrozen || myBaselineDataset == null || myBaselineDataset.isEmpty()) {
            return;
        }

        myCurrentSnapshot = new ArrayList<>(myBaselineDataset);
        myHighlightA = -1;
        myHighlightB = -1;

        myStatusLabel.setText("Baseline restored (size = " + myCurrentSnapshot.size() + ")");
        myComparisonsLabel.setText("Comparisons: 0");
        myTimeLabel.setText("Time: 0 ms");

        myBarPanel.repaint();
    }

    /** Start the selected sort on a background thread and animate bars. */
    private void startSort() {
        if (myIsSorting) {
            return;
        }
        SortType type = (SortType) myAlgorithmCombo.getSelectedItem();
        if (type == null || myCurrentSnapshot.isEmpty()) {
            return;
        }

        final SortAlgorithmBase sorter = SortFactory.createSorter(type);

        // Choose source list: baseline if frozen, else current snapshot
        final List<Integer> source = (myBaselineFrozen && myBaselineDataset != null)
                ? myBaselineDataset
                : myCurrentSnapshot;

        // Copy so we never mutate the stored baseline
        final List<Integer> working = new ArrayList<>(source);
        myIsSorting = true;
        setControlsEnabled(false);
        myStatusLabel.setText("Sorting with " + sorter.name() + "...");

        final long startNano = System.nanoTime();

        Thread worker = new Thread(() -> {
            // Listener called by the sorting algorithm
            SortUpdateListener listener = (list, indexA, indexB, comparisons) -> {
                // Make a copy for the GUI
                final List<Integer> snapshotCopy = new ArrayList<>(list);
                final long elapsedNs = System.nanoTime() - startNano;

                SwingUtilities.invokeLater(() -> {
                    myCurrentSnapshot = snapshotCopy;
                    myHighlightA = indexA;
                    myHighlightB = indexB;
                    myComparisonsLabel.setText("Comparisons: " + comparisons);
                    myTimeLabel.setText("Time: " + (elapsedNs / 1_000_000L) + " ms");
                    myBarPanel.repaint();
                });
            };

            // Run the visual sort
            sorter.sort(working, listener, DELAY_MS);

            final long finalTimeNs = sorter.getTimeDuration();
            final long finalComps = sorter.getComparisonCount();

            SwingUtilities.invokeLater(() -> {
                myIsSorting = false;
                setControlsEnabled(true);
                myHighlightA = -1;
                myHighlightB = -1;
                myTimeLabel.setText("Time: " + (finalTimeNs / 1_000_000L) + " ms");
                myComparisonsLabel.setText("Comparisons: " + finalComps);
                myStatusLabel.setText("Done: " + sorter.name());
                myBarPanel.repaint();
            });
        });

        worker.setDaemon(true);
        worker.start();
    }

    private void setControlsEnabled(final boolean enabled) {
        myAlgorithmCombo.setEnabled(enabled);
        mySizeCombo.setEnabled(enabled);
        myRandomizeButton.setEnabled(enabled);
        mySortButton.setEnabled(enabled);
        mySaveBaselineButton.setEnabled(enabled);
        myRestoreBaselineButton.setEnabled(enabled);
    }

    /** Panel that draws the current list as vertical bars. */
    private final class BarPanel extends JPanel {

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);

            if (myCurrentSnapshot == null || myCurrentSnapshot.isEmpty()) {
                return;
            }

            final int width = getWidth();
            final int height = getHeight();
            final int n = myCurrentSnapshot.size();

            final int barWidth = Math.max(1, width / n);
            int max = 1;
            for (int v : myCurrentSnapshot) {
                if (v > max) {
                    max = v;
                }
            }

            for (int i = 0; i < n; i++) {
                final int value = myCurrentSnapshot.get(i);
                final int barHeight =
                        (int) ((double) value / max * (height - 20));
                final int x = i * barWidth;
                final int y = height - barHeight;

                if (i == myHighlightA || i == myHighlightB) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLUE);
                }
                g.fillRect(x, y, barWidth - 1, barHeight);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(900, 400);
        }
    }

    /** Entry point to launch the GUI. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AlgorithmAnalysisGUI gui = new AlgorithmAnalysisGUI();
            gui.setVisible(true);
        });
    }
}
