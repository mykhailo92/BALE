package de.bale.repository.experiment;

import java.util.List;

public interface IExperimentRepository {
    List<Experiment> getAllExperiments();
    int getCurrentExperimentID();
    void addExperiment(Experiment experiment);
    void updateExperiment(Experiment experiment);
    void deleteExperiment(int id);
}
