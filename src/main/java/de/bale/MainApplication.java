package de.bale;

import de.bale.language.Localizations;
import de.bale.ui.interfaces.IController;
import de.bale.ui.interfaces.ILearningUnitModel;
import de.bale.ui.LearningUnitController;
import de.bale.ui.LearningUnitModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    String fxmlName = "hello-view.fxml";
    String title = "title";

    @Override
    public void start(Stage stage) throws IOException {
        Localizations.setLocale("de", "DE");
        FXMLLoader fxmlLoader = new FXMLLoader(LearningUnitController.class.getResource(fxmlName));
        Scene scene = new Scene(fxmlLoader.load());
        //Create Default Model and Controller and set the Model for the controller
        ILearningUnitModel learningUnitModel = new LearningUnitModel();
        IController learningUnitController = fxmlLoader.getController();
        learningUnitController.setModel(learningUnitModel);
        //Enable FullScreen and remove the Ability to close it with ESC (Default)
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setTitle(Localizations.getLocalizedString(title));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}