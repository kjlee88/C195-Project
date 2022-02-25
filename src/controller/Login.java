package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.JDBC;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Login implements Initializable {
    @FXML
    private Button login;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private TextField userNameInput;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            rb = ResourceBundle.getBundle("Properties.login", Locale.getDefault());
            userNameInput.setPromptText(rb.getString("userName"));
            passwordInput.setPromptText(rb.getString("password"));
        } catch (MissingResourceException e) {
            System.out.println("Missing resource");
        }

    }

    private int getUserID(String userName) throws SQLException {
        int userID = -1;
        Statement stmt = JDBC.connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT user_ID FROM users WHERE user_name = '" + userName + "'");
        while (rs.next()) {
            userID = rs.getInt("user_ID");
        }
        return userID;
    }

    private String getPassword(int userID) throws SQLException {
        String passwordDB = " ";
        Statement stmt = JDBC.connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT password FROM users WHERE user_ID = '" + userID + "'");
        while (rs.next()){
            passwordDB = rs.getString("password");
        }
        return passwordDB;
    }

    @FXML
    private void login(ActionEvent event) throws SQLException, IOException {
        String userName = userNameInput.getText();
        int userID = getUserID(userName);
        String passwordDB = getPassword(userID);
        String password = passwordInput.getText();

        if (password.equals(passwordDB)) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Error");
            a.setHeaderText("Login unsuccessful");
            a.setContentText("Incorrect Username and/or Password");
            Optional<ButtonType> result = a.showAndWait();
        }
    }


};

