package lk.ijse.ikmanRental.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String URL="jdbc:mysql://localhost:3306/ikmanRental";
    private static final Properties props=new Properties();
    private static DBConnection dbConnection;
    private static Connection connection;

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "80221474");
    }

    private DBConnection() throws SQLException {
        //Class.forName("com.mysql.cj.jdbc.Driver");
         connection= DriverManager.getConnection(URL, props);
    }

    public static DBConnection getInstance() throws SQLException {
        return dbConnection == null ? dbConnection = new DBConnection() : dbConnection;
    }
    public Connection getConnection() {
        return connection;
    }
}
