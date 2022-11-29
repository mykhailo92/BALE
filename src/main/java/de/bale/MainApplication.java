package de.bale;

import de.bale.language.Localizations;
import de.bale.ui.SceneHandler;
import de.bale.ui.startscreen.StartScreenController;
import de.bale.ui.startscreen.StartScreenModel;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class MainApplication extends Application {
    static String filePath = "";

    @Override
    public void start(Stage stage) {
        Localizations.getInstance().loadLanguage();
        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.setStage(stage);
        sceneHandler.setThemeName("default");
        sceneHandler.changeScene(new StartScreenController(), "startScreen.fxml", "title");
        ((StartScreenController) sceneHandler.getController()).setModel(new StartScreenModel());
        sceneHandler.setStageFullScreen(false);
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