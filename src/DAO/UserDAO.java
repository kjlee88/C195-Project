package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import model.Country;
import model.User;
import util.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static User currentUser = null;
    public static int userID = 0;

    /**
     * Get all user IDs from the database
     * @return userIdList
     */

    public static ObservableList<Integer> getUserID() {
        ObservableList<Integer> userIdList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT * FROM users";
            PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userID = rs.getInt("User_ID");
                userIdList.add(userID);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userIdList;
    }


    /**
     * Uses the provided username to find the password from the database, then compare this value to provided password
     * @param username username
     * @param pass password
     * @return boolean if password found from database and provided password are exact match or not
     * @throws SQLException sql error
     */
    public static boolean verifyUserLogin(String username, String pass) {
        try {
            String sql = "SELECT * FROM users WHERE User_Name='" + username + "'";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (pass.matches(rs.getString("Password"))) {
                    userID = rs.getInt("User_ID");
                    User user = new User(rs.getInt("User_ID"), rs.getString("User_Name"));
                    //int curruntUserID = rs.getInt("User_ID");
                    currentUser = user;
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
