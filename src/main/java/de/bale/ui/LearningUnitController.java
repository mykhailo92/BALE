package de.bale.ui;

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

public class LearningUnitController {
    //region Variables
    @FXML
    public Button nextButton;
    @FXML
    public Button closeButton;
    @FXML
    private WebView learningUnit = new WebView();
    private WebEngine engine;
    private final File startPage = new File(String.valueOf(getClass().getResource("/example.html")));
    private Element[] container;
    private LearningUnitModel model;
    //endregion

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
     * Special Case: containerIndicator == 0: Display first Element
     */
    @FXML
    private void displayNextSection() {
        if (model.isFirstFlag()) {
            model.setContainerIndicator(0);
            model.setFirstFlag(false);
        } else if (model.getContainerIndicator() < container.length - 1) {
            model.setContainerIndicator(model.getContainerIndicator() + 1);
            Element currentContainer = container[model.getContainerIndicator()];
            if (getClasses(currentContainer).contains("information")) {
                model.setContainerIndicator(model.getContainerIndicator() + 1);
            }
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

    /**
     * Temporary fix to close the App in Fullscreen-mode
     */
    @FXML
    private void closeApp() {
        Platform.exit();
    }


    public void setModel(LearningUnitModel learningUnitModel) {
        model = learningUnitModel;
        model.addListener((listenedModel) -> {
            setVisible(container[listenedModel.getContainerIndicator()]);
        });
    }

    /**
     * Sets the Display of all Container to invisible
     */
    private void setAllContainerInvisible() {
        for (Element containerElement : container) {
            setInvisible(containerElement);
        }
    }
    void setInvisible(Element disableElement) {
        disableElement.setAttribute("style", "display:none");
    }

    void setVisible(Element enableElement) {
        enableElement.setAttribute("style", "display:block");
    }
}