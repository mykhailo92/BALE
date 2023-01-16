package de.bale.ui.learningUnit.interfaces;

import com.google.common.eventbus.Subscribe;
import de.bale.messages.EyeTrackingDataMessage;

public class EyeTrackerListener {

    private ILearningUnitController controller;

    public EyeTrackerListener(ILearningUnitController controller) {
        this.controller = controller;
    }

    /**
     * Notifies the Controller of a new predicted Eye Position
     * @param eyeTrackingDataMessage Message
     */
    @Subscribe
    public void getNewMessage(EyeTrackingDataMessage eyeTrackingDataMessage) {
        controller.newEyePosition(eyeTrackingDataMessage.getX(), eyeTrackingDataMessage.getY());
    }
}
