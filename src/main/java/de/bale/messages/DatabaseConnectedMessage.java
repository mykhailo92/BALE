package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class DatabaseConnectedMessage extends AbstractMessage {
    public DatabaseConnectedMessage() {
        super("Database is connected", 2);
    }
}
