package controller;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.User;
import util.TimeAndZone;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class Menu implements Initializable {

    public Button reports;
    public TextArea alertDisplay;
    @FXML
    private Button customers;
    @FXML
    private Button appointments;
    @FXML
    private Button exit;
    public LocalDateTime loginTime;

    /**
     * Initializes the menu screen
     * Calls for eligible appointment list (upcoming within 15 minutes)
     * and parses it to the large text area
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginTime = LocalDateTime.now();
        try {
            String alertList = AppointmentDAO.upcomingAppointments(loginTime, UserDAO.userID);
            if (alertList.isEmpty()) {
                alertDisplay.setText("No Upcoming Appointment");
            } else {
                alertDisplay.setText(alertList);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads customer management screen
     * @param actionEvent actionEvent
     * @throws Exception exception error
     */

    public void onCustomers(ActionEvent actionEvent) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/customers.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads appointment management screen
     * @param actionEvent actionEvent
     * @throws Exception exception error
     */

    public void onAppointments(ActionEvent actionEvent) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads reports screen
     * @param actionEvent actionEvent
     * @throws Exception exception error
     */

    public void onReports(ActionEvent actionEvent) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/reports.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Exits the program
     * @param actionEvent actionEvent
     */
    public void onExit(ActionEvent actionEvent) {
        System.exit(0);
    }


}
