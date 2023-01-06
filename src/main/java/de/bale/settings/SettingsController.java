package de.bale.settings;


import de.bale.language.Localizations;
import de.bale.logger.Logger;
import de.bale.messages.InitMessage;
import de.bale.messages.SceneChangeMessage;
import de.bale.messages.SettingsMessage;
import de.bale.messages.TaskDoneMessage;
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
import javafx.scene.control.Slider;

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
    Label loglevelLabel;
    @FXML
    Slider loglevelSlider;
    @FXML
    Label loglevelExplanation;

    @FXML
    private void initialize() {
        Properties settings = PropertiesUtils.getSettingsProperties();
        loglevelSlider.setValue(Double.parseDouble(settings.getProperty("loglevel")));

        createLocalizedLabels();
        Localizations locale = Localizations.getInstance();
        ArrayList<String> availableLanguages = locale.getAvailableLanguages();
        ObservableList<String> comboBoxList = FXCollections.observableArrayList(availableLanguages);
        languageComboBox.setItems(comboBoxList);
        languageComboBox.setValue(locale.getLocale());

        themeCombobox.setItems(FXCollections.observableArrayList(SceneHandler.getInstance().getAllowedThemes()));
        themeCombobox.setValue(SceneHandler.getInstance().getThemeName());

        loglevelSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            loglevelExplanation.setText(Localizations.getLocalizedString("loglevelExplanation" + Math.round(Float.parseFloat(newValue.toString()))));
        });
    }

    private void createLocalizedLabels() {
        Logger.getInstance().post(new InitMessage("Started Creating Labels"));
        languageLabel.setText(Localizations.getLocalizedString("language"));
        saveButton.setText(Localizations.getLocalizedString("saveButton"));
        openStartScreenButton.setText(Localizations.getLocalizedString("openStartScreenButton"));
        themeLabel.setText(Localizations.getLocalizedString("themeLabel"));
        loglevelLabel.setText(Localizations.getLocalizedString("loglevelLabel"));
        loglevelExplanation.setText(Localizations.getLocalizedString("loglevelExplanation" + Math.round((loglevelSlider.getValue()))));
        Logger.getInstance().post(new TaskDoneMessage());
    }

    @FXML
    @Override
    public void saveSettings() {
        Logger.getInstance().changeLogLevel((int) Math.round((loglevelSlider.getValue())));
        Logger.getInstance().post(new SettingsMessage("loglevel", String.valueOf(Math.round((loglevelSlider.getValue())))));
        String[] selectedLanguage = languageComboBox.getSelectionModel().getSelectedItem().toString().split("_");
        Localizations.getInstance().setLocale(selectedLanguage[0], selectedLanguage[1]);
        Logger.getInstance().post(new SettingsMessage("language", languageComboBox.getSelectionModel().getSelectedItem().toString()));
        createLocalizedLabels();
        SceneHandler.getInstance().setThemeName(String.valueOf(themeCombobox.getSelectionModel().getSelectedItem()));
        Logger.getInstance().post(new SettingsMessage("theme", themeCombobox.getSelectionModel().getSelectedItem().toString()));
        writeSettings();
    }

    @Override
    public void writeSettings() {
        Properties properties = PropertiesUtils.getSettingsProperties();
        properties.setProperty("language", String.valueOf(languageComboBox.getSelectionModel().getSelectedItem()));
        properties.setProperty("theme", String.valueOf(themeCombobox.getSelectionModel().getSelectedItem()));
        properties.setProperty("loglevel", String.valueOf(Math.round((loglevelSlider.getValue()))));
        PropertiesUtils.writeProperty(properties, "settings");
    }

    @FXML
    @Override
    public void openStartScreen() {
        Logger.getInstance().post(new SceneChangeMessage("StartScreen"));
        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.changeScene(new StartScreenController(), "startScreen.fxml", "selectionTitle");
        ((StartScreenController) sceneHandler.getController()).setModel(new StartScreenModel());
        sceneHandler.setStageFullScreen(false);
    }
}
