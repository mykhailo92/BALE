package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class SceneChangeMessage extends AbstractMessage {
    public SceneChangeMessage(String newScene) {
        super("Opening new Scene: " + newScene, 1);
    }
}
