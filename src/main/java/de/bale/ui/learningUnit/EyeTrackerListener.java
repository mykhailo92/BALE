package de.bale.ui.learningUnit;

import com.google.common.eventbus.Subscribe;
import de.bale.messages.eyetracking.EyeTrackingDataMessage;
import de.bale.messages.eyetracking.EyetrackingCallibrationMessage;
import de.bale.ui.learningUnit.interfaces.ILearningUnitController;

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
    public void gotNewPosition(EyeTrackingDataMessage eyeTrackingDataMessage) {
        controller.newEyePosition(eyeTrackingDataMessage.getX(), eyeTrackingDataMessage.getY());
    }

    @Subscribe
    public void gotCallibrationPoint(EyetrackingCallibrationMessage eyetrackingCallibrationMessage) {
        controller.notifyCallibration(eyetrackingCallibrationMessage.getClickX(),eyetrackingCallibrationMessage.getClickY());
    }
}
