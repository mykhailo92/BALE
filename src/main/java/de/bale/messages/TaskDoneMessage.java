package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class TaskDoneMessage extends AbstractMessage {
    public TaskDoneMessage() {
        super("Done",0);
    }
}
