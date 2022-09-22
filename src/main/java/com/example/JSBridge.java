package com.example;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;

public class JSBridge {

    private WebEngine engine;

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
        JSObject bridgeCreator = (JSObject) engine.executeScript("window");
        bridgeCreator.setMember("javaBridge", new Bridge());
    }
}
