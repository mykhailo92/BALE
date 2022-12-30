package de.bale.storage;

import de.bale.Utils;
import de.bale.logger.Logger;
import de.bale.messages.InitMessage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtils extends Utils {


    /**
     * Writes a Property Object to a .properties File which is placed in the highest resources folder
     *
     * @param property     Propertyobject which will be written.
     * @param propertyName Name of the Propertyfile without file ending, e.g. settings
     */
    public static void writeProperty(Properties property, String propertyName) {
        try {
            OutputStream fileOutputStream = new FileOutputStream(getSettingsDir() + propertyName + ".properties");
            Logger.getInstance().post(new InitMessage("Saving Property: " + getSettingsDir() + propertyName + ".properties"));
            property.store(fileOutputStream, "");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            System.err.println("File not found: " + getSettingsDir() + "/settings.properties. Creating default Settings!");
            createDefaultSettingsProperties();
        }
        return properties;
    }

    /**
     * +
     * Creates and Writes a default settings.properties
     */
    private static void createDefaultSettingsProperties() {
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty("language", "de_DE");
        defaultProperties.setProperty("theme", "default");
        writeProperty(defaultProperties, "settings");
    }
}
