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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {
    @FXML
    public Button btnRegister;
    @FXML
    public Label txtError;
    @FXML
    public TextField txtUsername;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public TextField txtName;

    HelloController hc = new HelloController();
    HomepageController hp = new HomepageController();

    @FXML
    public void InsertData(String name, String username, String password){
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO tblusers (name,username,password) VALUES (?,?,?)"
            )){
            statement.setString(1,name);
            statement.setString(2,username);
            statement.setString(3,password);
            int rowsInserted = statement.executeUpdate();
            System.out.println("Rows inserted: "+rowsInserted);
            hc.ReadData(username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Register(ActionEvent event) throws IOException {
        String name = txtName.getText();
        String password = txtPassword.getText();
        String username = txtUsername.getText();
        InsertData(name,username,password);
        hc.ReadData(username,password);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent p = FXMLLoader.load(getClass().getResource("homepage-view.fxml"));
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }
}
