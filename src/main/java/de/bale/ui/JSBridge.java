package de.bale.ui;

import de.bale.repository.ExperimentRepository;
import de.bale.repository.FeedbackRepository;
import de.bale.repository.experiment.Experiment;
import de.bale.repository.feedback.Feedback;
import de.bale.ui.learningUnit.interfaces.ILearningUnitModel;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;


public class JSBridge {

    private final WebEngine engine;
    private final ILearningUnitModel model;
    private final Bridge jsBridge;

    /**
     * Public Class which holds function that can be called from JavaScript
     */
    public class Bridge {

        public void disablePreamble(String name, String date, String title) {
            if (name.length() > 0) {
                Experiment experiment = new Experiment(name, date, title);
                ExperimentRepository er = new ExperimentRepository();
                er.addExperiment(experiment);

                try {
                    Platform.runLater(() -> {
                        Element preamble = engine.getDocument().getElementById("preamble");
                        preamble.setAttribute("style", "display:none");
                        model.setContainerIndicator(model.getContainerIndicator() + 1);
                        model.setNextButtonDisabled(false);
                        model.setExperimentID(er.getCurrentExperimentID());
                    });
                } catch (NullPointerException npe) {
                    System.err.println("Preamble not found!");
                }
            }
        }
        public void setNextButtonDisabled(boolean disabled) {
            model.setNextButtonDisabled(disabled);
        }
        public void saveFeedback(String des, String date, int attempts, String comments) {
            Feedback fdb = new Feedback(model.getExperimentID(), des, date, attempts, comments);
            new FeedbackRepository().saveFeedback(fdb);
        }
    }

    public JSBridge(ILearningUnitModel model, WebEngine engine) {
        this.model = model;
        this.engine = engine;
        this.jsBridge = new Bridge();
    }

    /**
     * Register a JSBridge, which can be any public Class
     */
    public void registerBridge() {
        JSObject window = (JSObject) engine.executeScript("window");
        window.setMember("javaBridge", jsBridge);
    }
}
