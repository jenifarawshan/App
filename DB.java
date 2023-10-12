package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

	private static final String connect_driver = "com.mysql.cj.jdbc.Driver";
    private static final String connect_url = "jdbc:mysql://localhost:3306/kadaidb";
    private static final String connect_username = "root";
    private static final String connect_password = "jenifa1234!";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(connect_driver);
        return DriverManager.getConnection(connect_url,connect_username,connect_password);
    }
}