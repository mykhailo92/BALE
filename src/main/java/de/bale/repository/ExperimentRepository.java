package de.bale.repository;

import de.bale.db.Const;
import de.bale.db.DatabaseHandler;
import de.bale.repository.experiment.Experiment;
import de.bale.repository.experiment.IExperimentRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class ExperimentRepository implements IExperimentRepository {
    public Connection dbConnection;
    private PreparedStatement stmt;

    public ExperimentRepository() {
        dbConnection = DatabaseHandler.getInstance().dbConnection;
    }

    @Override
    public void addExperiment(Experiment experiment) {
        String insert = "INSERT INTO " + Const.EXPERIMENT_TABLE +
                "("+ Const.EXPERIMENT_ID + "," + Const.NAME + "," + Const.DATE + "," + Const.LE_TITLE +")"
                + "VALUES (?, ?, ?, ?)";
        try {
            stmt = dbConnection.prepareStatement(insert);
            stmt.setInt(1, experiment.getExperimentID());
            stmt.setString(2, experiment.getName());
            stmt.setString(3, experiment.getDate());
            stmt.setString(4, experiment.getLearnUnit());
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
    public int getCurrentExperimentID() {
        String sql = "SELECT * FROM " + Const.EXPERIMENT_TABLE+ " ORDER BY " + Const.EXPERIMENT_ID + " DESC LIMIT 1";
        try {
            stmt = dbConnection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(Const.EXPERIMENT_ID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public void updateExperiment(Experiment experiment) {

    }

    @Override
    public void deleteExperiment(int id) {

    }

    @Override
    public List<Experiment> getAllExperiments() {
        return null;
    }
}
