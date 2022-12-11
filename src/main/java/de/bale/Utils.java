package de.bale;

public class Utils {

    /**
     * @return The Resourcepath of settings.properties
     */
    public static String getSettingsDir() {
        return System.getProperty("user.dir") + "/bale/";

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
