package de.bale.language;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Implement Localizations as a Singleton so not every Class has to create a new Object
 * Localizations are located in resources/localization/lang_[LANGUAGECODE]_[COUNTRYCODE].properties
 */
public class Localizations {
    private static ResourceBundle bundle;
    private static final Localizations instance = new Localizations();

    public static Localizations getInstance() {
        return instance;
    }

    public static void setLocale(String languageCode, String countryCode) {
        Locale.setDefault(new Locale(languageCode,countryCode));
        bundle = ResourceBundle.getBundle("localization/Lang");
    }

    public static String getLocalizedString(String key) {
        try {
            if (bundle != null) {
                return bundle.getString(key);
            }
        } catch (MissingResourceException e) {
            e.printStackTrace();
            System.err.println("Missing Key: "+key+" in language: "+ Locale.getDefault());
        }
        return "";

    }
}
