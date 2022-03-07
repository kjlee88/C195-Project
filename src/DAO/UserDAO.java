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
    //static User currentUser = null;

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


    public static boolean verifyUserLogin(String username, String pass) throws SQLException {
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
