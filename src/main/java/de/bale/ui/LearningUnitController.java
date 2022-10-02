package de.bale.ui;

import de.bale.MainApplication;
import de.bale.language.Localizations;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class LearningUnitController {

    String fxmlName = "hello-view.fxml";

    public LearningUnitController(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource(fxmlName));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setTitle(Localizations.getLocalizedString("title"));
        stage.setScene(scene);
        stage.show();

    }
}
