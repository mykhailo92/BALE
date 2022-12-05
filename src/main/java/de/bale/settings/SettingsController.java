package de.bale.settings;


import de.bale.language.Localizations;
import de.bale.settings.interfaces.ISettingsController;
import de.bale.storage.PropertiesUtils;
import de.bale.ui.SceneHandler;
import de.bale.ui.startscreen.StartScreenController;
import de.bale.ui.startscreen.StartScreenModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Properties;

public class SettingsController implements ISettingsController {

    @FXML
    Label languageLabel;
    @FXML
    ComboBox languageComboBox;
    @FXML
    Button saveButton;
    @FXML
    Button openStartScreenButton;
    @FXML
    Label themeLabel;
    @FXML
    ComboBox themeCombobox;

    @FXML
    private void initialize() {
        createLocalizedLabels();
        Localizations locale = Localizations.getInstance();
        ArrayList<String> availableLanguages = locale.getAvailableLanguages();
        ObservableList<String> comboBoxList = FXCollections.observableArrayList(availableLanguages);
        languageComboBox.setItems(comboBoxList);
        languageComboBox.setValue(locale.getLocale());

        themeCombobox.setItems(FXCollections.observableArrayList(SceneHandler.getInstance().getAllowedThemes()));
        themeCombobox.setValue(SceneHandler.getInstance().getThemeName());
    }

    private void createLocalizedLabels() {
        languageLabel.setText(Localizations.getLocalizedString("language"));
        saveButton.setText(Localizations.getLocalizedString("saveButton"));
        openStartScreenButton.setText(Localizations.getLocalizedString("openStartScreenButton"));
        themeLabel.setText(Localizations.getLocalizedString("themeLabel"));
    }

    @FXML @Override
    public void saveSettings() {
        String[] selectedLanguage = languageComboBox.getSelectionModel().getSelectedItem().toString().split("_");
        Localizations.getInstance().setLocale(selectedLanguage[0],selectedLanguage[1]);
        createLocalizedLabels();
        SceneHandler.getInstance().setThemeName(String.valueOf(themeCombobox.getSelectionModel().getSelectedItem()));
        writeSettings();
    }

    @Override
    public void writeSettings() {
        Properties properties = PropertiesUtils.getSettingsProperties();
        properties.setProperty("language", String.valueOf(languageComboBox.getSelectionModel().getSelectedItem()));
        properties.setProperty("theme",String.valueOf(themeCombobox.getSelectionModel().getSelectedItem()));
        PropertiesUtils.writeProperty(properties,"settings");
    }

    @FXML @Override
    public void openStartScreen() {
        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.changeScene(new StartScreenController(), "startScreen.fxml", "selectionTitle");
        ((StartScreenController) sceneHandler.getController()).setModel(new StartScreenModel());
        sceneHandler.setStageFullScreen(false);
    }
}
