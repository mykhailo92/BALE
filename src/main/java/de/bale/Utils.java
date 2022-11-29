package de.bale;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

public class Utils {

    public static String getSettingsPath() {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource("settings.properties");
        return resource.toString().replace("file:/", "");
    }

    public static void writeSettingsProperty(Properties settings) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource("settings.properties");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(resource.toString().replace("file:/", ""));
            settings.store(fileOutputStream, "");
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public static Properties getSettingsProperties() {
        Properties properties = new Properties();
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(Utils.getSettingsPath());
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public static String getStylesheet(String themeName) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        String resource = classLoader.getResource("css").toExternalForm();
        return (resource + themeName + ".css");
    }
}
