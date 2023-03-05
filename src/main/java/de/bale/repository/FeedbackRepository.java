package de.bale.repository;

import de.bale.db.DatabaseHandler;
import de.bale.db.Const;
import de.bale.repository.feedback.IFeedback;
import de.bale.repository.feedback.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedbackRepository implements IFeedback {
    public Connection dbConnection;
    private PreparedStatement stmt;

    public FeedbackRepository() {
        dbConnection = DatabaseHandler.getInstance().dbConnection;
    }
    @Override
    public void save(Feedback feedback) {
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
}
