package de.bale.analyze;

import com.google.common.eventbus.Subscribe;
import de.bale.logger.Logger;
import de.bale.messages.eyetracking.EyetrackingAoIMessage;
import de.bale.messages.eyetracking.EyetrackingFitDoneMessage;
import de.bale.messages.eyetracking.EyetrackingHelpMessage;
import de.bale.repository.EyetrackingRepository;
import de.bale.repository.eyetracking.Eyetracking;
import netscape.javascript.JSException;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class EyetrackingAnalyzer {

    private String lastAoi;
    private Map<String, Long> viewTimeMap = new HashMap<>();
    private Instant lastEyetrackingTime;
    private int viewID = 0;


    @Subscribe
    public void gotEyetrackingFitDone(EyetrackingFitDoneMessage eyetrackingFitDoneMessage) {
        setLastEyetrackingTime(Instant.now());
    }

    @Subscribe
    public void gotNewAoIMessage(EyetrackingAoIMessage eyetrackingAoIMessage) {
        try {
            Instant end = Instant.now();
            String areaOfInterestAttribute = eyetrackingAoIMessage.getAoi();
            long timeDifference = Duration.between(getLastEyetrackingTime(), end).toMillis();
            addToAreaOfInterestMap(areaOfInterestAttribute, timeDifference);
            if (areaOfInterestAttribute == null) {
                areaOfInterestAttribute = "undefined";
            }
            setLastAoi(areaOfInterestAttribute);
            Eyetracking eyetracking = new Eyetracking(eyetrackingAoIMessage.getExperimentID(), getViewID(),
                    eyetrackingAoIMessage.getX(), eyetrackingAoIMessage.getY(), "", areaOfInterestAttribute, timeDifference);
            EyetrackingRepository eyetrackingRepository = new EyetrackingRepository();
            eyetrackingRepository.save(eyetracking);
            incrementCurrentViewID();
            setLastEyetrackingTime(Instant.now());
            analyzeCurrentEyetrackingData();
        } catch (ClassCastException | JSException ignored) {
        } //In case something that no Element is looked at or the JavaScript could not find an Element
    }

    private void analyzeCurrentEyetrackingData() {
        viewTimeMap.forEach((key, value) -> {
            if (key != null && value > EyetrackingConstant.HELP_TIME_IN_MS) {
                Logger.getInstance().post(new EyetrackingHelpMessage(key));
            }
        });
    }

    public void setLastAoi(String domElement) {
        lastAoi = domElement;
    }

    public String getLastAoi() {
        return lastAoi;
    }


    public Instant getLastEyetrackingTime() {
        return lastEyetrackingTime;
    }

    public void setLastEyetrackingTime(Instant now) {
        lastEyetrackingTime = now;
    }

    public void addToAreaOfInterestMap(String areaOfInterestAttribute, long durationToAdd) {
        long currentViewTime = 0;
        if (viewTimeMap.containsKey(areaOfInterestAttribute)) {
            currentViewTime = viewTimeMap.get(areaOfInterestAttribute);
        }
        viewTimeMap.put(areaOfInterestAttribute, currentViewTime + durationToAdd);
    }


    public Map<String, Long> getAoiMap() {
        return viewTimeMap;
    }

    public void incrementCurrentViewID() {
        viewID += 1;
    }

    public int getViewID() {
        return viewID;
    }
}
