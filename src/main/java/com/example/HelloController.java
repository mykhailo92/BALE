package com.example;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

public class HelloController {
    @FXML
    public Button nextButton;
    @FXML
    public Button closeButton;
    @FXML
    private WebView learningUnit = new WebView();
    private WebEngine engine;
    private final File startPage = new File(String.valueOf(getClass().getResource("/com/example/example.html")));
    private Element[] sections;
    private int currentSectionIndicator = 0;

    /**
     * Start the WebEngine with example.html
     * <p>
     * Add a StateListener to set Element[] Sections when load is finished
     */
    @FXML
    private void initialize() {
        engine = learningUnit.getEngine();
        engine.load(startPage.toString());
        engine.getLoadWorker().stateProperty().addListener(
                (observableValue, oldState, newState) -> {
                    if (!newState.equals(Worker.State.SUCCEEDED)) {
                        return;
                    } else if (oldState.equals(Worker.State.RUNNING)) {
                        sections = getSectionsFromDocument();
                        disableAllSections();
                    }
                }
        );
    }

    private void disableAllSections() {
        for (Element e : sections) {
            e.setAttribute("style", "display: none;");
        }
    }

    /**
     * Executes JavaScript to get and prepare an Array of all Sections in the Learningunit
     * We need to convert to a List structure first, since the length of the JSObject is unknown.
     *
     * @return Array of all Section in the current Learningunit in order of appierence
     */

    private Element[] getSectionsFromDocument() {
        JSObject sectionList = (JSObject) engine.executeScript("getSections();");
        int slotCounter = 0;
        ArrayList<Element> elementList = new ArrayList<>();
        while (!sectionList.getSlot(slotCounter).equals("undefined")) {
            Element element = (Element) sectionList.getSlot(slotCounter++);
            elementList.add(element);
        }
        Element[] elementArray = new Element[elementList.size()];
        for (int i = 0; i < elementArray.length; i++) {
            elementArray[i] = elementList.get(i);
        }
        return elementArray;
    }

    @FXML
    private void displayNextSection() {
        if (currentSectionIndicator < sections.length) {
            sections[currentSectionIndicator++].setAttribute("style", "display:block;");
        }

    }

    /**
     * Temporary fix to close the App in Fullscreenmode
     */
    @FXML
    private void closeApp() {
        Platform.exit();
    }
}