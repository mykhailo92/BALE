package de.bale.ui;

import de.bale.language.Localizations;
import de.bale.ui.interfaces.IController;
import de.bale.ui.interfaces.ILearningUnitModel;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.util.*;

public class LearningUnitController implements IController {

    @FXML
    public Button nextButton;
    @FXML
    public Button closeButton;
    public Button scrollDownButton;
    @FXML
    private WebView learningUnit = new WebView();
    private WebEngine engine;
    private final File startPage = new File(String.valueOf(getClass().getResource("/example.html")));
    private ILearningUnitModel model;
    private Element[] slides;
    private int currentSlideIndicator = 0;

    /**
     * Start the WebEngine with example.html
     * <p>
     * When load is finished:
     * Add a StateListener to set Element[] container and register a JSBridge
     * Also Initializes ChapterSelection
     */
    @FXML
    private void initialize() {
        engine = learningUnit.getEngine();
        engine.load(startPage.toString());
        engine.getLoadWorker().stateProperty().addListener(
                (observableValue, oldState, newState) -> {
                    if (oldState.equals(Worker.State.RUNNING) && newState.equals(Worker.State.SUCCEEDED)) {
                        model.setContainer(getContainerFromDocument());
                        slides = getSlideFromDocument();
                        new JSBridge(model, engine).registerBridge("javaBridge");
                        setAllContainerInvisible();
                        model.setChapter(getChapterFromDocument());
                        createChapterIndex();
                    }
                }
        );
        createControlLabels();
    }

    private void createChapterIndex() {
        Document document = engine.getDocument();
        Element[] chapters = model.getChapter();
        for (int i = 0; i < chapters.length; i++) {
            Element entry = document.createElement("a");
            chapters[i].setAttribute("id", "chapter" + i);
            entry.setAttribute("href", "#chapter"+i);
            entry.setTextContent(Localizations.getLocalizedString("chapter") + " " + i);
            document.getElementById("chapter-select").appendChild(entry);
            document.getElementById("chapter-select").appendChild(document.createElement("br"));
        }
    }

    private Element[] getChapterFromDocument() {
        JSObject sectionList = (JSObject) engine.executeScript("getChapter();");
        return createArrayFromJSObject(sectionList);
    }

    /**
     * Sets the Display of all Container to invisible
     */

    private void setAllContainerInvisible() {
        for (Element containerElement : model.getContainer()) {
            setInvisible(containerElement);
        }
        for (Element slideElement : slides) {
            setInvisible(slideElement);
        }
    }

    private void createControlLabels() {
        nextButton.setText(Localizations.getLocalizedString("nextButton"));
        closeButton.setText(Localizations.getLocalizedString("closeButton"));
        scrollDownButton.setText(Localizations.getLocalizedString("scrollDownButton"));
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

    private Element[] getSlideFromDocument() {
        JSObject slidesList = (JSObject) engine.executeScript("getSlides();");
        return createArrayFromJSObject(slidesList);
    }

    /**
     * Casts a JavaScript response Object to an Array of Elements
     *
     * @param jsObjectList JSObject which holds a List-structure
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
        if (model.getContainerIndicator() < model.getContainer().length - 1) {
            model.setContainerIndicator(model.getContainerIndicator() + 1);
            Element currentContainer = model.getContainer()[model.getContainerIndicator()];
            if (getClasses(currentContainer).contains("slide_vorschau")) {
                setVisible(slides[currentSlideIndicator++]);
            } else if (getClasses(currentContainer).contains("info_and_slide")) {
                if (currentSlideIndicator > 0) {
                    setInvisible(slides[currentSlideIndicator - 1]);
                }
                setVisible(slides[currentSlideIndicator++]);
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

    /**
     * Set a Model for the Controller and add a Listener that is fired whenever the Model gets updated
     *
     * @param learningUnitModel A Model which holds Information important for the Controller
     */
    @Override
    public void setModel(ILearningUnitModel learningUnitModel) {
        model = learningUnitModel;
        model.addListener(listenedModel -> {
            nextButton.setDisable(listenedModel.isNextButtonDisabled());
            setVisible(model.getContainer()[listenedModel.getContainerIndicator()]);
            scrollDown();
        });
    }

    private void setInvisible(Element disableElement) {
        disableElement.setAttribute("style", "display:none");
    }

    private void setVisible(Element enableElement) {
        enableElement.setAttribute("style", "display:block");
    }

    public void scrollDown() {
        engine.executeScript("scrollToBottom();");
    }
}