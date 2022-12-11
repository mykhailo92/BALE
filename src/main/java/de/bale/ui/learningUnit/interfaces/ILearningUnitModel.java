package de.bale.ui.learningUnit.interfaces;

import de.bale.ui.interfaces.Listener;
import org.w3c.dom.Element;

public interface ILearningUnitModel {

    int getContainerIndicator();

    void setContainerIndicator(int indicator);

    void addListener(Listener listener);

    void setContainer(Element[] containerArray);

    Element[] getContainer();

    void setSlides(Element[] slidesArray);

    Element[] getSlides();

    int getCurrentSlideIndicator();

    void setCurrentSlideIndicator(int indicator);

    boolean isNextButtonDisabled();

    void setNextButtonDisabled(boolean nextButtonDisabled);

    Element[] getChapter();

    void setChapter(Element[] chapter);

    void setChapterIndicator(int chapterIndicator);

    int getChapterIndicator();

    Element[] getChapterMarks();

    void setChapterMarks(Element[] chapterMarks);

    String getCloseButtonText();

    void setCloseButtonText(String closeButtonText);
}
