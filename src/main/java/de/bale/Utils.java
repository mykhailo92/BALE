package de.bale;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class Utils {

    public static String getSettingsPath() {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource("settings.properties");
        return resource.toString().replace("file:/", "");
    }

    public static Document readXML(String filepath) {
        Document domDoc;
        try {
            domDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filepath);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return domDoc;
    }

    public static String getStylesheet(String themeName) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        String resource = classLoader.getResource("css").toExternalForm();
        return (resource + themeName + ".css");
    }
}
