package com.example.jdbc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class HelloController {
    @FXML
    public TextField txtUsername;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public Button btnLogIn;
    @FXML
    public Button btnRegister;
    @FXML
    public Label txtError;

    public static String CurrentName;
    public static int CurrentID;

    @FXML
    public boolean ReadData(String name, String password){
        try(Connection c = MySQLConnection.getConnection();
            Statement statement = c.createStatement()) {
            c.setAutoCommit(false);
            String query = "SELECT * FROM tblusers";
            ResultSet res = statement.executeQuery(query);
            while (res.next()){
                String currName = res.getString("username");
                String currPass = res.getString("password");
                int id = res.getInt("id");
                if(name.equals(currName) && password.equals(currPass)){
                    CurrentName = currName;
                    CurrentID = id;
                    return true;
                }
            }
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    private void LogIn(ActionEvent event) throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        boolean res = ReadData(username,password);
        if(res){
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent p = FXMLLoader.load(getClass().getResource("homepage-view.fxml"));
            Scene s = new Scene(p);
            stage.setScene(s);
            stage.show();
        } else {
            txtError.setText("Log In Error! Register Instead");
        }
    }

    @FXML
    private void Register(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent p = FXMLLoader.load(getClass().getResource("register.fxml"));
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }
}