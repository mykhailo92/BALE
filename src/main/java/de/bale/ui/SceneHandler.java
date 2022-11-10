package de.bale.ui;

import de.bale.language.Localizations;
import de.bale.ui.interfaces.IController;
import de.bale.ui.interfaces.ILearningUnitModel;
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

    public void changeScene(IController controller, String fxmlName, String titleKey) throws IOException {
        if (primaryStage == null) {
            System.err.println("Primary Stage not defined!");
            return;
        }
        fxmlLoader = new FXMLLoader(controller.getClass().getResource(fxmlName));
        fxmlLoader.setController(controller);
        Scene primaryScene = new Scene(fxmlLoader.load());
        ILearningUnitModel learningUnitModel = new LearningUnitModel();
        controller.setModel(learningUnitModel);
        primaryStage.setTitle(Localizations.getLocalizedString(titleKey));
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    public void setStageFullScreen(boolean fulLScreenEnabled) {
        primaryStage.setFullScreen(fulLScreenEnabled);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
    }
}
