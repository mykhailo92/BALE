package de.bale.ui;

import de.bale.ui.interfaces.ILearningUnitModel;
import de.bale.ui.interfaces.Listener;
import org.w3c.dom.Element;

import java.util.LinkedList;
import java.util.List;

public class LearningUnitModel implements ILearningUnitModel {
    private int containerIndicator = -1;
    private final List<Listener> listeners = new LinkedList<>();
    private boolean nextButtonDisabled = true;
    private Element[] container;

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    private void notifyObserver() {
        listeners.forEach(listener -> listener.onChange(this));
    }

    @Override
    public int getContainerIndicator() {
        return containerIndicator;
    }

    @Override
    public void setContainerIndicator(int currentContainerIndicator) {
        this.containerIndicator = currentContainerIndicator;
        notifyObserver();
    }

    @Override
    public Element[] getContainer() {
        return container;
    }

    @Override
    public void setContainer(Element[] containerArray) {
        container = containerArray;
    }

    @Override
    public boolean isNextButtonDisabled() {
        return nextButtonDisabled;
    }

    @Override
    public void setNextButtonDisabled(boolean nextButtonDisabled) {
        this.nextButtonDisabled = nextButtonDisabled;
        notifyObserver();
    }
}
