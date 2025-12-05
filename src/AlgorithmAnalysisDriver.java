import view.AlgorithmAnalysisGUI;

import javax.swing.*;

public class AlgorithmAnalysisDriver {

    /** Entry point to launch the GUI. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AlgorithmAnalysisGUI gui = new AlgorithmAnalysisGUI();
            gui.setVisible(true);
        });
    }
}
