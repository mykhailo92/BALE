package de.bale.ui.learningUnit;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LearningUnitUtils {
    /**
     * @param element HTML DOM Element
     * @return String list of all HTML Classes that element has. Returns "none" when the Element has no HTML CLass.
     */
    public static List<String> getClasses(Element element) {
        NamedNodeMap elementAttributes = element.getAttributes();
        if (elementAttributes.getNamedItem("class") != null) {
            return Arrays.asList(elementAttributes.getNamedItem("class").getNodeValue().split(" "));
        }
        return Collections.singletonList("none");
    }

    /**
     * @param node HTML DOM Node
     * @return String list of all HTML Classes that element has. Returns "none" when the Element has no HTML CLass.
     */
    public static List<String> getClasses(Node node) {
        NamedNodeMap elementAttributes = node.getAttributes();
        if ((elementAttributes != null) && (elementAttributes.getNamedItem("class") != null)) {
            return Arrays.asList(elementAttributes.getNamedItem("class").getNodeValue().split(" "));
        }
        return Collections.singletonList("none");
    }

    /**
     * Returns the ID of a Node
     *
     * @param node node which to get the ID from
     * @return String with the ID, "none" if no ID is specified
     */
    public static String getID(Node node) {
        NamedNodeMap elementAttributes = node.getAttributes();
        if ((elementAttributes != null) && (elementAttributes.getNamedItem("id") != null)) {
            return elementAttributes.getNamedItem("id").getNodeValue();
        }
        return "none";
    }

    public static void createStyleNode(WebEngine engine, String name) {
        Document doc = engine.getDocument();
        Element styleNode = doc.createElement("link");
        styleNode.setAttribute("rel","stylesheet");
        styleNode.setAttribute("href","css/"+name+".css");
        doc.getDocumentElement().getElementsByTagName("head").item(0).appendChild(styleNode);
    }


    /**
     * Casts a JavaScript response Object to an Array of Elements
     *
     * @param jsObjectList JSObject which holds a List-structure
     * @return Array of Elements which were held by the jsObjectList
     */
    static Element[] createArrayFromJSObject(JSObject jsObjectList) {
        int slotCounter = 0;
        ArrayList<Element> elementList = new ArrayList<>();
        while (!jsObjectList.getSlot(slotCounter).equals("undefined")) {
            Element element = (Element) jsObjectList.getSlot(slotCounter++);
            elementList.add(element);
        }
        Element[] elementArray = new Element[elementList.size()];
        for (int i = 0; i < elementArray.length; i++) {
            elementArray[i] = elementList.get(i);
        }
        return elementArray;
    }
}
