package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Customer;
import util.JDBC;
import util.TimeAndZone;

import java.sql.*;
import java.util.Collections;

public class CustomerDAO {

    /**
     * gets all customer objects from database
     * @return a list of customers
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, countries.Country, countries.country_id, first_level_divisions.Division, customers.Division_ID" + "\n" + "FROM customers" + "\n" + "LEFT JOIN first_level_divisions ON customers.Division_ID=first_level_divisions.Division_ID" + "\n" + "LEFT JOIN countries ON first_level_divisions.Country_ID=countries.Country_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postal = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                String country = rs.getString("Country");
                int countryID = rs.getInt("country_id");
                String state = rs.getString("Division");
                int stateID = rs.getInt("Division_ID");
                Customer customer = new Customer(id, name, address, postal, phone, country, countryID, state, stateID);
                customerList.add(customer);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        Collections.sort(customerList, (c1, c2) -> c1.getId()-c2.getId());
        return customerList;
    }

    /**
     * Assigns customerID to newly added customer object
     * @return customerID
     */
    public static Integer assignCustomerID(){
        Integer customer_ID = 1;
        try {
            Statement stmt = JDBC.connection.createStatement();
            String query = "SELECT customer_Id FROM customers ORDER BY customer_Id";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                while (rs.getInt("customer_Id") == customer_ID) {
                    customer_ID++;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customer_ID;
    }

    /**
     * Add a new customer to the database using parameters passed by Customers.onSubmit()
     * @param name name
     * @param address address
     * @param postal postal code
     * @param phone phone number
     * @param stateID division ID
     */

    public static void addCustomer(String name, String address, String postal, String phone, int stateID) {
        Integer customer_ID = assignCustomerID();
        Timestamp utcTime = TimeAndZone.getTimestamp();
        try {
            Statement stmt = JDBC.connection.createStatement();
            String q = "INSERT INTO customers SET customer_ID=" + customer_ID + ", customer_Name='" + name + "', address='" + address + "', postal_code='" + postal + "', phone='" + phone + "', Create_date='" + utcTime + "', Created_by='script', Last_update='" + utcTime + "', Last_Updated_By='script', Division_ID=" + stateID;
            stmt.executeUpdate(q);
            System.out.println("Customer was added");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Edit any columns from an existing customer row
     * @param customer_id customerID, immutable, defines the customer object being edited
     * @param name customer name
     * @param address address
     * @param postal postal code
     * @param phone phone number
     * @param stateID division ID
     */

    public static void editCustomer(Integer customer_id, String name, String address, String postal, String phone, int stateID){
        Timestamp utcTime = TimeAndZone.getTimestamp();
        try {
            Statement stmt = JDBC.connection.createStatement();
            String q = "UPDATE customers SET customer_Name='" + name + "', address='" + address + "', postal_code='" + postal + "', phone='" + phone + "', Last_update='" + utcTime + "', Last_Updated_By='script', Division_ID=" + stateID + " WHERE customer_id=" + customer_id;
            stmt.executeUpdate(q);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Deletes selected customer from the database
     * @param customer_ID
     * @throws SQLException
     */
    public static void delCustomer(Integer customer_ID) {
        try {
            Statement stmt = JDBC.connection.createStatement();
            String q = "DELETE FROM customers WHERE customer_id =" + customer_ID;
            stmt.executeUpdate(q);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Customer was successfully deleted");
            alert.setContentText("");
            alert.showAndWait();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Creates list of all customer IDs
     * @return customerIdList
     */
    public static ObservableList<Integer> getCustomerID() {
        ObservableList<Integer> customerIdList = FXCollections.observableArrayList();
        try {
            String q = "SELECT customer_id from customers";
            PreparedStatement ps = JDBC.connection.prepareStatement(q);
            ResultSet rs = ps.executeQuery(q);
            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                customerIdList.add(customerID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        customerIdList = customerIdList.sorted();
        return customerIdList;
    }

}

