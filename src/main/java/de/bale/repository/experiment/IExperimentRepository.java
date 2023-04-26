package de.bale.repository.experiment;

public interface IExperimentRepository {
    void saveExperiment(Experiment experiment);
    int getCurrentExperimentID();
}
