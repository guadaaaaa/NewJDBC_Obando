package com.example.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/dbobandocsit228";
    public static final String USERNAME = "Obando";
    public static final String PASSWORD = "guadalue123";

    static Connection getConnection(){
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("DB Connection success");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }
}
