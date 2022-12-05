package de.bale.settings.interfaces;

import javafx.fxml.FXML;

public interface  ISettingsController {

    @FXML
    public  void saveSettings();

    void writeSettings();

    @FXML
    public  void openStartScreen();
}
