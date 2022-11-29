package de.bale.language;

import de.bale.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Implement Localizations as a Singleton so not every Class has to create a new Object
 * Localizations are located in resources/localization/Lang_[LANGUAGECODE]_[COUNTRYCODE].properties
 */
public class Localizations {
    private static ResourceBundle bundle;
    private static final Localizations instance = new Localizations();
    private ArrayList<String> allowedLocales = new ArrayList<>() {{
        add("en_US");
        add("de_DE");
    }};


    public static Localizations getInstance() {
        return instance;
    }

    /**
     * Set the Default Locale vor Localization and Load the ResourceBundle.
     *
     * @param languageCode Identifier of the language in lower case, e.g. en, de
     * @param countryCode  Identifier of the Country in upper case, e.g. US, DE
     */
    public void setLocale(String languageCode, String countryCode) {
        if (allowedLocales.contains(languageCode + "_" + countryCode)) {
            Locale.setDefault(new Locale(languageCode, countryCode));
            bundle = ResourceBundle.getBundle("localization/Lang");
        } else {
            System.err.println("UNKOWN LOCATION: " + languageCode + countryCode);
        }

    }

    public Locale getLocale() {
        return Locale.getDefault();
    }

    public ArrayList<String> getAvailableLanguages() {
        return allowedLocales;
    }

    /**
     * Get a localized String from the Current Locale
     *
     * @param key Name of the Key in the .properties Files
     * @return Localized Name of the Key, returns empty String if an Error accured.
     */
    public static String getLocalizedString(String key) {
        try {
            if (bundle != null) {
                return bundle.getString(key);
            } else {
                System.err.println("Locale not yet set.");
                return "";
            }
        } catch (MissingResourceException e) {
            System.err.println("Missing Key: " + key + " in language: " + Locale.getDefault());
        }
        return "";

    }

    public void loadLanguage() {
        Properties properties = Utils.getSettingsProperties();
        String[] langProperty = properties.getProperty("language").split("_");
        Localizations.getInstance().setLocale(langProperty[0], langProperty[1]);
    }
}
