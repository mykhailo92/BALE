package de.bale.ui.learningUnit;

import de.bale.language.Localizations;
import de.bale.logger.Logger;
import de.bale.messages.*;
import de.bale.messages.eyetracking.AoiMapMessage;
import de.bale.messages.eyetracking.EyetrackingAOIMessage;
import de.bale.messages.eyetracking.WriteToPythonMessage;
import de.bale.ui.JSBridge;
import de.bale.ui.SceneHandler;
import de.bale.ui.learningUnit.interfaces.ILearningUnitController;
import de.bale.ui.learningUnit.interfaces.ILearningUnitModel;
import de.bale.ui.startscreen.StartScreenController;
import de.bale.ui.startscreen.StartScreenModel;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class LearningUnitController implements ILearningUnitController {

    @FXML
    public Button nextButton;
    @FXML
    public Button closeButton;
    public Button scrollDownButton;
    @FXML
    private WebView learningUnit;
    private WebEngine engine;
    private final String startPage;
    private ILearningUnitModel model;
    private JSBridge bridge;
    private SectionVisibleListener listener;
    private Logger logger;

    public LearningUnitController(String filePath) {
        startPage = "file:///" + filePath;
        logger = Logger.getInstance();
    }

    public void newEyePosition(int x, int y) {
        Platform.runLater(() -> {
            engine.executeScript("drawCircleAtPosition(" + x + "," + y + ");");
            try {
                Instant end = Instant.now();
                Object object = engine.executeScript("getElementFromPosition(" + x + "," + y + ");");
                Element areaOfInterest = (Element) object;
                String areaOfInterestAttribute = areaOfInterest.getAttribute("aoi");
                long timeDifference = Duration.between(model.getLastEyetrackingTime(), end).toMillis();
                model.addToAreaOfInterestMap(areaOfInterestAttribute, timeDifference);
                if (areaOfInterestAttribute != null) {
                    model.setLastAoi(areaOfInterest);
                    Logger.getInstance().post(new EyetrackingAOIMessage(areaOfInterestAttribute + " Last AoI was looked at  for: " + timeDifference + "ms"));
                }
                model.setLastEyetrackingTime(Instant.now());
            } catch (ClassCastException | JSException ignored) {
            } //In case something that no Element is looked at or the JavaScript could not find an Element
        });
    }

    @Override
    public void eyetrackingFitIsDone() {
        Platform.runLater(() -> {
            engine.executeScript("fitDone();");
            model.setNextButtonDisabled(false);
            model.setLastEyetrackingTime(Instant.now());
        });

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
        Logger.getInstance().post(new WriteToPythonMessage("start"));
        engine = learningUnit.getEngine();
        logger.post(new InitMessage("Loading LearningUnit: " + startPage + "..."));
        engine.load(startPage);
        engine.getLoadWorker().stateProperty().addListener(
                (observableValue, oldState, newState) -> {
                    if (oldState.equals(Worker.State.RUNNING) && newState.equals(Worker.State.SUCCEEDED)) {
                        logger.post(new TaskDoneMessage());
                        LearningUnitUtils.createStyleNode(engine, SceneHandler.getInstance().getThemeName());
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
        Logger.getInstance().register(new EyeTrackerListener(this));
        createControlLabels();
    }


    /**
     * prepares Index and Jump Marks to the Chapters
     */
    private void prepareChapterIndex() {
        logger.post(new InitMessage("Preparing ChapterIndex..."));
        model.setChapter(getChapterFromDocument());
        Document document = engine.getDocument();
        Element[] chapterArray = model.getChapter();
        Element[] chapterMarks = new Element[chapterArray.length + 1];
        chapterMarks[0] = document.getElementById("chapter-select");
        for (int i = 0; i < chapterArray.length; i++) {
            logger.post(new InitMessage("Adding ID chapter" + i + "..."));
            Element entry = document.createElement("a");
            chapterArray[i].setAttribute("id", "chapter" + i);
            entry.setAttribute("href", "#chapter" + i);
            setInvisible(entry);
            entry.setTextContent(Localizations.getLocalizedString("chapter") + " " + i);
            chapterMarks[i + 1] = entry;
            chapterMarks[0].appendChild(entry);
            chapterMarks[0].appendChild(document.createElement("br"));
        }
        logger.post(new TaskDoneMessage());
        model.setChapterMarks(chapterMarks);
    }

    /**
     * Executes JavaScript to get and prepare an Array of all Chapter in the Learning-unit
     * We need to convert to a List structure first, since the length of the JSObject is unknown.
     *
     * @return Array of all Chapter in the current Learning-unit in order of appearance
     */
    private Element[] getChapterFromDocument() {
        JSObject sectionList = (JSObject) engine.executeScript("getChapter();");
        return LearningUnitUtils.createArrayFromJSObject(sectionList);
    }

    /**
     * Sets the Display of all Container to invisible
     */

    private void setAllContainerInvisible() {
        logger.post(new InitMessage("Setting all Container Invisible..."));
        for (Element containerElement : model.getContainer()) {
            setInvisible(containerElement);
        }
        for (Element slideElement : model.getSlides()) {
            setInvisible(slideElement);
        }
        logger.post(new TaskDoneMessage());
    }

    /**
     * Create localized Labels for JavaFX Control Elements
     */
    private void createControlLabels() {
        logger.post(new InitMessage("Creating Localized Labels"));
        nextButton.setText(Localizations.getLocalizedString("nextButton"));
        closeButton.setText(Localizations.getLocalizedString("closeButton"));
        scrollDownButton.setText(Localizations.getLocalizedString("scrollDownButton"));
        logger.post(new TaskDoneMessage());
    }

    /**
     * Create Localized HTML Labels
     */
    private void createHTMLControlLabels() {
        logger.post(new InitMessage("Creating HTML localized Labels"));
        Element[] readOutButtons = getControlLabelsFromDocument();
        for (Element element : readOutButtons) {
            List<String> elementClasses = LearningUnitUtils.getClasses(element);
            if (elementClasses.contains("reading")) {
                element.setTextContent(Localizations.getLocalizedString("readOutButton"));
            } else if (elementClasses.contains("save")) {
                element.setAttribute("value", Localizations.getLocalizedString("saveButton"));
            } else if (elementClasses.contains("preamble-button")) {
                element.setTextContent(Localizations.getLocalizedString("preamble-button"));
            } else if (elementClasses.contains("demo-video")) {
                element.setTextContent(Localizations.getLocalizedString("demoVideoButton"));
            }
        }
        logger.post(new TaskDoneMessage());
    }

    private Element[] getControlLabelsFromDocument() {
        JSObject readOutButtonList = (JSObject) engine.executeScript("getControlLabels();");
        return LearningUnitUtils.createArrayFromJSObject(readOutButtonList);
    }

    /**
     * Executes JavaScript to get and prepare an Array of all Sections in the Learning-unit
     * We need to convert to a List structure first, since the length of the JSObject is unknown.
     *
     * @return Array of all Section in the current Learning-unit in order of appearance
     */

    private Element[] getContainerFromDocument() {
        JSObject sectionList = (JSObject) engine.executeScript("getContainer();");
        return LearningUnitUtils.createArrayFromJSObject(sectionList);
    }

    private Element[] getSlideFromDocument() {
        JSObject slidesList = (JSObject) engine.executeScript("getSlides();");
        return LearningUnitUtils.createArrayFromJSObject(slidesList);
    }


    /**
     * Sets the Display of the next invisible Container to visible
     * Special Case: containerIndicator == 0: Display first Element
     */
    @FXML
    private void displayNextSection() {
        if (model.getContainerIndicator() < model.getContainer().length - 1) {
            model.setContainerIndicator(model.getContainerIndicator() + 1);
            logger.post(new SectionMessage("Displaying new Section"));
            Element currentContainer = model.getContainer()[model.getContainerIndicator()];
            if (LearningUnitUtils.getClasses(model.getContainer()[model.getContainerIndicator() - 1]).contains("diashow")) {
                model.setCurrentSlideIndicator(model.getCurrentSlideIndicator() + 1);
                logger.post(new SectionMessage("Displaying Diashow"));
            } else if (LearningUnitUtils.getClasses(currentContainer).contains("info-and-slide")) {
                model.setCurrentSlideIndicator(model.getCurrentSlideIndicator() + 1);
                logger.post(new SectionMessage("Displaying info-and-slide"));
            }
            listener.notifyMyself();
        }
    }

    /**
     * Temporary fix to close the App in Fullscreen-mode
     */
    @FXML
    private void closeApp() {
        Logger.getInstance().post(new SceneChangeMessage("Startscreen"));
        Logger.getInstance().post(new AoiMapMessage(model.getAoiMap()));
        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.changeScene(new StartScreenController(), "startscreen.fxml", "selectionTitle");
        ((StartScreenController) sceneHandler.getController()).setModel(new StartScreenModel());
        sceneHandler.setStageFullScreen(false);
        Logger.getInstance().post(new WriteToPythonMessage("stop"));
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
            if (model.getChapterIndicator() >= 1) {
                setVisible(model.getChapterMarks()[model.getChapterIndicator()]);
            }
            if (!model.getCloseButtonText().equals("")) {
                closeButton.setText(model.getCloseButtonText());
            }
        });

        model.addListener((listenedModel) -> {
            try {
                setVisible(model.getSlides()[listenedModel.getCurrentSlideIndicator()]);
                if (listenedModel.getCurrentSlideIndicator() != 0) {
                    setInvisible(model.getSlides()[listenedModel.getCurrentSlideIndicator() - 1]);
                }
            } catch (ArrayIndexOutOfBoundsException exception) {
                Logger.getInstance().post(new ErrorMessage(exception.getMessage()));
            }
        });
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