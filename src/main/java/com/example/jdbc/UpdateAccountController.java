package com.example.jdbc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class UpdateAccountController {
    @FXML
    public TextField txtUsername;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public PasswordField txtConfirmPass;
    @FXML
    public Button btnUpdate;

    public void initialize() {
        HelloController hc = new HelloController();
        txtUsername.setText(hc.CurrentName);
    }

    public void UpdateData(ActionEvent event) throws SQLException {
        HelloController hc = new HelloController();
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement(
                     "UPDATE tblusers SET username = ?, password = ? WHERE id = ?"
             )) {
            String newUsername = txtUsername.getText();
            String newPassword = txtPassword.getText();
            if(newPassword.equals(txtConfirmPass.getText())) {
                statement.setString(1, newUsername);
                statement.setString(2, txtConfirmPass.getText());
                statement.setInt(3, hc.CurrentID);
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update Account");
            alert.setHeaderText("Are you sure you want to update this account?");
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            alert.getButtonTypes().setAll(yes, no);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == yes) {
                int rowsUpdated = statement.executeUpdate();
                hc.ReadData(newUsername, newPassword);
                System.out.println("Rows updated: "+rowsUpdated);
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent p = FXMLLoader.load(getClass().getResource("homepage-view.fxml"));
                Scene s = new Scene(p);
                stage.setScene(s);
                stage.show();
            } else {
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent p = FXMLLoader.load(getClass().getResource("homepage-view.fxml"));
                Scene s = new Scene(p);
                stage.setScene(s);
                stage.show();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
