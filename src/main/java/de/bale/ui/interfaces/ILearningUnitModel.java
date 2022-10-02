package de.bale.ui.interfaces;

import org.w3c.dom.Element;

public interface ILearningUnitModel {
    boolean isFirstFlag();

    void setFirstFlag(boolean flag);

    int getContainerIndicator();

    void setContainerIndicator(int indicator);

    void addListener(Listener listener);

    void setContainer(Element[] containerArray);
    Element[] getContainer();
}
