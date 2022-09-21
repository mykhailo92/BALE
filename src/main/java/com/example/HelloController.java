package com.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import java.io.File;

public class HelloController {
    @FXML
    public Button nextButton;
    @FXML
    public Button closeButton;
    @FXML
    private WebView learningUnit = new WebView();
    WebEngine engine;
    File file = new File(String.valueOf(getClass().getResource("/com/example/example.html")));

    /**
     * Starte die Webengine und lade das Beispiel
     */
    @FXML
    private void initialize() {
        engine = learningUnit.getEngine();
        engine.load(file.toString());
    }

    /**
     * Executes JavaScript to get and prepare a List of all Sections in the Learningunit
     * @return List of all Section in the current Learningunit in order of appierence
     */
    @FXML
    private JSObject getSections() {
        JSObject sectionList = (JSObject) engine.executeScript("getSections();");
        System.out.println(sectionList.getSlot(1));
        return sectionList;
    }

    /**
     * Temporary fix to close the App in Fullscreenmode
     */
    @FXML
    private void closeApp() {
        Platform.exit();
    }
}