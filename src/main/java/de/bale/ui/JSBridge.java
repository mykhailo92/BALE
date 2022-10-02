package de.bale.ui;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;

public class JSBridge {

    private final WebEngine engine;

    /**
     * Public Class which holds function that can be called from JavaScript
     */
    public class Bridge {
        public void disablePreamble() {
            try {
                Element preamble = engine.getDocument().getElementById("preamble");
                preamble.setAttribute("style", "display:none");
            } catch (NullPointerException npe) {
                System.err.println("Preamble not found!");
            }
        }
    }

    JSBridge(WebEngine engine) {
        this.engine = engine;
    }

    /**
     * Register a JSBridge, which can be any public Class
     *
     * @param bridgeName Name by which the Bridge can be called from JS
     */
    void registerBridge(String bridgeName) {
        JSObject bridgeCreator = (JSObject) engine.executeScript("window");
        bridgeCreator.setMember(bridgeName, new Bridge());
    }
}
