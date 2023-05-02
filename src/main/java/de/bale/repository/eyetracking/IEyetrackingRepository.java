package de.bale.repository.eyetracking;

import java.util.List;

public interface IEyetrackingRepository {
    List<Eyetracking> getAllEyetrackings();
    int getEyetrackingByID(int id);
    void addEyetracking(Eyetracking answers);
    void updateEyetracking(Eyetracking eyetracking);
    void deleteEyetracking(int id);
}
