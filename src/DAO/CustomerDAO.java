package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import util.JDBC;
import util.TimeAndZone;

import java.sql.*;

public class CustomerDAO {
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
        return customerList;
    }

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

    /* public static int findStateID(String state) {
        int stateID = 0;
        try {
            Statement stmt = JDBC.connection.createStatement();
            String q = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + state + "'";
            ResultSet rs = stmt.executeQuery(q);

            while (rs.next()) {
                stateID = (rs.getInt("Division_ID"));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return stateID;
    }*/



    public static void addCustomer(String name, String address, String postal, String phone, int countryID, int stateID) {
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

    public static void editCustomer(Integer customer_id, String name, String address, String postal, String phone, int countryID, int stateID){
        Timestamp utcTime = TimeAndZone.getTimestamp();
        try {
            Statement stmt = JDBC.connection.createStatement();
            String q = "UPDATE customers SET customer_Name='" + name + "', address='" + address + "', postal_code='" + postal + "', phone='" + phone + "', Last_update='" + utcTime + "', Last_Updated_By='script', Division_ID=" + stateID + " WHERE customer_id=" + customer_id;
            stmt.executeUpdate(q);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void delCustomer(Integer customer_ID) throws SQLException {
        try {
            Statement stmt = JDBC.connection.createStatement();
            String q = "DELETE FROM customers WHERE customer_id =" + customer_ID;
            stmt.executeUpdate(q);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static ObservableList<Integer> getCustomerID() {
        ObservableList<Integer> customerIdList = FXCollections.observableArrayList();
        try {
            String q = "SELECT customer_id from customers";
            PreparedStatement ps = JDBC.connection.prepareStatement(q);
            ResultSet rs = ps.executeQuery(q);
            while (rs.next()) {
                int customer_id = rs.getInt("Customer_Id");
                customerIdList.add(customer_id);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        customerIdList = customerIdList.sorted();
        return customerIdList;
    }

}

