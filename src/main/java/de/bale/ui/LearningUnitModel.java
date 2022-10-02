package de.bale.ui;

import javafx.scene.web.WebEngine;
import org.w3c.dom.Element;

import java.util.LinkedList;
import java.util.List;

public class LearningUnitModel {
    private WebEngine engine;
    private int containerIndicator = 0;
    private boolean firstFlag=true;

    //region Obvserverpattern
    public interface Listener {
        void onChange(LearningUnitModel model);
    }

    private final List<Listener> listeners = new LinkedList<>();

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    private void notifyObserver() {
        listeners.forEach(listener -> listener.onChange(this));
    }
    //endregion

    public int getContainerIndicator() {
        return containerIndicator;
    }

    public void setContainerIndicator(int currentContainerIndicator) {
        this.containerIndicator = currentContainerIndicator;
        notifyObserver();
    }

    public boolean isFirstFlag() {
        return firstFlag;
    }

    public void setFirstFlag(boolean firstFlag) {
        this.firstFlag = firstFlag;
        notifyObserver();
    }


}
