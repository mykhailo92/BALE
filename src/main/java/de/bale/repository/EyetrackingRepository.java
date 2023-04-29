package de.bale.repository;

import de.bale.db.Const;
import de.bale.db.DatabaseHandler;
import de.bale.repository.eyetracking.Eyetracking;
import de.bale.repository.eyetracking.IEyetrackingRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EyetrackingRepository implements IEyetrackingRepository {

    public Connection dbConnection;
    private PreparedStatement stmt;

    public EyetrackingRepository() {
        dbConnection = DatabaseHandler.getInstance().dbConnection;
    }

    @Override
    public void save(Eyetracking eyetracking) {
        String insert = "INSERT INTO " + Const.EYETRACKING_TABLE +
                "(" + Const.EXPERIMENT_ID + "," + Const.BLICK_ID + "," + Const.X + ","
                + Const.Y + "," + Const.ELEMENT + "," + Const.VIEW_DURATION + ")"
                + "VALUES (?, ?, ?, ?,?,?)";
//        String insert = "INSERT INTO " + Const.EYETRACKING_TABLE +
//                "(" + Const.EXPERIMENT_ID + "," + Const.BLICK_ID + "," + Const.TIMESTAMP + "," + Const.X + ","
//                + Const.Y + "," + Const.ELEMENT + "," + Const.VIEW_DURATION + ")"
//                + "VALUES (?, ?, ?, ?, ?, ?,?)";
        try {
            stmt = dbConnection.prepareStatement(insert);
            stmt.setInt(1, eyetracking.getExperimentID());
            stmt.setInt(2, eyetracking.getBlickID());
//            stmt.setString(3, eyetracking.getTimeStamp());
            stmt.setInt(3, eyetracking.getX());
            stmt.setInt(4, eyetracking.getY());
            stmt.setString(5, eyetracking.getElement());
            stmt.setLong(6, eyetracking.getViewDurationInMS());
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
