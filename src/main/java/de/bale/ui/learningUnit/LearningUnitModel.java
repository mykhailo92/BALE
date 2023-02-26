package de.bale.ui.learningUnit;

import de.bale.ui.interfaces.Listener;
import de.bale.ui.learningUnit.interfaces.ILearningUnitModel;
import org.w3c.dom.Element;

import java.util.LinkedList;
import java.util.List;

public class LearningUnitModel implements ILearningUnitModel {
    private int containerIndicator = -1;
    private final List<Listener> listeners = new LinkedList<>();
    private boolean nextButtonDisabled = true;
    private Element[] container;
    private Element[] chapter;
    private int chapterIndicator = 0;
    private Element[] chapterMarks; //First Element should be the Container of the Elements
    private Element[] slides;
    private int currentSlideIndicator = 0;
    private String closeButtonText="";
    private int experimentID;

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
    public void setContainer(Element[] containerArray) { this.container = containerArray; }


    @Override
    public boolean isNextButtonDisabled() {
        return nextButtonDisabled;
    }

    @Override
    public void setNextButtonDisabled(boolean nextButtonDisabled) {
        this.nextButtonDisabled = nextButtonDisabled;
        notifyObserver();
    }

    @Override
    public Element[] getChapter() {
        return chapter;
    }

    @Override
    public void setChapter(Element[] chapter) {
        this.chapter = chapter;
    }

    @Override
    public void setChapterIndicator(int chapterIndicator) {
        this.chapterIndicator = chapterIndicator;
        notifyObserver();
    }

    @Override
    public int getChapterIndicator() {
        return chapterIndicator;
    }

    @Override
    public Element[] getChapterMarks() {
        return chapterMarks;
    }

    @Override
    public void setChapterMarks(Element[] chapterMarks) {
        this.chapterMarks = chapterMarks;
        notifyObserver();
    }

    @Override
    public void setSlides(Element[] slidesArray) { this.slides = slidesArray; }

    @Override
    public Element[] getSlides() { return slides; }

    @Override
    public int getCurrentSlideIndicator() { return currentSlideIndicator; }

    @Override
    public void setCurrentSlideIndicator(int currentSlideIndicator) {
        this.currentSlideIndicator = currentSlideIndicator;
        notifyObserver();
    }

    @Override
    public String getCloseButtonText() {
        return closeButtonText;
    }
    @Override
    public void setCloseButtonText(String closeButtonText) {
        this.closeButtonText = closeButtonText;
        notifyObserver();
    }
    @Override
    public int getExperimentID() { return experimentID; }
    @Override
    public void setExperimentID(int experimentID) { this.experimentID = experimentID; }
}
