package controller;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import util.JDBC;
import util.TimeAndZone;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

public class Login implements Initializable {
    @FXML
    private Label loginError;
    @FXML
    private Label password;
    @FXML
    private Label instruction;
    @FXML
    private Label userID;
    @FXML
    private Label zoneInfo;
    @FXML
    private Button login;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private TextField usernameInput;

    public static ZoneId userZoneID = ZoneId.systemDefault();


    /**
     * Defines path for language property package
     */
    ResourceBundle rb = ResourceBundle.getBundle("util.lang",Locale.getDefault());


    /**
     * Detects userZone and uses that to determine with language to display
     * Takes in username and password
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        zoneInfo.setText(String.valueOf(userZoneID));
        try {
            rb = ResourceBundle.getBundle("util.lang", Locale.getDefault());
            userID.setText(rb.getString("username"));
            password.setText(rb.getString("password"));
            login.setText(rb.getString("login"));
        } catch (MissingResourceException e) {
            System.out.println("Resource file missing or not used");
        }

    }

    /**
     * Grabs the username to find the user ID from SQL database
     * @param username uses username provided by user at the login screen
     * @return userID
     * @throws SQLException
     */

    private int getUserID(String username) throws SQLException {
        int userID = 0;
        Statement stmt = JDBC.connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT user_ID FROM users WHERE user_name = '" + username + "'");
        while (rs.next()) {
            userID = rs.getInt("user_ID");
        }
        return userID;
    }

    /**
     * uses primary key userID to get password and saves it
     * @param userID userID that was found on customers table using getUserID method
     * @return returns password found on customers table for passed userID
     * @throws SQLException
     */

    private String getPassword(int userID) throws SQLException {
        String passwordDB = " ";
        Statement stmt = JDBC.connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT password FROM users WHERE user_ID = '" + userID + "'");
        while (rs.next()){
            passwordDB = rs.getString("password");
        }
        return passwordDB;
    }

    /**
     * Compares password user entered with saved password from database
     * If matches, then login user is created
     * records both successful and failed login activities to login_activity file
     * @param event event
     * @throws SQLException sql error
     * @throws IOException i/o error
     */

    @FXML
    private void login(ActionEvent event) throws SQLException, IOException {
        String username = usernameInput.getText();
        String pass = passwordInput.getText();

        if (UserDAO.verifyUserLogin(username,pass)){
            User currentUser = new User(getUserID(username),username);
            String attempt = "successful.";
            logger(username, attempt);
            Parent root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
            loginError.setText(rb.getString("loginerror"));
        }


    /**
     * Creates login_activity file to user directory
     * @param username username entered at login screen
     * @param attempt variable that either prints successful or unsuccessful login events
     */
    public void logger(String username, String attempt) {
        try {
            String fileName = "login_activity";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(TimeAndZone.getTimestamp() + " User " + username + " login attempt was " + attempt + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

};

