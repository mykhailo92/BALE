package de.bale;

import de.bale.language.Localizations;
import de.bale.ui.LearningUnitModel;
import de.bale.ui.SceneHandler;
import de.bale.ui.interfaces.IController;
import de.bale.ui.interfaces.ILearningUnitModel;
import de.bale.ui.LearningUnitController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    String fxmlName = "learningUnit.fxml";
    static String filePath = "";

    @Override
    public void start(Stage stage) throws IOException {
        Localizations.setLocale("de", "DE");
        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.setStage(stage);
        sceneHandler.setStageFullScreen(true);
        sceneHandler.changeScene(new LearningUnitController(filePath),fxmlName,"title");
    }


    public static void main(String[] args) {
        if (args.length > 0) {
            filePath = args[0];
            launch();
        } else {
            System.err.println("Bitte Dateipfad zu der .html Datei angeben!");
            System.exit(1);
        }

    }
}