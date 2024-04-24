package com.example.jdbc;

import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Tables {
    @FXML
    public void CreateTableUsers(){
        String query = "CREATE TABLE IF NOT EXISTS tblusers (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50) NOT NULL," +
                "username VARCHAR(50)," +
                "password VARCHAR(100) NOT NULL)";
        try (Connection con = MySQLConnection.getConnection();
            Statement stmt = con.createStatement()){
            stmt.executeUpdate(query);
            System.out.println("Table Created Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void CreateTableArtifacts(){
        String query = "CREATE TABLE IF NOT EXISTS tblartifact (" +
                "artifactid INT PRIMARY KEY AUTO_INCREMENT," +
                "acctid INT(10) NOT NULL," +
                "artifact_name VARCHAR(50) NOT NULL," +
                "artifact_type VARCHAR(50) NOT NULL," +
                "artifact_origin VARCHAR(50) NOT NULL," +
                "FOREIGN KEY (acctid) REFERENCES tblusers(id)" +
                " ON DELETE CASCADE" +
                " ON UPDATE CASCADE" +
                ")";
        try (Connection con = MySQLConnection.getConnection();
             Statement stmt = con.createStatement()){
            stmt.executeUpdate(query);
            System.out.println("Table Created Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
