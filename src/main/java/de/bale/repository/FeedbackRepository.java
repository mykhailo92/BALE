package de.bale.repository;

import de.bale.db.DatabaseHandler;
import de.bale.db.Const;
import de.bale.repository.feedback.IFeedbackRepository;
import de.bale.repository.feedback.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FeedbackRepository implements IFeedbackRepository {
    public Connection dbConnection;
    private PreparedStatement stmt;

    public FeedbackRepository() {
        dbConnection = DatabaseHandler.getInstance().dbConnection;
    }

    @Override
    public void saveFeedback(Feedback feedback) {
        String insert = "INSERT INTO " + Const.FEEDBACK_TABLE +
                "("+ Const.EXPERIMENT_ID + "," + Const.DESCRIPTION + "," + Const.DATE_TIME+ "," + Const.ANSWER_ATTEMPTS
                + "," + Const.COMMENTS + ")"
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            stmt = dbConnection.prepareStatement(insert);
            stmt.setInt(1, feedback.getExperimentID());
            stmt.setString(2, feedback.getDescription());
            stmt.setString(3, feedback.getDate());
            stmt.setInt(4, feedback.getAttempts());
            stmt.setString(5, feedback.getComments());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("SQLException");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Feedback> getAllExperiments() {
        return null;
    }

    @Override
    public int getFeedbackByID(int id) {
        return 0;
    }

    @Override
    public void updateFeedback(Feedback feedback) {

    }

    @Override
    public void deleteFeedback(int id) {

    }
}
