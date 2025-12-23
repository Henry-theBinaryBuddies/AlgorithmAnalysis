package view;

import controller.AlgorithmAnalysisController;

import model.SortType;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI to visualize sorting algorithms using bars.
 */
public final class AlgorithmAnalysisGUI extends JFrame {

    @Serial
    private static final long serialVersionUID = 1L;

    private AlgorithmAnalysisController myController;

    // Snapshot of current list state to draw (always touched on EDT)
    private List<Integer> myCurrentSnapshot = new ArrayList<>();

    // Indices currently being operated on
    private int myHighlightA = -1;
    private int myHighlightB = -1;

    // GUI components
    private final JComboBox<SortType> myAlgorithmCombo;
    private final JComboBox<Integer> mySizeCombo;
    private final JButton myRandomizeButton;
    private final JButton mySortButton;
    private final JButton mySaveBaselineButton;
    private final JButton myRestoreBaselineButton;
    private final JSlider mySpeedSlider;
    private final JToggleButton myCompareToggle;


    private final JLabel myDelayLabel;
    private final JLabel myStatusLabel;
    private final JLabel myComparisonsLabel;
    private final JLabel myTimeLabel;

    private final BarPanel myBarPanel;

    public AlgorithmAnalysisGUI() {
        super("Algorithm Analysis – Sorting Visualizer");
        //Constructors:
        String myCompareLabel = "Ascending";
        myAlgorithmCombo = new JComboBox<>(SortType.values());
        mySizeCombo = new JComboBox<>(new Integer[]{10, 20, 100, 200, 500});
        mySizeCombo.setSelectedItem(10);
        mySaveBaselineButton = new JButton("Save Dataset");
        myRestoreBaselineButton = new JButton("Restore Dataset");
        mySpeedSlider = new JSlider(5, 500);

        myRandomizeButton = new JButton("Randomize");
        mySortButton = new JButton("Sort");
        myCompareToggle = new JToggleButton(myCompareLabel);

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
        myDelayLabel = new JLabel("Visual Delay: " + mySpeedSlider.getValue() + "ms");
        controls.add(myDelayLabel);
        controls.add(mySpeedSlider);
        controls.add(myCompareToggle);

        // Buttons
        controls.add(myRandomizeButton);
        controls.add(mySaveBaselineButton);
        controls.add(myRestoreBaselineButton);
        myRestoreBaselineButton.setEnabled(false);
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
        myRandomizeButton.addActionListener(e ->
                myController.handleRandomizeRequested());
        mySizeCombo.addActionListener(e ->
                myController.handleRandomizeRequested());
        mySpeedSlider.addChangeListener(e -> {
            myController.handleSpeedChanged(mySpeedSlider.getValue());
        });
        mySortButton.addActionListener(
                e -> myController.handleSortRequested());
        mySaveBaselineButton.addActionListener(
                e -> myController.handleSaveBaseline());
        myRestoreBaselineButton.addActionListener(
                e -> myController.handleRestoreBaseLine());
        myCompareToggle.addActionListener(e -> {
            myController.handleCompareModeToggled(myCompareToggle.isSelected());
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        myBarPanel.setBackground(Color.BLACK);
    }

    public void setControlsEnabled(final boolean theEnabled) {
        myAlgorithmCombo.setEnabled(theEnabled);
        mySizeCombo.setEnabled(theEnabled);
        myRandomizeButton.setEnabled(theEnabled);
        mySpeedSlider.setEnabled(theEnabled);
        mySortButton.setEnabled(theEnabled);
        mySaveBaselineButton.setEnabled(theEnabled);
        myCompareToggle.setEnabled(theEnabled);
    }

    //Helper methods:

    public void setController(final AlgorithmAnalysisController theController) {
        myController = theController;
    }

    public void setSnapshot(final List<Integer> theSnapshot) {
        myCurrentSnapshot = theSnapshot;
        myHighlightA = -1;
        myHighlightB = -1;

        myBarPanel.repaint();
    }

    public void setCompareToggleLabel(final boolean theDescending) {
        if (theDescending) {
            myCompareToggle.setText("Descending");
        }
        else {
            myCompareToggle.setText("Ascending");
        }
    }

    public void setDelayLabel(final int theDelayMs) {
        myDelayLabel.setText("Visual Delay: " +theDelayMs + "ms");
    }

    public boolean isCompareToggleSelected() {
        return myCompareToggle.isSelected();
    }

    public void setHighlights(final int theIndexA, final int theIndexB) {
        myHighlightA = theIndexA;
        myHighlightB = theIndexB;
    }

    public void setComparisons(final long theComparisons) {
        myComparisonsLabel.setText("Comparisons: " + theComparisons);
    }

    public void setTimeLabel(final long theTimeMs) {
        myTimeLabel.setText("Time: " + theTimeMs + " ms");
    }

    public void setStatus(final String theStatus) {
        myStatusLabel.setText(theStatus);
    }

    public void resetStatsAfterRandomize(final int theSize) {
        myStatusLabel.setText("Ready. Size = " + theSize);
        myComparisonsLabel.setText("Comparisons: 0");
        myTimeLabel.setText("Time: 0 ms");
    }

    public int getSelectedSize() {
        return (Integer) mySizeCombo.getSelectedItem();
    }

    public List<Integer> getCurrentSnapshot() {
        return myCurrentSnapshot;
    }

    public int getSelectedSpeed() {
        return mySpeedSlider.getValue();
    }

    public SortType getSelectedAlgorithm() {
        return (SortType) myAlgorithmCombo.getSelectedItem();
    }

    public void setRestoreBaselineEnabled(final boolean theEnabled) {
        myRestoreBaselineButton.setEnabled(theEnabled);
    }

    public void showBaselineSavedStatus(final int theSize) {
        myStatusLabel.setText("Baseline saved. (size = " + theSize + ")");
    }

    public void showBaselineRestoredStatus(final int theSize) {
        myStatusLabel.setText("Baseline restored. (size = " + theSize + ")");
    }



    /**
     * Panel that draws the current list as vertical bars.
     */
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

                // --- draw bar ---
                if (i == myHighlightA || i == myHighlightB) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLUE);
                }
                g.fillRect(x, y, barWidth - 1, barHeight);

                // --- draw label (only if there's room) ---
                if (barWidth >= 12) { // avoid unreadable spam on tiny bars
                    final String label = String.valueOf(value);
                    g.setColor(Color.GREEN); // stands out on blue/red

                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(label);
                    int textHeight = fm.getAscent();

                    // Center horizontally in bar
                    int textX = x + (barWidth - textWidth) / 2;

                    // Put text near top of bar if tall enough, else above it
                    int textY;
                    if (barHeight >= textHeight + 4) {
                        textY = y + textHeight;         // inside bar
                    } else {
                        textY = y - 2;                  // just above bar
                    }

                    g.drawString(label, textX, textY);
                }
            }
        }
    }
}
