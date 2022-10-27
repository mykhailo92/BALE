package de.bale.ui;

import de.bale.ui.interfaces.ILearningUnitModel;
import de.bale.ui.interfaces.Listener;
import org.w3c.dom.Element;

import java.util.LinkedList;
import java.util.List;

public class LearningUnitModel implements ILearningUnitModel {
    private int containerIndicator = 0;
    private boolean firstFlag = true;
    private final List<Listener> listeners = new LinkedList<>();

    private Element[] container;

    private Element[] slides;

    private int currentSlideIndicator = 0;

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
    public boolean isFirstFlag() {
        return firstFlag;
    }

    @Override
    public void setFirstFlag(boolean firstFlag) {
        this.firstFlag = firstFlag;
        notifyObserver();
    }

    @Override
    public void setContainer(Element[] containerArray) {
        this.container = containerArray;
    }

    @Override
    public Element[] getContainer() {
        return container;
    }

    public void setSlides(Element[] slidesArray) { this.slides = slidesArray; }

    public Element[] getSlides() { return slides; }

    public int getCurrentSlideIndicator() { return currentSlideIndicator; }

    public void setCurrentSlideIndicator(int currentSlideIndicator) {
        this.currentSlideIndicator = currentSlideIndicator;
        notifyObserver();
    }
}
