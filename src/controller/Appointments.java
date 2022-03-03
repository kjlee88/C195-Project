package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Appointments {
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
    private ComboBox customerIdComboBox;



    public void onMonthly(ActionEvent actionEvent) {
    }

    public void onWeekly(ActionEvent actionEvent) {
    }

    public void onClear(ActionEvent actionEvent) {
    }

    public void onEditButton(ActionEvent actionEvent) {
    }

    public void onDelButton(ActionEvent actionEvent) {
    }

    public void onNewButton(ActionEvent actionEvent) {
    }

    public void onExit(ActionEvent actionEvent) {
    }

    public void onSubmit(ActionEvent actionEvent) {
    }
}
