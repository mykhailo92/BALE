package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class SettingsMessage extends AbstractMessage {
    public SettingsMessage(String key, String value) {
        super("Saving Setting: " + key + ", " + value, 0);
    }
}
