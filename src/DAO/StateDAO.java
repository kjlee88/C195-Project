package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.State;
import util.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StateDAO {
    public static ObservableList<State> getAllStates(int countryID) {
        ObservableList<State> stateList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
            ps.setInt(1, countryID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int stateID = rs.getInt("Division_ID");
                String stateName = rs.getString("Division");
                State s = new State(stateID, stateName);
                stateList.add(s);
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return stateList;
    }
}
