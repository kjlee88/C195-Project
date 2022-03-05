package controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.ZoneId;
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
    private ComboBox userIdComboBox;
    @FXML
    private ComboBox contactComboBox;
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
    public ComboBox customerIdComboBox;



    @Override
    public void initialize (URL url, ResourceBundle rb) {
        customerIdComboBox.setItems(CustomerDAO.getCustomerID());

        startTimeComboBox.setItems(AppointmentDAO.availableTime());

        refreshAppointmentList();
    }





    public void refreshAppointmentList() {
        appointmentTable.setItems(AppointmentDAO.getAllAppointments());
        appointIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
    }


    public void onMonthly(ActionEvent actionEvent) {
    }

    public void onWeekly(ActionEvent actionEvent) {
    }


    public void onEditButton(ActionEvent actionEvent) {
    }

    public void onDelButton(ActionEvent actionEvent) {
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

    public void onSubmit(ActionEvent actionEvent) {
    }

    public void onCustomerIdCombo(ActionEvent actionEvent) {

    }

    public void onClear() {
        disableInput(true);
        appointId.setPromptText("Auto Gen- Disabled");
        appointId.setText("");
        titleInput.setText("");
        descriptionInput.setText("");
        locationInput.setText("");
        typeInput.setText("");
    }

    public void disableInput(boolean b) {
        titleInput.setDisable(b);
        descriptionInput.setDisable(b);
        locationInput.setDisable(b);
        userIdComboBox.setDisable(b);
        contactComboBox.setDisable(b);
        typeInput.setDisable(b);
        submit.setDisable(b);
        clear.setDisable(b);
        startDatePicker.setDisable(b);
        endDatePicker.setDisable(b);
        startTimeComboBox.setDisable(b);
        endTimeComboBox.setDisable(b);


    }
}
