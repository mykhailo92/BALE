package de.bale.ui;

import de.bale.repository.TimeStampRepository;
import de.bale.repository.UsersAnswers.UsersAnswers;
import de.bale.repository.UsersAnswersRepository;
import de.bale.repository.timeStamp.TimeStamp;
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

        public void disablePreamble(int id) {
            if (id == 0) { return; }
            try {
                Platform.runLater(() -> {
                    Element preamble = engine.getDocument().getElementById("preamble");
                    preamble.setAttribute("style", "display:none");
                    model.setContainerIndicator(model.getContainerIndicator() + 1);
                    model.setNextButtonDisabled(false);
                    model.setExperimentID(id);
                });
            } catch (NullPointerException npe) {
                System.err.println("Preamble not found!");
            }
        }
        public void setNextButtonDisabled(boolean disabled) {
            model.setNextButtonDisabled(disabled);
        }
        public void saveUsersAnswer(int id, String des, String answer, int attempts) {
            UsersAnswers answers = new UsersAnswers(id, des, answer, attempts);
            new UsersAnswersRepository().save(answers);
        }
        public void saveTimestamp(int id, String des, String date) {
            TimeStamp ts = new TimeStamp(id, des, date);
            new TimeStampRepository().save(ts);
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
