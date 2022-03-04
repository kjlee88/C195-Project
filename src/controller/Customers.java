package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.StateDAO;
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



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        countryCombo.setItems(CountryDAO.getAllCountries());
        refreshCustomerList();

    }

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





    public void onEdit(ActionEvent actionEvent) {
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



    public void onDel(ActionEvent actionEvent) throws SQLException {
        Customer customerSelected = (Customer) customerTable.getSelectionModel().getSelectedItem();
        int customer_id = customerSelected.getId();
        CustomerDAO.delCustomer(customer_id);
        refreshCustomerList();
        onClear();
    }

    /**
     *
     *
     * @param actionEvent Activates customer add form on the right of the screen.
     */
    public void onAdd(ActionEvent actionEvent) {
        disableInput(false);
    }

    public void onCountryCombo(ActionEvent actionEvent){
        Country c = countryCombo.getValue();
        System.out.println(c.getCountryID());
        stateCombo.setItems(StateDAO.getAllStates(c.getCountryID()));
    }



    /**
     *
     * @param actionEvent Saves a new customer with information provided on the add form.
     */
    public void onSubmit(ActionEvent actionEvent) {
        String name = nameInput.getText();
        String address = addressInput.getText();
        String postal = postalInput.getText();
        String phone = phoneInput.getText();
        String country = countryCombo.getValue().toString();
        String state = stateCombo.getValue().toString();
        if (id.getText().isEmpty()) {
            CustomerDAO.addCustomer(name, address, postal, phone, country, state);
        } else {
            int customer_ID = Integer.parseInt(id.getText());
            CustomerDAO.editCustomer(customer_ID, name, address, postal, phone, country, state);
        }
        refreshCustomerList();
        onClear();

    }


    /**
     *
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
}
