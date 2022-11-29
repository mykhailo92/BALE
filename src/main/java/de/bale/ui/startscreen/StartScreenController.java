package de.bale.ui.startscreen;

import de.bale.Utils;
import de.bale.language.Localizations;
import de.bale.settings.SettingsController;
import de.bale.ui.learningUnit.LearningUnitController;
import de.bale.ui.learningUnit.LearningUnitModel;
import de.bale.ui.SceneHandler;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

    private void populateLearningUnitTable() {
        Document document = Utils.readXML(String.valueOf(this.getClass().getResource("learningUnitTable.xml")));
        NodeList learningUnitEntries = document.getElementsByTagName("LearningUnitEntry");
        for (int i = 0; i < learningUnitEntries.getLength(); i++) {
            Node item = learningUnitEntries.item(i);
            Element xmlElement = (Element) item;
            String name = xmlElement.getElementsByTagName("name").item(0).getTextContent();
            String path = xmlElement.getElementsByTagName("path").item(0).getTextContent();
            model.addEntry(name,path);
        }
    }

    @FXML
    private void initialize() {
        createLocalizedLabels();
        learningUnitTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void createLocalizedLabels() {
        nameColumn.setText(Localizations.getLocalizedString("nameColumn"));
        pathColumn.setText(Localizations.getLocalizedString("pathColumn"));
        openLearningUnitButton.setText(Localizations.getLocalizedString("openLearningUnit"));
        openSettingsButton.setText(Localizations.getLocalizedString("openSettingsButton"));
    }

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

    public void changeLanguage() {
        String language = Localizations.getInstance().getLocale().getLanguage();
        switch (language) {
            case "de" -> Localizations.getInstance().setLocale("en", "US");
            case "en" -> Localizations.getInstance().setLocale("de", "DE");
        }
        createLocalizedLabels();
    }

    @FXML
    public void openSettings() {
        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.changeScene(new SettingsController(), "settings.fxml", "settingsTitle");
        sceneHandler.setStageFullScreen(false);
    }
}
