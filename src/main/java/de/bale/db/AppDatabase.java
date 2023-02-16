package de.bale.db;

import de.bale.logger.Logger;
import de.bale.messages.DatabaseConnectedMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AppDatabase extends Configs {
    private static AppDatabase instance;
    public Connection dbConnection;

    private AppDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC ClassNotFoundException");
        }
        try {
            dbConnection = DriverManager.getConnection(dbRoot);

//            Statement stmt = dbConnection.createStatement();
//            String sql = "CREATE TABLE IF NOT EXISTS useranswers (experimentID INTEGER PRIMARY KEY, description TEXT, " +
//                    "answer TEXT, attempts INTEGER)";
//            stmt.executeUpdate(sql);
            Logger.getInstance().post(new DatabaseConnectedMessage());
        } catch (SQLException e) {
            System.out.println("SQLException");
        }
    }
    public static synchronized AppDatabase getInstance() {
        if (instance == null) {
            instance = new AppDatabase();
        }
        return instance;
    }

}
