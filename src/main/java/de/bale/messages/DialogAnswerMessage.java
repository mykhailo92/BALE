package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class DialogAnswerMessage extends AbstractMessage {


    public DialogAnswerMessage(String title, String path) {
        super("Dialog Answer: Title= "+ title+ " Path= "+path,2);
    }
}
