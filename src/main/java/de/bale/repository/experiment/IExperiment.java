package de.bale.repository.experiment;

public interface IExperiment {
    void save(Experiment experiment);
    int getCurrentExperimentID();
}
