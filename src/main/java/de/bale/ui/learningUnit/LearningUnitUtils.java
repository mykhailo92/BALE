package de.bale.ui.learningUnit;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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
}
