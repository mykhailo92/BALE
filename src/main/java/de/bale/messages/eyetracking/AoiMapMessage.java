package de.bale.messages.eyetracking;

import de.bale.logger.Logger;

import java.util.Map;

public class AoiMapMessage {
    String jsonString;

    public AoiMapMessage(Map<String, Long> aoiMap) {
        StringBuilder returnString = new StringBuilder();
        jsonString = "{";
        for (String key : aoiMap.keySet()) {
            returnString.append("Key: ").append(key).append(" View Duration: ").append(aoiMap.get(key)).append(" ");
            jsonString += "\"" + key + "\": \"" + aoiMap.get(key) + "\",";
        }
        if (jsonString.substring(jsonString.length() - 1).equals(",")) {
            jsonString = jsonString.substring(0, jsonString.length() - 1);
        }
        jsonString += "}";
        Logger.getInstance().post(new AoiStringMessage(returnString.toString()));
        Logger.getInstance().post(new AoiStringMessage(jsonString));
    }
}
