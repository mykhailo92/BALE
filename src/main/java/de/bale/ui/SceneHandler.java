package de.bale.ui;

import de.bale.Utils;
import de.bale.language.Localizations;
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
    private ArrayList<String> allowedThemes = new ArrayList<>() {{
        add("default");
        add("darcula");
    }};

    private SceneHandler() {
    }

    public static SceneHandler getInstance() {
        if (instance == null) {
            instance = new SceneHandler();
            Properties properties = Utils.getSettingsProperties();
            instance.setThemeName(properties.getProperty("theme"));
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
            primaryScene = new Scene(fxmlLoader.load());
            primaryScene.getStylesheets().add(Utils.getStylesheet(themeName));
            primaryScene.getStylesheets().add(Utils.getStylesheet("fxmlStyle"));
            primaryStage.setTitle(Localizations.getLocalizedString(titleKey));
            primaryStage.setScene(primaryScene);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        primaryStage.show();
    }

    public void setThemeName(String themeName) {
        if (allowedThemes.contains(themeName)) {
            this.themeName = themeName;
            if (primaryScene != null) {
                primaryScene.getStylesheets().add(Utils.getStylesheet(themeName));
            }
        } else {
            System.err.println("Themename:" + themeName + " is unknown! Loading Default Theme");
            this.themeName = "default";
        }
    }

    public String getThemeName() {
        return themeName;
    }

    public ArrayList<String> getAllowedThemes() {
        return allowedThemes;
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
