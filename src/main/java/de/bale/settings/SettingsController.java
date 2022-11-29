package de.bale.settings;


import de.bale.XMLUtils;
import de.bale.language.Localizations;
import de.bale.settings.interfaces.ISettingsController;
import de.bale.ui.SceneHandler;
import de.bale.ui.startscreen.StartScreenController;
import de.bale.ui.startscreen.StartScreenModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.w3c.dom.Document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

public class SettingsController extends ISettingsController {

    @FXML
    Label languageLabel;
    @FXML
    ComboBox languageComboBox;
    @FXML
    Button saveButton;
    @FXML
    Button openStartScreenButton;

    @FXML
    private void initialize() {
        createLocalizedLabels();
        Localizations locale = Localizations.getInstance();
        ArrayList<String> availableLanguages = locale.getAvailableLanguages();
        ObservableList<String> comboBoxList = FXCollections.observableArrayList(availableLanguages);
        languageComboBox.setItems(comboBoxList);
        languageComboBox.setValue(locale.getLocale());
    }

    private void createLocalizedLabels() {
        languageLabel.setText(Localizations.getLocalizedString("language"));
        saveButton.setText(Localizations.getLocalizedString("saveButton"));
        openStartScreenButton.setText(Localizations.getLocalizedString("openStartScreenButton"));
    }

    @FXML
    public void saveSettings() {
        String[] selectedLanguage = languageComboBox.getSelectionModel().getSelectedItem().toString().split("_");
        Localizations.getInstance().setLocale(selectedLanguage[0],selectedLanguage[1]);
        createLocalizedLabels();
        writeSettings();
    }

    private void writeSettings() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL resource = classLoader.getResource("settings.properties");
        String filepath = resource.toString().replace("file:/", "");
        Properties properties = new Properties();
        properties.setProperty("language", String.valueOf(languageComboBox.getSelectionModel().getSelectedItem()));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            properties.store(fileOutputStream, "");
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void openStartScreen() {
        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.changeScene(new StartScreenController(), "startScreen.fxml", "title");
        ((StartScreenController) sceneHandler.getController()).setModel(new StartScreenModel());
        sceneHandler.setStageFullScreen(false);
    }
}
