package de.bale.ui.learningUnit;

import de.bale.ui.learningUnit.interfaces.ILearningUnitModel;
import org.w3c.dom.Element;

public class SectionVisibleListener {

    ILearningUnitModel model;

    public SectionVisibleListener(ILearningUnitModel model) {
        this.model = model;
    }

    /**
     * Notifies the Listener to check if there is a onVisible-Event Attribute in the section
     */
    public void notifyMyself() {
        Element section = model.getContainer()[model.getContainerIndicator()];
        try {
            String[] onVisible = section.getAttribute("onVisible").split(";");
            switch (onVisible[0]) {
                case "disableNextButton"-> model.setNextButtonDisabled(true);
                case "renameCloseButton"-> model.setCloseButtonText(onVisible[1]);
                default -> System.err.println("Unknown Value:" + onVisible[0]);
            }
        } catch (NullPointerException ignored) {}
    }
}
