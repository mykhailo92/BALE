package de.bale.ui.startscreen;

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
    Button changeLanguageButton;
    @FXML
    Button openSettingsButton;
    IStartScreenModel model;

    //TODO TEMPORARY FIX FOR TABLE
    private LearningUnitEntry exampleEntry;

    //TODO: Find a Way to Save the table
    public StartScreenController(String learningUnitTitle, String filepath) {
        exampleEntry = new LearningUnitEntry(learningUnitTitle, filepath);
    }

    @Override
    public void setModel(IStartScreenModel model) {
        this.model = model;
        Platform.runLater(() -> {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("learningUnitTitle"));
            pathColumn.setCellValueFactory(new PropertyValueFactory<>("learningUnitPath"));
            model.addEntry(this.exampleEntry);
            learningUnitTable.setItems(model.getEntries());
        });
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
        changeLanguageButton.setText(Localizations.getLocalizedString("changeLanguageButton"));
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
        sceneHandler.changeScene(new SettingsController(), "settings.fxml", "title");
//        ILearningUnitModel learningUnitModel = new LearningUnitModel();
//        ((LearningUnitController) sceneHandler.getController()).setModel(learningUnitModel);
        sceneHandler.setStageFullScreen(false);
    }
}
