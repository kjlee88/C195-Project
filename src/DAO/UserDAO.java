package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import util.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static ObservableList<Integer> getUserID() {
        ObservableList<Integer> userIdList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT * FROM users";
            PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int userID = rs.getInt("User_ID");
                userIdList.add(userID);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userIdList;
    }

}
