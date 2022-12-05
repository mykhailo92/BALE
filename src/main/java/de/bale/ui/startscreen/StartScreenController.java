package de.bale.ui.startscreen;

import de.bale.language.Localizations;
import de.bale.settings.SettingsController;
import de.bale.storage.XMLUtils;
import de.bale.ui.SceneHandler;
import de.bale.ui.learningUnit.LearningUnitController;
import de.bale.ui.learningUnit.LearningUnitModel;
import de.bale.ui.learningUnit.interfaces.ILearningUnitModel;
import de.bale.ui.startscreen.interfaces.IStartScreenController;
import de.bale.ui.startscreen.interfaces.IStartScreenModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.stage.Modality;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
    IStartScreenModel model;

    @Override
    public void setModel(IStartScreenModel model) {
        this.model = model;
        Platform.runLater(() -> {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("learningUnitTitle"));
            pathColumn.setCellValueFactory(new PropertyValueFactory<>("learningUnitPath"));
            populateLearningUnitTable();
            learningUnitTable.setItems(model.getEntries());
        });
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
        }
    }

    /**
     * Sets up the Labels and learningUnitTable properties
     */
    @FXML
    private void initialize() {
        createLocalizedLabels();
        learningUnitTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        learningUnitTable.widthProperty().addListener((source, oldWidth, newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) learningUnitTable.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });
    }

    private void createLocalizedLabels() {
        nameColumn.setText(Localizations.getLocalizedString("nameColumn"));
        pathColumn.setText(Localizations.getLocalizedString("pathColumn"));
        openLearningUnitButton.setText(Localizations.getLocalizedString("openLearningUnit"));
        openSettingsButton.setText(Localizations.getLocalizedString("openSettingsButton"));
        newEntryButton.setText(Localizations.getLocalizedString("newEntryButton"));
        editOrDeleteEntryButton.setText(Localizations.getLocalizedString("editOrDeleteEntryButton"));
        saveTableViewButton.setText(Localizations.getLocalizedString("saveButton"));
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
        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.changeScene(new LearningUnitController(selectedEntry.getLearningUnitPath()), "learningUnit.fxml", "title");
        ILearningUnitModel learningUnitModel = new LearningUnitModel();
        ((LearningUnitController) sceneHandler.getController()).setModel(learningUnitModel);
        sceneHandler.setStageFullScreen(true);
    }

    @FXML
    public void openSettings() {
        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.changeScene(new SettingsController(), "settings.fxml", "settingsTitle");
        sceneHandler.setStageFullScreen(false);
    }

    @FXML
    public void addNewEntry() {
        CreateEntryDialog dialog = new CreateEntryDialog(Localizations.getLocalizedString("newEntryButton"));
        dialog.initOwner(learningUnitTable.getScene().getWindow());
        dialog.initModality(Modality.NONE);
        Optional<Pair<String, String>> resultPair = dialog.showAndWait();
        resultPair.ifPresent(result -> {
            LearningUnitEntry newEntry = new LearningUnitEntry(result.getKey(), result.getValue());
            model.addEntry(newEntry);
        });
    }

    @FXML
    public void saveTableView() {
        System.out.println("SAVING");
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("LearningUnitEntries");
            for (LearningUnitEntry learningUnitEntry : learningUnitTable.getItems()) {
                Element entry = document.createElement("LearningUnitEntry");
                Element name = document.createElement("name");
                Element path = document.createElement("path");

                name.setTextContent(learningUnitEntry.getLearningUnitTitle());
                path.setTextContent(learningUnitEntry.getLearningUnitPath());
                entry.appendChild(name);
                entry.appendChild(path);

                rootElement.appendChild(entry);
            }
            document.appendChild(rootElement);
            XMLUtils.writeXML(document, "learningUnitTable.xml");
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
