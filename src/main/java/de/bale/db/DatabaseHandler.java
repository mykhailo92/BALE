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
            String sqlAnswers = "CREATE TABLE IF NOT EXISTS " + Const.ANSWER_TABLE + " (experimentID INT PRIMARY KEY, " +
                    "description VARCHAR(50), answer VARCHAR(200), attempts INT)";
            stmt.executeUpdate(sqlAnswers);
            String sqlTimestamp = "CREATE TABLE IF NOT EXISTS " + Const.TIMESTAMP_TABLE + " (id INT PRIMARY KEY, " +
                    "experimentID VARCHAR(50), description VARCHAR(50), answer VARCHAR(200), attempts INT)";
            stmt.executeUpdate(sqlTimestamp);
        } catch (SQLException e) { throw new RuntimeException(e); }

    }

    public static synchronized DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }
}
