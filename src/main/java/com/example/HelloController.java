package com.example;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.util.*;

public class HelloController {
    @FXML
    public Button nextButton;
    @FXML
    public Button closeButton;
    @FXML
    private WebView learningUnit = new WebView();
    private WebEngine engine;
    private final File startPage = new File(String.valueOf(getClass().getResource("/com/example/example.html")));
    private Element[] container;
    private int currentContainerIndicator = 0;

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
                        container = getContainersFromDocument();
                        JSBridge jsBridge = new JSBridge(engine);
                        disableAllContainer();
                    }
                }
        );
    }

    private void disableAllContainer() {
        for (Element sectiont : container) {
            disableElement(sectiont);
        }
    }

    /**
     * Executes JavaScript to get and prepare an Array of all Sections in the Learningunit
     * We need to convert to a List structure first, since the length of the JSObject is unknown.
     *
     * @return Array of all Section in the current Learningunit in order of appierence
     */

    private Element[] getContainersFromDocument() {
        JSObject sectionList = (JSObject) engine.executeScript("getContainer();");
        return createArrayFromJSObject(sectionList);
    }

    private Element[] createArrayFromJSObject(JSObject jsObjectList) {
        int slotCounter = 0;
        ArrayList<Element> elementList = new ArrayList<>();
        while (!jsObjectList.getSlot(slotCounter).equals("undefined")) {
            Element element = (Element) jsObjectList.getSlot(slotCounter++);
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
        if (currentContainerIndicator < container.length) {
            Element currentContainer = container[currentContainerIndicator];
            if (getClasses(currentContainer).contains("information")) {
                enableElement(container[currentContainerIndicator++]);
            }
            enableElement(container[currentContainerIndicator++]);
        }
    }

    private List<String> getClasses(Element element) {
        NamedNodeMap elementAttributes = element.getAttributes();
        if (elementAttributes.getNamedItem("class") != null) {
            return Arrays.asList(elementAttributes.getNamedItem("class").getNodeValue().split(" "));
        }
        return Collections.singletonList("none");
    }

    private void disableElement(Element disableElement) {
        disableElement.setAttribute("style", "display:none");
    }

    private void enableElement(Element enableElement) {
        enableElement.setAttribute("style", "display:block");
    }

    /**
     * Temporary fix to close the App in Fullscreenmode
     */
    @FXML
    private void closeApp() {
        Platform.exit();
    }
}