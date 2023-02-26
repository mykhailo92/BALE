package de.bale.repository;

import de.bale.db.DatabaseHandler;
import de.bale.db.Const;
import de.bale.repository.UsersAnswers.IUsersAnswers;
import de.bale.repository.UsersAnswers.UsersAnswers;

import java.sql.*;

public class UsersAnswersRepository implements IUsersAnswers {

    public Connection dbConnection;
    private PreparedStatement stmt;

    public UsersAnswersRepository() {
        dbConnection = DatabaseHandler.getInstance().dbConnection;
    }
    @Override
    public void save(UsersAnswers usersAnswers) {
        String insert = "INSERT INTO " + Const.ANSWER_TABLE +
                "("+ Const.EXPERIMENT_ID + "," + Const.DESCRIPTION + "," + Const.ANSWER + "," + Const.ANSWER_ATTEMPTS +")"
                + "VALUES (?, ?, ?, ?)";
        try {
            stmt = dbConnection.prepareStatement(insert);
            stmt.setInt(1, usersAnswers.getExperimentID());
            stmt.setString(2, usersAnswers.getDescription());
            stmt.setString(3, usersAnswers.getAnswer());
            stmt.setInt(4, usersAnswers.getSolutionAttempts());
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
