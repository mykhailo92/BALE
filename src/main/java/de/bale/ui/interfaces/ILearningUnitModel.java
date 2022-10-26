package de.bale.ui.interfaces;

import org.w3c.dom.Element;

public interface ILearningUnitModel {

    int getContainerIndicator();

    void setContainerIndicator(int indicator);

    void addListener(Listener listener);

    void setContainer(Element[] containerArray);
    Element[] getContainer();

    boolean isNextButtonDisabled();

    void setNextButtonDisabled(boolean nextButtonDisabled);
}
