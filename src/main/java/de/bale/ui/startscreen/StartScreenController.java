package de.bale.ui.startscreen;

import com.google.common.eventbus.Subscribe;
import de.bale.language.Localizations;
import de.bale.logger.Logger;
import de.bale.messages.eyetracking.EyetrackingRunningMessage;
import de.bale.messages.InitMessage;
import de.bale.messages.SceneChangeMessage;
import de.bale.messages.TaskDoneMessage;
import de.bale.settings.SettingsController;
import de.bale.storage.XMLUtils;
import de.bale.ui.SceneHandler;
import de.bale.ui.dialogs.CreateEntryDialog;
import de.bale.ui.dialogs.EditOrDeleteEntryDialog;
import de.bale.ui.learningUnit.LearningUnitController;
import de.bale.ui.learningUnit.LearningUnitModel;
import de.bale.ui.learningUnit.interfaces.ILearningUnitModel;
import de.bale.ui.startscreen.interfaces.IStartScreenController;
import de.bale.ui.startscreen.interfaces.IStartScreenModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.stage.Modality;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Optional;

public class StartScreenController implements IStartScreenController {
    @FXML
    Button openLearningUnitButton;
    @FXML
    TableView<LearningUnitEntry> learningUnitTable;
    @FXML
    TableColumn nameColumn;
    @FXML
    TableColumn pathColumn;
    @FXML
    Button openSettingsButton;
    @FXML
    Button editOrDeleteEntryButton;
    @FXML
    Button newEntryButton;
    @FXML
    Button saveTableViewButton;
    @FXML
    ProgressIndicator progressIndicator;
    IStartScreenModel model;

    @Override
    public void setModel(IStartScreenModel model) {
        this.model = model;
        Logger.getInstance().register(this);
        Platform.runLater(() -> {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("learningUnitTitle"));
            pathColumn.setCellValueFactory(new PropertyValueFactory<>("learningUnitPath"));
            populateLearningUnitTable();
            learningUnitTable.setItems(model.getEntries());
        });
    }

    @Subscribe
    public void gotEyetrackingStart(EyetrackingRunningMessage startMessage) {
        openLearningUnitButton.setDisable(false);
        progressIndicator.setVisible(false);
    }

    /**
     * Populates the LearningUnitTable with th elearningUnitTable.xml
     */
    private void populateLearningUnitTable() {
        Document document = XMLUtils.readXML("learningUnitTable.xml");
        NodeList learningUnitEntries = document.getElementsByTagName("LearningUnitEntry");
        for (int i = 0; i < learningUnitEntries.getLength(); i++) {
            Node item = learningUnitEntries.item(i);
            Element xmlElement = (Element) item;
            String name = xmlElement.getElementsByTagName("name").item(0).getTextContent();
            String path = xmlElement.getElementsByTagName("path").item(0).getTextContent();
            model.addEntry(name, path);
            Logger.getInstance().post(new InitMessage("Added TableEntry: " + name + " " + path));
        }
    }

    /**
     * Sets up the Labels and learningUnitTable properties, also checks if the Eyetracker is already Running
     */
    @FXML
    private void initialize() {
        createLocalizedLabels();
        learningUnitTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        learningUnitTable.widthProperty().addListener((source, oldWidth, newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) learningUnitTable.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });
        Platform.runLater(() -> {
            if (SceneHandler.getInstance().eyetrackerIsRunning()) {
                Logger.getInstance().post(new EyetrackingRunningMessage("Eyetracker is already running"));
            }
        });
    }

    private void createLocalizedLabels() {
        Logger.getInstance().post(new InitMessage("Started Creating Labels"));
        nameColumn.setText(Localizations.getLocalizedString("nameColumn"));
        pathColumn.setText(Localizations.getLocalizedString("pathColumn"));
        openLearningUnitButton.setText(Localizations.getLocalizedString("openLearningUnit"));
        openSettingsButton.setText(Localizations.getLocalizedString("openSettingsButton"));
        newEntryButton.setText(Localizations.getLocalizedString("newEntryButton"));
        editOrDeleteEntryButton.setText(Localizations.getLocalizedString("editOrDeleteEntryButton"));
        saveTableViewButton.setText(Localizations.getLocalizedString("saveButton"));
        Logger.getInstance().post(new TaskDoneMessage());
    }

    /**
     * Opens a Learnign Unit which is selected in the LearningUnitTable
     */
    @FXML
    public void openLearningUnit() {
        LearningUnitEntry selectedEntry = learningUnitTable.getSelectionModel().getSelectedItem();
        if (selectedEntry == null) {
            return;
        }
        Logger.getInstance().post(new SceneChangeMessage("Learning Unit - " + selectedEntry.getLearningUnitTitle()));

        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.changeScene(new LearningUnitController(selectedEntry.getLearningUnitPath()), "learningUnit.fxml", "title");
        ILearningUnitModel learningUnitModel = new LearningUnitModel();
        ((LearningUnitController) sceneHandler.getController()).setModel(learningUnitModel);
        sceneHandler.setStageFullScreen(true);
    }

    @FXML
    public void openSettings() {
        Logger.getInstance().post(new SceneChangeMessage("Settings"));
        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.changeScene(new SettingsController(), "settings.fxml", "settingsTitle");
        sceneHandler.setStageFullScreen(false);
    }

    /**
     * Creates a new Dialog to add a new Entry to the LearningUnitTable
     */
    @FXML
    public void addNewEntry() {
        CreateEntryDialog dialog = new CreateEntryDialog(Localizations.getLocalizedString("newEntryButton"));
        dialog.initOwner(learningUnitTable.getScene().getWindow());
        dialog.initModality(Modality.NONE);
        Logger.getInstance().post(new SceneChangeMessage("CreateEntryDialog"));
        Optional<Pair<String, String>> resultPair = dialog.showAndWait();
        resultPair.ifPresent(result -> {
            LearningUnitEntry newEntry = new LearningUnitEntry(result.getKey(), result.getValue());
            model.addEntry(newEntry);
        });
    }

    /**
     * Saves the Table Using XMLUtils
     */
    @FXML
    public void saveTableView() {
        Document document = XMLUtils.createDocument();
        Element rootElement = document.createElement("LearningUnitEntries");
        for (LearningUnitEntry learningUnitEntry : learningUnitTable.getItems()) {
            Element entry = XMLUtils.createTag(document, rootElement, "LearningUnitEntry");
            XMLUtils.createTag(document, entry, "name", learningUnitEntry.getLearningUnitTitle());
            XMLUtils.createTag(document, entry, "path", learningUnitEntry.getLearningUnitPath());
        }
        document.appendChild(rootElement);
        XMLUtils.writeXML(document, "learningUnitTable.xml");
    }

    @FXML
    public void editOrDeleteEntry() {
        if (learningUnitTable.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        LearningUnitEntry selectedItem = learningUnitTable.getSelectionModel().getSelectedItem();
        EditOrDeleteEntryDialog dialog = new EditOrDeleteEntryDialog(
                Localizations.getLocalizedString("editOrDeleteEntryButton"),
                learningUnitTable.getSelectionModel().getSelectedItem().getLearningUnitTitle(),
                learningUnitTable.getSelectionModel().getSelectedItem().getLearningUnitPath());
        dialog.initOwner(learningUnitTable.getScene().getWindow());
        dialog.initModality(Modality.NONE);
        Optional<Pair<String, String>> resultPair = dialog.showAndWait();
        resultPair.ifPresent(result -> {
            if (result.getKey().equals("") && result.getValue().equals("")) {
                model.removeEntry(selectedItem);
                return;
            }
            LearningUnitEntry newEntry = new LearningUnitEntry(result.getKey(), result.getValue());
            model.changeEntry(selectedItem, newEntry);
            learningUnitTable.getSelectionModel().select(newEntry);
        });
    }
}
