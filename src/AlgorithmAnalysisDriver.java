import view.AlgorithmAnalysisGUI;
import model.AlgorithmAnalysisModel;
import controller.AlgorithmAnalysisController;

import javax.swing.*;

public class AlgorithmAnalysisDriver {

    /** Entry point to launch the GUI. */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            AlgorithmAnalysisModel model = new AlgorithmAnalysisModel();
            AlgorithmAnalysisGUI view = new AlgorithmAnalysisGUI();
            AlgorithmAnalysisController controller =
                    new AlgorithmAnalysisController(model, view);

            view.setController(controller);
            // initial dataset
            controller.handleRandomizeRequested();
            view.setVisible(true);
        });
    }
}
