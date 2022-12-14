package de.bale;

import de.bale.language.Localizations;
import de.bale.ui.SceneHandler;
import de.bale.ui.startscreen.StartScreenController;
import de.bale.ui.startscreen.StartScreenModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) {
        Localizations.getInstance().loadLanguage();
        SceneHandler sceneHandler = SceneHandler.getInstance();
        sceneHandler.setStage(stage);
        sceneHandler.changeScene(new StartScreenController(), "startScreen.fxml", "selectionTitle");
        ((StartScreenController) sceneHandler.getController()).setModel(new StartScreenModel());
        sceneHandler.setStageFullScreen(false);
    }

    public static void main(String[] args) {
        launch();
    }
}