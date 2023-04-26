package de.bale.messages.eyetracking;

import de.bale.logger.Logger;

import java.util.Map;

public class AoiMapMessage {
    public AoiMapMessage(Map<String, Long> aoiMap) {
        String returnString = "";
        for (String key : aoiMap.keySet()) {
            returnString += "Key: " + key + " View Duration: " + aoiMap.get(key) + " ";
        }
        Logger.getInstance().post(new AoiStringMessage(returnString));
    }
}
