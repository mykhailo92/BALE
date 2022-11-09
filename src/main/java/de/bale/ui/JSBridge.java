package de.bale.ui;

import de.bale.ui.interfaces.ILearningUnitModel;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.web.WebEngine;
import javafx.scene.media.MediaPlayer;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;

import java.nio.file.Paths;

public class JSBridge {

    private final WebEngine engine;
    private final ILearningUnitModel model;
    private final Bridge jsBridge;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;


    /**
     * Public Class which holds function that can be called from JavaScript
     */
    public class Bridge {

        public void disablePreamble() {
            try {
                Platform.runLater(() -> {
                    Element preamble = engine.getDocument().getElementById("preamble");
                    preamble.setAttribute("style", "display:none");
                    model.setContainerIndicator(model.getContainerIndicator() + 1);
                    model.setNextButtonDisabled(false);
                });
            } catch (NullPointerException npe) {
                System.err.println("Preamble not found!");
            }
        }

        public void playAudio(int index) {
            if (isPlaying) {
                mediaPlayer.stop();
                isPlaying = false;
                switch (index) {
                    case 0 -> editInnerTextByID("reading-button-0", "Vorlesen");
                    case 1 -> editInnerTextByID("reading-button-1", "Vorlesen");
                    case 2 -> editInnerTextByID("reading-button-2", "Vorlesen");
                    case 3 -> editInnerTextByID("reading-button-3", "Vorlesen");
                    case 4 -> editInnerTextByID("reading-button-4", "Vorlesen");
                }
               return;
            }
            try {
                switch (index) {
                    case 0 -> {
                        mediaPlayer = new MediaPlayer(new Media(Paths.get("Lerneinheit/audio/task_0.mp3")
                                .toUri().toString()));
                        mediaPlayer.play();
                        editInnerTextByID("reading-button-0", "Vorlesen stoppen");
                        isPlaying = true;
                    }
                    case 1 -> {
                        mediaPlayer = new MediaPlayer(new Media(Paths.get("Lerneinheit/audio/task_1.1.mp3")
                                .toUri().toString()));
                        mediaPlayer.play();
                        editInnerTextByID("reading-button-1", "Vorlesen stoppen");
                        isPlaying = true;
                    }
                    case 2 -> {
                        mediaPlayer = new MediaPlayer(new Media(Paths.get("Lerneinheit/audio/task_1.2.mp3")
                                .toUri().toString()));
                        mediaPlayer.play();
                        editInnerTextByID("reading-button-2", "Vorlesen stoppen");
                        isPlaying = true;
                    }
                    case 3 -> {
                        mediaPlayer = new MediaPlayer(new Media(Paths.get("Lerneinheit/audio/task_2.mp3")
                                .toUri().toString()));
                        mediaPlayer.play();
                        editInnerTextByID("reading-button-3", "Vorlesen stoppen");
                        isPlaying = true;
                    }
                    case 4 -> {
                        mediaPlayer = new MediaPlayer(new Media(Paths.get("Lerneinheit/audio/task_3.mp3")
                                .toUri().toString()));
                        mediaPlayer.play();
                        editInnerTextByID("reading-button-4", "Vorlesen stoppen");
                        isPlaying = true;
                    }
                }
            } catch (Exception e) {
                System.err.println("Audio not found!");
            }
        }
    }

    JSBridge(ILearningUnitModel model, WebEngine engine) {
        this.model = model;
        this.engine = engine;
        this.jsBridge = new Bridge();
    }

    /**
     * Register a JSBridge, which can be any public Class
     */
    void registerBridge() {
        JSObject window = (JSObject) engine.executeScript("window");
        window.setMember("javaBridge", jsBridge);
    }

    void editInnerTextByID (String id, String text) {
        try {
            engine.executeScript("changeInnerTextById('" + id + "','" + text + "')");
        } catch (Exception e) {
            System.err.println("Error by edit of inner text");
        }
    }
}
