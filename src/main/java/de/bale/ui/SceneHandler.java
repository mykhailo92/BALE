package de.bale.ui;

import de.bale.Utils;
import de.bale.eyetracking.Eyetracker;
import de.bale.language.Localizations;
import de.bale.logger.Logger;
import de.bale.messages.ErrorMessage;
import de.bale.messages.InitMessage;
import de.bale.storage.PropertiesUtils;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;


public class SceneHandler {

    private FXMLLoader fxmlLoader;
    private Stage primaryStage;
    private static SceneHandler instance;
    private String themeName;
    private Scene primaryScene;
    private final Eyetracker eyetracker;
    private final ArrayList<String> allowedThemes = new ArrayList<>() {{
        add("default");
        add("darcula");
    }};

    private SceneHandler() {
        //Start the Eyetracker and register a new Listener to the Logger
        eyetracker = new Eyetracker();
        eyetracker.startRunning();
    }

    public static SceneHandler getInstance() {
        if (instance == null) {
            instance = new SceneHandler();
            Properties properties = PropertiesUtils.getSettingsProperties();
            instance.setThemeName(properties.getProperty("theme"));
            instance.closeAllOnExit();
        }
        return instance;
    }

    public boolean eyetrackerIsRunning() {
        return eyetracker.isRunning();
    }
    private void closeAllOnExit() {
        Platform.runLater(() -> primaryStage.setOnCloseRequest(e -> {
            eyetracker.destroyProcess();
            Platform.exit();
            System.exit(0);
        }));
    }

    /**
     * Changes the primaryScene of the primaryStage
     *
     * @param controller Controller Object, e.g. LearningUnitController
     * @param fxmlName   Name of the associated .fxml File, e.g. learningUnit.fxml
     * @param titleKey   Key of the Title in the localization
     */
    public void changeScene(Object controller, String fxmlName, String titleKey) {
        if (primaryStage == null) {
            System.err.println("Primary Stage not defined!");
            return;
        }
        fxmlLoader = new FXMLLoader(controller.getClass().getResource(fxmlName));
        fxmlLoader.setController(controller);
        fxmlLoader.setLocation(controller.getClass().getResource(fxmlName));
        try {
            primaryScene = new Scene(fxmlLoader.load());
            primaryScene.getStylesheets().add(Utils.getStylesheetPath(themeName));
            primaryScene.getStylesheets().add(Utils.getStylesheetPath("fxmlStyle"));
            primaryStage.setTitle(Localizations.getLocalizedString(titleKey));
            primaryStage.setScene(primaryScene);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        primaryStage.show();
    }

    /**
     * Sets the Themename an updated the primaryScene to use the Theme
     *
     * @param themeName Name of the Theme, must be added in the allowedThemes List!
     */
    public void setThemeName(String themeName) {
        if (allowedThemes.contains(themeName)) {
            this.themeName = themeName;
            Logger.getInstance().post(new InitMessage("Setting Theme: " + themeName));
        } else {
            Logger.getInstance().post(new ErrorMessage("Themename:" + themeName + " is unknown! Loading Default Theme"));
            this.themeName = "default";
        }
        if (primaryScene != null) {
            primaryScene.getStylesheets().add(Utils.getStylesheetPath(themeName));
        }
    }

    public String getThemeName() {
        return themeName;
    }

    public ArrayList<String> getAllowedThemes() {
        return allowedThemes;
    }

    public void setStageFullScreen(boolean fulLScreenEnabled) {
        Logger.getInstance().post(new InitMessage("Setting Fullscreen to: " + fulLScreenEnabled));
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
