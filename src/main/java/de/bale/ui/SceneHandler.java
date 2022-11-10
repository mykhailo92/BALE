package de.bale.ui;

import de.bale.language.Localizations;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneHandler {

    FXMLLoader fxmlLoader;
    Stage primaryStage;
    static SceneHandler instance;

    private SceneHandler() {
    }

    public static SceneHandler getInstance() {
        if (instance == null) {
            instance = new SceneHandler();
        }
        return instance;
    }

    public void changeScene(Object controller, String fxmlName, String titleKey) {
        if (primaryStage == null) {
            System.err.println("Primary Stage not defined!");
            return;
        }
        fxmlLoader = new FXMLLoader(controller.getClass().getResource(fxmlName));
        fxmlLoader.setController(controller);
        fxmlLoader.setLocation(controller.getClass().getResource(fxmlName));
        try {
            Scene primaryScene = new Scene(fxmlLoader.load());
            ;
            primaryStage.setTitle(Localizations.getLocalizedString(titleKey));
            primaryStage.setScene(primaryScene);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        primaryStage.show();
    }

    public void setStageFullScreen(boolean fulLScreenEnabled) {
        primaryStage.setFullScreen(fulLScreenEnabled);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    public Object getController() {
        return fxmlLoader.getController();
    }
}
