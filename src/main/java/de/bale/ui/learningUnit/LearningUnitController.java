package de.bale.ui.learningUnit;

import de.bale.language.Localizations;
import de.bale.ui.JSBridge;
import de.bale.ui.learningUnit.interfaces.ILearningUnitController;
import de.bale.ui.learningUnit.interfaces.ILearningUnitModel;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LearningUnitController implements ILearningUnitController {

    @FXML
    public Button nextButton;
    @FXML
    public Button closeButton;
    public Button scrollDownButton;
    @FXML
    private WebView learningUnit = new WebView();
    private WebEngine engine;
    private final String startPage;
    private ILearningUnitModel model;
    private JSBridge bridge;
    private SectionVisibleListener listener;

    public LearningUnitController(String filePath) {
        startPage = "file:///" + filePath;
    }


    /**
     * Start the WebEngine with example.html
     * <p>
     * When load is finished:
     * Add a StateListener to set Element[] container and register a JSBridge
     * Also Initializes ChapterSelection
     */
    @FXML
    private void initialize() {
        learningUnit.setContextMenuEnabled(false);
        engine = learningUnit.getEngine();
        engine.load(startPage);
        engine.getLoadWorker().stateProperty().addListener(
                (observableValue, oldState, newState) -> {
                    if (oldState.equals(Worker.State.RUNNING) && newState.equals(Worker.State.SUCCEEDED)) {
                        model.setContainer(getContainerFromDocument());
                        model.setSlides(getSlideFromDocument());
                        bridge = new JSBridge(model, engine);
                        bridge.registerBridge();
                        listener = new SectionVisibleListener(model);
                        setAllContainerInvisible();
                        prepareChapterIndex();
                        createHTMLControlLabels();
                    }
                }
        );
        createControlLabels();
    }

    /**
     * prepares Index and Jump Marks to the Chapters
     */
    private void prepareChapterIndex() {
        model.setChapter(getChapterFromDocument());
        Document document = engine.getDocument();
        Element[] chapterArray = model.getChapter();
        Element[] chapterMarks = new Element[chapterArray.length + 1];
        chapterMarks[0] = document.getElementById("chapter-select");
        for (int i = 0; i < chapterArray.length; i++) {
            Element entry = document.createElement("a");
            chapterArray[i].setAttribute("id", "chapter" + i);
            entry.setAttribute("href", "#chapter" + i);
            setInvisible(entry);
            entry.setTextContent(Localizations.getLocalizedString("chapter") + " " + i);
            chapterMarks[i + 1] = entry;
            chapterMarks[0].appendChild(entry);
            chapterMarks[0].appendChild(document.createElement("br"));
        }
        model.setChapterMarks(chapterMarks);
        setInvisible(chapterMarks[0]);
    }

    /**
     * Executes JavaScript to get and prepare an Array of all Chapter in the Learning-unit
     * We need to convert to a List structure first, since the length of the JSObject is unknown.
     *
     * @return Array of all Chapter in the current Learning-unit in order of appearance
     */
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
        for (Element slideElement : model.getSlides()) {
            setInvisible(slideElement);
        }
    }

    /**
     * Create localized Labels for JavaFX Control Elements
     */
    private void createControlLabels() {
        nextButton.setText(Localizations.getLocalizedString("nextButton"));
        closeButton.setText(Localizations.getLocalizedString("closeButton"));
        scrollDownButton.setText(Localizations.getLocalizedString("scrollDownButton"));
    }

    /**
     * Create Localized HTML Labels
     */
    private void createHTMLControlLabels() {
        Element[] readOutButtons = getControlLabelsFromDocument();
        for (Element element : readOutButtons) {
            if (getClasses(element).contains("reading")) {
                element.setTextContent(Localizations.getLocalizedString("readOutButton"));
            } else if (getClasses(element).contains("save")) {
                element.setAttribute("value",Localizations.getLocalizedString("saveButton"));
            } else if (getClasses(element).contains("preamble-button")) {
                element.setTextContent(Localizations.getLocalizedString("preamble-button"));
            } else if (getClasses(element).contains("demo-video")) {
                element.setTextContent(Localizations.getLocalizedString("demoVideoButton"));
            }
        }
    }
    private Element[] getControlLabelsFromDocument() {
        JSObject readOutButtonList = (JSObject) engine.executeScript("getControlLabels();");
        return createArrayFromJSObject(readOutButtonList);
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
            if (getClasses(model.getContainer()[model.getContainerIndicator() - 1]).contains("diashow")) {
                model.setCurrentSlideIndicator(model.getCurrentSlideIndicator() + 1);
            } else if (getClasses(currentContainer).contains("info-and-slide")) {
                model.setCurrentSlideIndicator(model.getCurrentSlideIndicator() + 1);
            }
            listener.notifyMyself();
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
            if (listenedModel.getContainerIndicator() >= 0) {
                setVisible(model.getContainer()[listenedModel.getContainerIndicator()]);
            }
            scrollToBottom();
            setVisible(model.getChapterMarks()[model.getChapterIndicator()]);
            if (!model.getCloseButtonText().equals("")) {
                closeButton.setText(model.getCloseButtonText());
            }
            Platform.runLater(() -> checkChapter(listenedModel));
        });

        model.addListener((listenedModel) -> {
            setVisible(model.getSlides()[listenedModel.getCurrentSlideIndicator()]);
            if (listenedModel.getCurrentSlideIndicator() != 0) {
                setInvisible(model.getSlides()[listenedModel.getCurrentSlideIndicator() - 1]);
            }
        });
    }

    /**
     * Checks if a Chapter is made Visible by looking at its Height by using JavaScript
     * If the Threshold is met, we increment the chapterIndicator of the model
     * If the chapterIndicator is 0 we set the Indicator to 0 to display the border
     *
     * @param model Model which is listened to
     */
    private void checkChapter(ILearningUnitModel model) {
        if (model.getChapterIndicator() < model.getChapterMarks().length - 1) {
            Integer height = (Integer) engine.executeScript("getElementHeightByID('chapter" + model.getChapterIndicator() + "');");
            if (height > 20) { // Height threshold which counts as displayed Chapter
                if (model.getChapterIndicator() == 0) {
                    model.setChapterIndicator(0);
                }
                model.setChapterIndicator(model.getChapterIndicator() + 1);
            }
        }
    }

    private void setInvisible(Element disableElement) {
        disableElement.setAttribute("style", "display:none");
    }

    private void setVisible(Element enableElement) {
        enableElement.setAttribute("style", "display:block");
    }

    /**
     * Executes JavaScript to smoothly scroll to the Bottom of the Webview
     */
    public void scrollToBottom() {
        engine.executeScript("scrollToBottom();");
    }
}