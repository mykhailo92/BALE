package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class HelloController {
    public Button getSections;
    @FXML
    private WebView learningUnit =new WebView();
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
        Object b = engine.executeScript("getSections();");
        System.out.println(b.getClass());
    }
    @FXML
    private void changeColorFunction(ActionEvent event) {
        engine.executeScript("changeBgColor();");
    }
}