package controller;

import javafx.scene.Parent;
import javafx.stage.Stage;
import util.JDBC;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Login implements Initializable {
    @FXML
    private Button login;
    @FXML
    private PasswordField password;
    @FXML
    private TextField userNameTf;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            rb = ResourceBundle.getBundle("Properties.login", Locale.getDefault());
            userNameTf.setPromptText(rb.getString("userName"));
        } catch (MissingResourceException e) {
            System.out.println("Missing resource");
        }

    }

    private int getUserID(String userName) throws SQLException {
        int userID = -1;
        Statement stmt = JDBC.connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT userID FROM user WHERE userName = '" + userName + "'");
        while (rs.next()) {
            userID = rs.getInt("userID");
        }
        return userID;
    }

    private String getPassword(String userID) throws SQLException {
        String password = " ";
        Statement stmt = JDBC.connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT password FROM user WHERE userID = '" + userID + "'");
        return password;
    }

    @FXML
    private void LoginButtonAction(ActionEvent event) throws SQLException, IOException {
        String userName = userNameTf.getText();
        int userId = getUserID(userName);
        Parent root;
        Stage stage;
    }


};

