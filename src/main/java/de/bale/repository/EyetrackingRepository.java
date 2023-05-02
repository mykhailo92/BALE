package de.bale.repository;

import de.bale.db.DatabaseHandler;
import de.bale.db.Const;
import de.bale.repository.eyetracking.IEyetrackingRepository;
import de.bale.repository.eyetracking.Eyetracking;

import java.sql.*;
import java.util.List;

public class EyetrackingRepository implements IEyetrackingRepository {

    public Connection dbConnection;
    private PreparedStatement stmt;

    public EyetrackingRepository() {
        dbConnection = DatabaseHandler.getInstance().dbConnection;
    }

    @Override
    public void addEyetracking(Eyetracking eyetracking) {
        String insert = "INSERT INTO " + Const.EYETRACKING_TABLE +
                "("+ Const.EXPERIMENT_ID + "," + Const.BLICK_ID + "," + Const.TIMESTAMP + "," + Const.X + ","
                + Const.Y + "," + Const.ELEMENT +")"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            stmt = dbConnection.prepareStatement(insert);
            stmt.setInt(1, eyetracking.getExperimentID());
            stmt.setInt(2, eyetracking.getBlickID());
            stmt.setString(3, eyetracking.getTimeStamp());
            stmt.setInt(4, eyetracking.getX());
            stmt.setInt(5, eyetracking.getY());
            stmt.setString(6, eyetracking.getElement());
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

    @Override
    public List<Eyetracking> getAllEyetrackings() {
        return null;
    }

    @Override
    public int getEyetrackingByID(int id) {
        return 0;
    }

    @Override
    public void updateEyetracking(Eyetracking eyetracking) {

    }

    @Override
    public void deleteEyetracking(int id) {

    }

}
