package com.example.jdbc;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class HomepageController {
    @FXML
    public Label txtWelcome;
    @FXML
    public TextField txtArtiOrigin;
    @FXML
    public TextField txtArtiType;
    @FXML
    public TextField txtArtiName;
    @FXML
    public Button btnSave;
    @FXML
    public Button btnDelete;
    @FXML
    public Button btnUpdate;
    @FXML
    public ListView<String> ArtifactsList;
    @FXML
    public Button btnLogOut;
    @FXML
    public Button btnDelArti;

    @FXML
    public void initialize() throws SQLException {
        HelloController hc = new HelloController();
        txtWelcome.setText("Welcome, "+ hc.CurrentName);

        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM tblartifact WHERE acctid = ?")){
            c.setAutoCommit(false);
            int userid = hc.CurrentID;
            stmt.setInt(1, userid);
            ResultSet rs = stmt.executeQuery();
            ObservableList<String> items = FXCollections.observableArrayList();
            while(rs.next()){
                String item = rs.getString("artifactid") + " , " + rs.getString("artifact_name") + " , " + rs.getString("artifact_type") + " , " + rs.getString("artifact_origin");
                items.add(item);
            }
            ArtifactsList.setItems(items);
            c.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void InsertData(String name, String type, String origin){
        HelloController hc = new HelloController();
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "INSERT INTO tblartifact (acctid,artifact_name,artifact_type,artifact_origin) VALUES (?,?,?,?)"
            )){
            c.setAutoCommit(false);
            statement.setInt(1, hc.CurrentID);
            statement.setString(2,name);
            statement.setString(3,type);
            statement.setString(4,origin);
            int rowsInserted = statement.executeUpdate();
            System.out.println("Rows inserted: "+rowsInserted);
            initialize();
            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void SaveArtifact(){
        String name = txtArtiName.getText();
        String type = txtArtiType.getText();
        String origin = txtArtiOrigin.getText();
        InsertData(name, type, origin);
        txtArtiName.clear();
        txtArtiType.clear();
        txtArtiOrigin.clear();
    }

    @FXML
    public void DeleteAccount(ActionEvent event) throws SQLException {
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "DELETE FROM tblusers WHERE id=?"
            )){
            c.setAutoCommit(false);
            HelloController hc = new HelloController();
            int id = hc.CurrentID;
            statement.setInt(1,id);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Account");
            alert.setHeaderText("Are you sure you want to delete this account?");
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            alert.getButtonTypes().setAll(yes, no);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == yes) {
                int rowsDeleted = statement.executeUpdate();
                System.out.println("Rows deleted: " + rowsDeleted);
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent p = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Scene s = new Scene(p);
                stage.setScene(s);
                stage.show();
                c.commit();
            } else {
                alert.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void UpdateAccount(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent p = FXMLLoader.load(getClass().getResource("update-acccount-view.fxml"));
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }

    @FXML
    public void LogOut(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent p = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene s = new Scene(p);
        stage.setScene(s);
        stage.show();
    }

    @FXML
    public void deleteArtifact(ActionEvent event){
        int selectedIndex = ArtifactsList.getSelectionModel().getSelectedIndex();
        ObservableList<String> items = FXCollections.observableArrayList();
        String selectedItem = items.get(selectedIndex);
        if(selectedIndex != -1){
            try(Connection c = MySQLConnection.getConnection();
                PreparedStatement statement = c.prepareStatement(
                        "DELETE FROM tblartifact WHERE artifactid=?"
                )){
                HelloController hc = new HelloController();
                String[] parts = selectedItem.split(" , ");

                txtArtiName.setText(parts[1]);
                txtArtiType.setText(parts[2]);
                txtArtiOrigin.setText(parts[3]);

                statement.setInt(1, Integer.parseInt(parts[0]));
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Artifact");
                alert.setHeaderText("Are you sure you want to delete this artifact?");
                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No");
                alert.getButtonTypes().setAll(yes, no);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == yes) {
                    int rowsDeleted = statement.executeUpdate();
                    System.out.println("Rows deleted: " + rowsDeleted);
                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Parent p = FXMLLoader.load(getClass().getResource("homepage-view.fxml"));
                    Scene s = new Scene(p);
                    stage.setScene(s);
                    stage.show();
                    initialize();
                } else {
                    alert.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
