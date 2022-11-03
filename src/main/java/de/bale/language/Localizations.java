package de.bale.language;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Implement Localizations as a Singleton so not every Class has to create a new Object
 * Localizations are located in resources/localization/Lang_[LANGUAGECODE]_[COUNTRYCODE].properties
 */
public class Localizations {
    private static ResourceBundle bundle;
    private static final Localizations instance = new Localizations();

    public static Localizations getInstance() {
        return instance;
    }

    /**
     * Set the Default Locale vor Localization and Load the ResourceBundle.
     * @param languageCode Identifier of the language in lower case, e.g. en, de
     * @param countryCode Identifier of the Country in upper case, e.g. US, DE
     *                    TODO NON STATIC
     */
    public static void setLocale(String languageCode, String countryCode) {
        Locale.setDefault(new Locale(languageCode,countryCode));
        bundle = ResourceBundle.getBundle("localization/Lang");
    }

    /**
     * Get a localized String from the Current Locale
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
            System.err.println("Missing Key: "+key+" in language: "+ Locale.getDefault());
        }
        return "";

    }
}
