package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.StateDAO;
import DAO.AppointmentDAO;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.State;
import util.JDBC;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Customer controller class.
 * shows customers. provides add/update/delete user interfaces
 * @author Kyung Jun Lee
 */

public class Customers implements Initializable {
    @FXML
    private Button edit;
    @FXML
    private Button clear;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn nameCol;
    @FXML
    private TableColumn addressCol;
    @FXML
    private TableColumn postalCol;
    @FXML
    private TableColumn phoneCol;
    @FXML
    private TableColumn countryCol;
    @FXML
    private TableColumn stateCol;
    @FXML
    private TableView customerTable;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField addressInput;
    @FXML
    private TextField postalInput;
    @FXML
    private TextField phoneInput;
    @FXML
    public ComboBox<Country> countryCombo;
    @FXML
    private ComboBox<State> stateCombo;
    @FXML
    private Button submit;
    @FXML
    private TextField id;

    /**
     * Initializes Customer management screen
     * populates Country combobox
     * @param url
     * @param rb
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        countryCombo.setItems(CountryDAO.getAllCountries());
        refreshCustomerList();
        disableInput(true);
    }


    /**
     * Refreshes the table with customer list
     */
    public void refreshCustomerList() {
        customerTable.setItems(CustomerDAO.getAllCustomers());
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCol.setCellValueFactory(new PropertyValueFactory<>("postal"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
    }


    /**
     * Parses selected customer's information to the input fields
     * @param actionEvent
     */

    public void onEdit(ActionEvent actionEvent) {
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
        } else {
        disableInput(false);
        Customer customerSelected = (Customer) customerTable.getSelectionModel().getSelectedItem();
        id.setText(Integer.toString(customerSelected.getId()));
        nameInput.setText(customerSelected.getName());
        addressInput.setText(customerSelected.getAddress());
        postalInput.setText(customerSelected.getPostal());
        phoneInput.setText(customerSelected.getPhone());
        countryCombo.setValue(customerSelected.getCountryObject());
        stateCombo.setValue(customerSelected.getStateObject());
        }
    }


    /**
     * Deletes seleccted customer
     * Checks if the customer has associated appointments
     * @param actionEvent
     * @throws SQLException
     */


    public void onDel(ActionEvent actionEvent) throws SQLException {
        if (customerTable.getSelectionModel().getSelectedItem() == null) {
        } else {
            Customer customerSelected = (Customer) customerTable.getSelectionModel().getSelectedItem();
            int customer_id = customerSelected.getId();
            if (AppointmentDAO.associatedAppointment(customer_id).isEmpty()) {
                CustomerDAO.delCustomer(customer_id);
                refreshCustomerList();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Customer Delete Error");
                alert.setHeaderText("Customer has an associated appointments");
                alert.setContentText("You must delete associated appointments first");
                alert.showAndWait();
            }
            refreshCustomerList();
        }
    }

    /**
     * Activates customer add form on the right of the screen.
     * @param actionEvent
     */
    public void onAdd(ActionEvent actionEvent) {
        disableInput(false);
    }

    /**
     * Once Country is selected, it pulls associated Division information
     * @param actionEvent
     */

    public void onCountryCombo(ActionEvent actionEvent){
        Country c = countryCombo.getValue();
        stateCombo.setItems(StateDAO.getAllStates(c.getCountryID()));
    }


    /**
     * Validates all inputs and saves them under Customer class
     * @param actionEvent Saves a new customer with information provided on the add form.
     */
    public void onSubmit(ActionEvent actionEvent) {
        if (requiredInputCheck()) {
            if (validphonenumber()) {
                String name = nameInput.getText();
                String address = addressInput.getText();
                String postal = postalInput.getText();
                String phone = phoneInput.getText();
                int countryID = countryCombo.getValue().getCountryID();
                int stateID = stateCombo.getValue().getStateID();
                if (id.getText().isEmpty()) {
                    CustomerDAO.addCustomer(name, address, postal, phone, stateID);
                    onClear();
                    refreshCustomerList();
                } else {
                    int customer_ID = Integer.parseInt(id.getText());
                    CustomerDAO.editCustomer(customer_ID, name, address, postal, phone, stateID);
                    onClear();
                    refreshCustomerList();
                }
            }
        }
    }


    /**
     * Takes the user back to the main menu
     * @param actionEvent Returns to the menu options.
     * @throws IOException Missing the menu screen.
     */
    public void onMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void onClear() {
        disableInput(true);
        id.setPromptText("Auto Gen- Disabled");
        id.setText("");
        nameInput.setText("");
        addressInput.setText("");
        postalInput.setText("");
        phoneInput.setText("");
        stateCombo.setValue(null);
    }

    public void disableInput(boolean b) {
        nameInput.setDisable(b);
        addressInput.setDisable(b);
        postalInput.setDisable(b);
        phoneInput.setDisable(b);
        countryCombo.setDisable(b);
        stateCombo.setDisable(b);
        submit.setDisable(b);
        clear.setDisable(b);

    }

    private boolean requiredInputCheck() {
        boolean requiredInputCheck = false;
        if (nameInput.getText().isEmpty() || addressInput.getText().isEmpty() || postalInput.getText().isEmpty() || phoneInput.getText().isEmpty() || countryCombo.getValue() == null || stateCombo.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Missing input field(s) found");
            alert.setContentText("All fields are required before submitting");
            alert.showAndWait();
        } else {
            requiredInputCheck = true;
        } return requiredInputCheck;
    }


    /**
     * Part of input validators. Looks for any unnatural inputs for phone number
     * Defines only allowable characters are digits and dashes
     * @return boolean validPhoneNumber
     */
    private boolean validphonenumber() {
        boolean validphonenumber = false;
        if (phoneInput.getText().matches("^[0-9.-]+$")) {
            validphonenumber = true;
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Phone number is invalid");
            alert.setContentText("Only numbers and dashes are allowed");
            alert.showAndWait();
            validphonenumber = false;
        } return validphonenumber;
    }
}
