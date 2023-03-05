package de.bale.repository;

import de.bale.db.DatabaseHandler;
import de.bale.db.Const;
import de.bale.repository.eyetracking.IEyetracking;
import de.bale.repository.eyetracking.Eyetracking;

import java.sql.*;

public class EyetrackingRepository implements IEyetracking {

    public Connection dbConnection;
    private PreparedStatement stmt;

    public EyetrackingRepository() {
        dbConnection = DatabaseHandler.getInstance().dbConnection;
    }
    @Override
    public void save(Eyetracking eyetracking) {
        String insert = "INSERT INTO " + Const.FEEDBACK_TABLE +
                "("+ Const.EXPERIMENT_ID + "," + Const.DESCRIPTION + "," + Const.FEEDBACK_TABLE + "," + Const.ANSWER_ATTEMPTS +")"
                + "VALUES (?, ?, ?, ?)";
        try {
            stmt = dbConnection.prepareStatement(insert);
            stmt.setInt(1, eyetracking.getExperimentID());
            stmt.setString(2, eyetracking.getDescription());
            stmt.setString(3, eyetracking.getAnswer());
            stmt.setInt(4, eyetracking.getSolutionAttempts());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
