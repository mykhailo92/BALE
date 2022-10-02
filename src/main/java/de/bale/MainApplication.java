package de.bale;

import de.bale.language.Localizations;
import de.bale.ui.LearningUnitController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Localizations.setLocale("de", "DE");
        LearningUnitController defaultController = new LearningUnitController(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}