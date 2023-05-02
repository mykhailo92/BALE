package de.bale.messages.eyetracking;

import java.util.List;
import java.util.Map;

/**
 * Currently unused.
 * This Message converts the AoIMap to an JSON String in Case it is needed in the Future
 */
public class AoiMapAsJasonMessage {
    String jsonString;

    public AoiMapAsJasonMessage(Map<String, List<Long>> aoiMap) {
        StringBuilder returnString = new StringBuilder();
        jsonString = "{";
        for (String key : aoiMap.keySet()) {
            returnString.append("Key: ").append(key).append(" View Duration: ").append(aoiMap.get(key).get(0)).append(" ");
            jsonString += "\"" + key + "\": \"" + aoiMap.get(key) + "\",";
        }

        if (jsonString.substring(jsonString.length() - 1).equals(",")) {
            jsonString = jsonString.substring(0, jsonString.length() - 1);
        }
        jsonString += "}";
    }
}
