package de.bale.analyze;

import com.google.common.eventbus.Subscribe;
import de.bale.logger.Logger;
import de.bale.messages.ExperimentEndMessage;
import de.bale.messages.eyetracking.EyetrackingAoIMessage;
import de.bale.messages.eyetracking.EyetrackingFitDoneMessage;
import de.bale.messages.eyetracking.EyetrackingHelpMessage;
import de.bale.repository.EyetrackingRepository;
import de.bale.repository.eyetracking.Eyetracking;
import de.bale.storage.ExcelWriter;
import netscape.javascript.JSException;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class EyetrackingAnalyzer {

    private String lastAoi;
    private Map<String, List<Long>> viewTimeMap = new HashMap<>();
    private Instant lastEyetrackingTime;
    private int viewID = 0;
    private int totalFixationDuration = 0;
    private List<EyetrackingRow> eyetrackingRows = new ArrayList<>(9);

    @Subscribe
    public void gotEyetrackingFitDone(EyetrackingFitDoneMessage eyetrackingFitDoneMessage) {
        setLastEyetrackingTime(Instant.now());
    }

    /**
     * Fired on receiving a EyetrackingAoIMessage.Its the main Method of this class, as it triggers al the other functions of
     * his Class, such as analyzeCurrentEyetrackingData. It also keeps track of the fixation time and fixation count.
     *
     * @param eyetrackingAoIMessage
     */
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
            eyetrackingRows.add(new EyetrackingRow(eyetracking.getX(), eyetracking.getY(), eyetrackingAoIMessage.getAoi(), timeDifference));
            totalFixationDuration += timeDifference;
            EyetrackingRepository eyetrackingRepository = new EyetrackingRepository();
            eyetrackingRepository.save(eyetracking);
            incrementCurrentViewID();
            setLastEyetrackingTime(Instant.now());
            analyzeCurrentEyetrackingData();
        } catch (ClassCastException | JSException ignored) {
        } //In case something that no Element is looked at or the JavaScript could not find an Element
    }

    /**
     * Fired on receiving an ExperimentEndMessage. It saves the Experiments information in an Excel Sheet in
     * %PROGRAMM_DIRECTORY%/bale/%TITLE_OF_LEARNINGUNIT%.xlsx
     *
     * @param experimentEndMessage
     */
    @Subscribe
    public void gotExperimentEndMessage(ExperimentEndMessage experimentEndMessage) {
        ExcelWriter excelWriter = new ExcelWriter(experimentEndMessage.getExperimentTitle());
        excelWriter.writeExcelSheet(experimentEndMessage.getExperimentID(), experimentEndMessage.getChildName(),
                eyetrackingRows, totalFixationDuration, viewID + 1, viewTimeMap);
    }

    private void analyzeCurrentEyetrackingData() {
        viewTimeMap.forEach((key, value) -> {
            long currentViewTime = value.get(0);
            if (key != null && currentViewTime > EyetrackingConstant.HELP_TIME_IN_MS) {
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

    /**
     * Adds an Element to the AoI Map. Fixation Count is automatically generated and as such is not needed in the
     * Signature
     * @param areaOfInterestAttribute Name of the Area of Interest
     * @param durationToAdd The Fixation Duration which is to be added to the Area of Interest
     */
    private void addToAreaOfInterestMap(String areaOfInterestAttribute, long durationToAdd) {
        long currentViewTime = 0;
        long currentFixationCount = 0;
        if (viewTimeMap.containsKey(areaOfInterestAttribute)) {
            currentViewTime = viewTimeMap.get(areaOfInterestAttribute).get(0);
            currentFixationCount = viewTimeMap.get(areaOfInterestAttribute).get(1);
        }
        List<Long> listToAdd = new LinkedList<>();
        listToAdd.add(0, currentViewTime + durationToAdd);
        listToAdd.add(1, ++currentFixationCount);
        viewTimeMap.put(areaOfInterestAttribute, listToAdd);
    }


    public Map<String, List<Long>> getAoiMap() {
        return viewTimeMap;
    }

    public void incrementCurrentViewID() {
        viewID += 1;
    }

    public int getViewID() {
        return viewID;
    }
}
