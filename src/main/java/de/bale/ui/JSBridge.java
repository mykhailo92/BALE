package de.bale.ui;

import de.bale.logger.Logger;
import de.bale.messages.eyetracking.EyetrackingCallibrationDoneMessage;
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
        public void disablePreamble() {
            try {
                Platform.runLater(() -> {
                    Element preamble = engine.getDocument().getElementById("preamble");
                    preamble.setAttribute("style", "display:none");
                    model.setContainerIndicator(model.getContainerIndicator() + 1);
//                    model.setNextButtonDisabled(false);
                });
            } catch (NullPointerException npe) {
                System.err.println("Preamble not found!");
            }
        }
        public void setNextButtonDisabled(boolean disabled) {
            model.setNextButtonDisabled(disabled);
        }
        public void callibrationDone() {
            Logger.getInstance().post(new EyetrackingCallibrationDoneMessage());
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