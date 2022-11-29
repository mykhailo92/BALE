package de.bale;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Utils {

    /**
     * @return The Resourcepath of settings.properties
     */
    private static String getSettingsPath() {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource("settings.properties");
        return resource.toString().replace("file:/", "");
    }

    /**
     * Writes a Property Object to a .properties File which is placed in the highest resources folder
     * @param property Propertyobject which will be written.
     * @param propertyName Name of the Propertyfile without file ending, e.g. settings
     */
    public static void writeProperty(Properties property, String propertyName) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource(propertyName+".properties");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(resource.toString().replace("file:/", ""));
            property.store(fileOutputStream, "");
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads an XML File to return a changable DOM Document
     * @param filepath Complete Filepath, e.g. (...).class.getResource(ResourceBundle("learningUnitTable.xml")
     * @return w3c DOM Document
     */
    public static Document readXML(String filepath) {
        Document domDoc;
        try {
            domDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filepath);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return domDoc;
    }

    /**
     * @return settings.properties as Property Object
     */
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

    /**
     *
     * @param themeName Name of the Stylesheet, e.g. "default" or "darcula"
     * @return Path to the .css File, which can be used by the SceneHandler
     */
    public static String getStylesheetPath(String themeName) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        String resource = classLoader.getResource("css").toExternalForm();
        return (resource + themeName + ".css");
    }
}
