package de.bale.repository;

import de.bale.db.DatabaseHandler;
import de.bale.db.Const;
import de.bale.repository.timeStamp.ITimestamp;
import de.bale.repository.timeStamp.TimeStamp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TimeStampRepository implements ITimestamp {
    public Connection dbConnection;
    private PreparedStatement stmt;

    public TimeStampRepository() {
        dbConnection = DatabaseHandler.getInstance().dbConnection;
    }
    @Override
    public void save(TimeStamp timeStamp) {
        String insert = "INSERT INTO " + Const.TIMESTAMP_TABLE +
                "("+ Const.EXPERIMENT_ID + "," + Const.DESCRIPTION + "," + Const.DATE_TIME +")"
                + "VALUES (?, ?, ?)";
        try {
            stmt = dbConnection.prepareStatement(insert);
            stmt.setInt(1, timeStamp.getExperimentID());
            stmt.setString(2, timeStamp.getDescription());
            stmt.setString(3, timeStamp.getDate());
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
