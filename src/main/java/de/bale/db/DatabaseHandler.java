package de.bale.db;

import de.bale.logger.Logger;
import de.bale.messages.DatabaseConnectedMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler extends Configs {
    private static DatabaseHandler instance;
    public Connection dbConnection;

    private DatabaseHandler() {
        String connString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbConnection = DriverManager.getConnection(connString, dbUser, dbPasswort);
            Logger.getInstance().post(new DatabaseConnectedMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Treiber nicht gefunden: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Verbindung zur Datenbank fehlgeschlagen: " + e.getMessage());
        }

        try {
            assert dbConnection != null;
            Statement stmt = dbConnection.createStatement();

            String sqlExperiment = "CREATE TABLE IF NOT EXISTS " + Const.EXPERIMENT_TABLE +
                    " (" + Const.EXPERIMENT_ID + " INT AUTO_INCREMENT NOT NULL, " + Const.NAME + " VARCHAR(50) NOT NULL," +
                    Const.DATE + " VARCHAR(50)," + Const.LE_TITLE + " VARCHAR(50), PRIMARY KEY (" + Const.EXPERIMENT_ID
                    + "," + Const.NAME + "))";
            stmt.executeUpdate(sqlExperiment);

            String sqlFeedback = "CREATE TABLE IF NOT EXISTS " + Const.FEEDBACK_TABLE +
                    " (" + Const.EXPERIMENT_ID + " INT NOT NULL, " + Const.CLICK_ID + " INT AUTO_INCREMENT NOT NULL, " +
                    Const.DESCRIPTION + " VARCHAR(50)," + Const.DATE_TIME + " VARCHAR(50)," + Const.ANSWER_ATTEMPTS +
                    " INT," + Const.COMMENTS + " VARCHAR(300), PRIMARY KEY (" + Const.CLICK_ID + "), FOREIGN KEY (" +
                    Const.EXPERIMENT_ID + ") REFERENCES " + Const.EXPERIMENT_TABLE + "(" + Const.EXPERIMENT_ID + ")" +
                    " ON DELETE CASCADE)";
            stmt.executeUpdate(sqlFeedback);

            String sqlEyetracking = "CREATE TABLE IF NOT EXISTS " + Const.EYETRACKING_TABLE +
                    " (" + Const.EXPERIMENT_ID + " INT NOT NULL, " + Const.BLICK_ID + " INT AUTO_INCREMENT NOT NULL," +
                    Const.TIMESTAMP + " TIMESTAMP," + Const.X + " INT," + Const.Y + " INT," + Const.ELEMENT +
                    " VARCHAR(50)," + Const.VIEW_DURATION + " BIGINT," + "PRIMARY KEY (" + Const.BLICK_ID + "), FOREIGN KEY (" +
                    Const.EXPERIMENT_ID + ") REFERENCES " + Const.EXPERIMENT_TABLE + "(" + Const.EXPERIMENT_ID + ")" +
                    " ON DELETE CASCADE)";
            stmt.executeUpdate(sqlEyetracking);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static synchronized DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }
}
