package de.bale;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class Utils {

    /**
     * @return The Resourcepath of settings.properties
     */
    private static String getSettingsDir() {
        return System.getProperty("user.dir")+"/bale/";

    }

    /**
     * Writes a Property Object to a .properties File which is placed in the highest resources folder
     *
     * @param property     Propertyobject which will be written.
     * @param propertyName Name of the Propertyfile without file ending, e.g. settings
     */
    public static void writeProperty(Properties property, String propertyName) {
        try {
            OutputStream fileOutputStream = new FileOutputStream(getSettingsDir() + "/settings.properties");
//            FileOutputStream fileOutputStream = new FileOutputStream(resource.toString().replace("file:/", ""));
            property.store(fileOutputStream, "");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads an XML File to return a changable DOM Document
     *
     * @param filepath Complete Filepath, e.g. (...).class.getResource(ResourceBundle("learningUnitTable.xml")
     * @return w3c DOM Document
     */
    public static Document readXML(String filepath) {
        Document domDoc;
        try {
            domDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(getSettingsDir() +"/"+ filepath);
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
            FileInputStream resource = new FileInputStream(getSettingsDir() + "/settings.properties");
            properties.load(resource);
            resource.close();
        } catch (IOException e) {
            System.err.println("File not found: " + getSettingsDir()+"/settings.properties. Creating default Settings!");
            createDefaultSettingsProperties();
        }
        return properties;
    }

    private static void createDefaultSettingsProperties() {
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty("language","de_DE");
        defaultProperties.setProperty("theme","default");
        writeProperty(defaultProperties,"settigns");
    }

    /**
     * @param themeName Name of the Stylesheet, e.g. "default" or "darcula"
     * @return Path to the .css File, which can be used by the SceneHandler
     */
    public static String getStylesheetPath(String themeName) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        String resource = classLoader.getResource("css/").toExternalForm();
        return (resource + themeName + ".css");
    }
}
