package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import DAO.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.Appointment;
import model.Contact;
import util.TimeAndZone;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class Appointments implements Initializable {
    @FXML
    private RadioButton weekly;
    @FXML
    private ToggleGroup scheduleView;
    @FXML
    private RadioButton monthly;
    @FXML
    private Button clear;
    @FXML
    private TableView appointmentTable;
    @FXML
    private TableColumn appointIdCol;
    @FXML
    private TableColumn titleCol;
    @FXML
    private TableColumn descriptionCol;
    @FXML
    private TableColumn locationCol;
    @FXML
    private TableColumn contactCol;
    @FXML
    private TableColumn typeCol;
    @FXML
    private TableColumn startTimeCol;
    @FXML
    private TableColumn endTimeCol;
    @FXML
    private TableColumn customerIdCol;
    @FXML
    private TableColumn userIdCol;
    @FXML
    private Button editButton;
    @FXML
    private Button delButton;
    @FXML
    private Button newButton;
    @FXML
    private Button exit;
    @FXML
    private TextField appointId;
    @FXML
    private TextField titleInput;
    @FXML
    private TextField descriptionInput;
    @FXML
    private TextField locationInput;
    @FXML
    private ComboBox<Integer> userIdComboBox;
    @FXML
    private ComboBox<Contact> contactComboBox;
    @FXML
    private Button submit;
    @FXML
    private TextField typeInput;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox startTimeComboBox;
    @FXML
    private ComboBox endTimeComboBox;
    @FXML
    public ComboBox<Integer> customerIdComboBox;
    public static boolean validityCheck;
    public static boolean overlapCheck;
    public static boolean requiredInputCheck;



    @Override
    public void initialize (URL url, ResourceBundle rb) {
        customerIdComboBox.setItems(CustomerDAO.getCustomerID());
        startTimeComboBox.setItems(AppointmentDAO.availableTime());
        endTimeComboBox.setItems(AppointmentDAO.availableTime());
        userIdComboBox.setItems(UserDAO.getUserID());
        contactComboBox.setItems(ContactDAO.getAllContacts());
        refreshAppointmentList();
        disableInput(true);
    }





    public void refreshAppointmentList() {
        appointmentTable.setItems(AppointmentDAO.getAllAppointments());
        appointIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTimeLocal"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTimeLocal"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }


    public void onMonthly(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentDAO.getAppointmentsThisMonth());
        refreshAppointmentList();
    }

    public void onWeekly(ActionEvent actionEvent) throws ParseException {
        appointmentTable.setItems(AppointmentDAO.getAppointmentsThisWeek());
        refreshAppointmentList();
    }


    public void onEditButton(ActionEvent actionEvent) {
        disableInput(false);
        Appointment appointmentSelected = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        Pair<LocalDate, String> start = TimeAndZone.timestampToDateAndTime(appointmentSelected.getStartTimeLocal());
        Pair<LocalDate, String> end = TimeAndZone.timestampToDateAndTime(appointmentSelected.getEndTimeLocal());

        appointId.setText(Integer.toString(appointmentSelected.getAppointmentId()));
        titleInput.setText(appointmentSelected.getTitle());
        descriptionInput.setText(appointmentSelected.getDescription());
        locationInput.setText(appointmentSelected.getLocation());
        contactComboBox.setValue(appointmentSelected.getContactObject());
        typeInput.setText(appointmentSelected.getType());
        startDatePicker.setValue(start.getKey());
        startTimeComboBox.setValue(start.getValue());
        endDatePicker.setValue(end.getKey());
        endTimeComboBox.setValue(end.getValue());
        customerIdComboBox.setValue(appointmentSelected.getCustomerID());
        userIdComboBox.setValue(appointmentSelected.getUserID());

    }

    public void onDelButton(ActionEvent actionEvent) throws SQLException {
        Appointment appointmentSelected = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        int appointment_id = appointmentSelected.getAppointmentId();
        AppointmentDAO.delAppointment(appointment_id);
        appointmentTable.setItems(AppointmentDAO.getAllAppointments());
        refreshAppointmentList();
        onClear();
    }

    public void onNewButton(ActionEvent actionEvent) {
        disableInput(false);
    }

    public void onExit(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onSubmit(ActionEvent actionEvent) throws ParseException {
        if (requiredInputCheck()) {
            String title = titleInput.getText();
            String description = descriptionInput.getText();
            String location = locationInput.getText();
            int contactID = contactComboBox.getValue().getContactID();
            String type = typeInput.getText();
            String startDate = startDatePicker.getValue().toString();
            String startTime = startTimeComboBox.getValue().toString();
            String endDate = endDatePicker.getValue().toString();
            String endTime = endTimeComboBox.getValue().toString();
            int customerID = customerIdComboBox.getValue();
            int userID = userIdComboBox.getValue();

            String startTimeLocal = TimeAndZone.generateUTCTimestamp(startDate, startTime);
            String endTimeLocal = TimeAndZone.generateUTCTimestamp(endDate, endTime);

            validityCheck = AppointmentDAO.appointmentTimeValidityCheck(startTimeLocal, endTimeLocal);
            overlapCheck = AppointmentDAO.customerOverlapCheck(customerID, startTimeLocal);

            if (validityCheck && overlapCheck && requiredInputCheck) {

                if (appointId.getText().isEmpty()) {
                    AppointmentDAO.addAppointment(title, description, location, type, startTimeLocal, endTimeLocal, customerID, userID, contactID);
                } else {
                    int appointmentID = Integer.parseInt(appointId.getText());
                    AppointmentDAO.editAppointment(appointmentID, title, description, location, type, startTimeLocal, endTimeLocal, customerID, userID, contactID);
                }

                appointmentTable.setItems(AppointmentDAO.getAllAppointments());
                refreshAppointmentList();
                onClear();
            }
        }
    }


    public void onClear() {
        disableInput(true);
        appointId.setPromptText("Auto Gen- Disabled");
        appointId.setText("");
        titleInput.setText("");
        descriptionInput.setText("");
        locationInput.setText("");
        typeInput.setText("");
        contactComboBox.setValue(null);
        customerIdComboBox.setValue(null);
        userIdComboBox.setValue(null);
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }

    public void disableInput(boolean b) {
        titleInput.setDisable(b);
        descriptionInput.setDisable(b);
        locationInput.setDisable(b);
        userIdComboBox.setDisable(b);
        contactComboBox.setDisable(b);
        typeInput.setDisable(b);
        clear.setDisable(b);
        submit.setDisable(b);
        startDatePicker.setDisable(b);
        endDatePicker.setDisable(b);
        startTimeComboBox.setDisable(b);
        endTimeComboBox.setDisable(b);
        customerIdComboBox.setDisable(b);
    }


    private boolean requiredInputCheck() {
        if (titleInput.getText().isEmpty() || descriptionInput.getText().isEmpty() || locationInput.getText().isEmpty() || contactComboBox.getValue().equals(null) || typeInput.getText().isEmpty() || startDatePicker.getValue().equals(null) || startTimeComboBox.getValue().equals(null) || endDatePicker.getValue().equals(null) || endTimeComboBox.getValue().equals(null) || customerIdComboBox.getValue().equals(null) || userIdComboBox.getValue().equals(null)) {
            requiredInputCheck = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Missing input field(s) found");
            alert.setContentText("All fields are required before submitting");
            alert.showAndWait();
        } else {
            requiredInputCheck = true;

        } return requiredInputCheck;
    }

}
