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
     * When load is finished:
     * Add a StateListener to set Element[] container and register a JSBridge
     */
    @FXML
    private void initialize() {
        engine = learningUnit.getEngine();
        engine.load(startPage.toString());
        engine.getLoadWorker().stateProperty().addListener(
                (observableValue, oldState, newState) -> {
                    if (oldState.equals(Worker.State.RUNNING) && newState.equals(Worker.State.SUCCEEDED)) {
                        container = getContainerFromDocument();
                        new JSBridge(engine).registerBridge("javaBridge");
                        setAllContainerInvisible();
                    }
                }
        );
    }

    /**
     * Sets the Display of all Container to invisible
     */
    private void setAllContainerInvisible() {
        for (Element containerElement : container) {
            setInvisible(containerElement);
        }
    }

    /**
     * Executes JavaScript to get and prepare an Array of all Sections in the Learning-unit
     * We need to convert to a List structure first, since the length of the JSObject is unknown.
     *
     * @return Array of all Section in the current Learning-unit in order of appearance
     */

    private Element[] getContainerFromDocument() {
        JSObject sectionList = (JSObject) engine.executeScript("getContainer();");
        return createArrayFromJSObject(sectionList);
    }

    /**
     * Casts a JavaScript response Object to an Array of Elements
     *
     * @param jsObjectList JSObject which holds a Liststructure
     * @return Array of Elements which were held by the jsObjectList
     */
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

    /**
     * Sets the Display of the next invisible Container to visible
     */
    @FXML
    private void displayNextSection() {
        if (currentContainerIndicator < container.length) {
            Element currentContainer = container[currentContainerIndicator];
            if (getClasses(currentContainer).contains("information")) {
                setVisible(container[currentContainerIndicator++]);
            }
            setVisible(container[currentContainerIndicator++]);
        }
    }

    /**
     * @param element HTML DOM Element
     * @return String list of all HTML Classes that element has. Returns "none" when the Element has no HTML CLass.
     */
    private List<String> getClasses(Element element) {
        NamedNodeMap elementAttributes = element.getAttributes();
        if (elementAttributes.getNamedItem("class") != null) {
            return Arrays.asList(elementAttributes.getNamedItem("class").getNodeValue().split(" "));
        }
        return Collections.singletonList("none");
    }

    private void setInvisible(Element disableElement) {
        disableElement.setAttribute("style", "display:none");
    }

    private void setVisible(Element enableElement) {
        enableElement.setAttribute("style", "display:block");
    }

    /**
     * Temporary fix to close the App in Fullscreen-mode
     */
    @FXML
    private void closeApp() {
        Platform.exit();
    }
}