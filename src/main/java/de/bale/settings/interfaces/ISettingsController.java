package de.bale.settings.interfaces;

import javafx.fxml.FXML;

public abstract class ISettingsController {

    @FXML
    public abstract void saveSettings();

    protected abstract void writeSettings();

    @FXML
    public abstract void openStartScreen();
}
