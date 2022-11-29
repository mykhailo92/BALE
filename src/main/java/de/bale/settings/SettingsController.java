package de.bale.settings;

import de.bale.language.Localizations;
import de.bale.settings.interfaces.ISettingsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;

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

}
