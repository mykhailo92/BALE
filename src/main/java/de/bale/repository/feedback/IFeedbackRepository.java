package de.bale.repository.feedback;

import java.util.List;

public interface IFeedbackRepository {
    List<Feedback> getAllExperiments();
    int getFeedbackByID(int id);
    void saveFeedback(Feedback feedback);
    void updateFeedback(Feedback feedback);
    void deleteFeedback(int id);
}
