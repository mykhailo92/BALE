package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import java.io.File;

public class HelloController {
    public Button getSections;
    @FXML
    private WebView learningUnit = new WebView();
    WebEngine engine;
    File file = new File(String.valueOf(getClass().getResource("/com/example/example.html")));
    @FXML
    Button changeColor;

    @FXML
    private void initialize() {
        engine = learningUnit.getEngine();
        engine.load(file.toString());
    }

    @FXML
    private void getSections() {
        JSObject sectionList = (JSObject) engine.executeScript("getSections();");
        System.out.println(sectionList.getSlot(1));
    }

    @FXML
    private void changeColorFunction(ActionEvent event) {
        Object b = engine.executeScript("changeBgColor();");
        System.out.println(b.getClass());
    }
}