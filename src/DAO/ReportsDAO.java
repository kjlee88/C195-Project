package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import util.JDBC;
import util.TimeAndZone;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class ReportsDAO {


    public static String report1() throws SQLException {
        String SQL = "SELECT COUNT(Appointment_ID) AS 'Count', Type , MONTH(Start) AS 'Month', YEAR(Start) AS 'Year' FROM appointments a GROUP BY Year, Month, Type";
        PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
        ResultSet rs = ps.executeQuery();

        String report = "'MM-YYYY'\t\t\t\t'# of Appointments'\t\t\t'Appointment Type'\n";

        while (rs.next()) {

            report += rs.getString(3) + "-" + rs.getString(4) + "\t\t\t\t\t" + rs.getString(1) + "\t\t\t\t\t\t\t\t" + rs.getString(2) + "\n";

        }  return report;

    }

    public static String report2() throws SQLException {
        String SQL = "SELECT COUNT(appointments.Appointment_ID) AS 'Count', MONTH(appointments.Start) AS 'Month', YEAR(appointments.Start) AS 'Year', countries.Country AS 'Country' FROM appointments JOIN customers ON customers.Customer_ID = appointments.Customer_ID JOIN first_level_divisions on first_level_divisions.Division_ID = customers.Division_ID JOIN countries on countries.Country_ID = first_level_divisions.Country_ID GROUP BY Year, Month, Country";
        PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
        ResultSet rs = ps.executeQuery();

        String report2 = "'MM-YYYY'\t\t\t\t'# of Appointments'\t\t\t'Customer Country'\n";

        while (rs.next()) {

            report2 += rs.getString(2) + "-" + rs.getString(3) + "\t\t\t\t\t" + rs.getString(1) + "\t\t\t\t\t\t\t\t" + rs.getString(4) + "\n";

        }  return report2;

    }

    public static String report3() throws SQLException, ParseException {
        String SQL = "SELECT contacts.Contact_name, appointments.Appointment_ID, appointments.Title, appointments.Type, appointments.description, appointments.Start, appointments.End, appointments.customer_ID FROM appointments JOIN contacts ON contacts.contact_ID = appointments.contact_ID ORDER BY Contact_name, Start";
        PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
        ResultSet rs = ps.executeQuery();

        String report3 = "";

        while (rs.next()) {

            String startTime = TimeAndZone.convertToLocalTime(rs.getString(6));
            String endTime = TimeAndZone.convertToLocalTime(rs.getString(7));

            report3 += "Contact: " + rs.getString(1) + "\t\t\t" + "| Appointment ID :" + rs.getString(2) + "\t\t" + "| Title: " + rs.getString(3) + "\t\t" + "| Type: " + rs.getString(4) + "\t\t\t" + "| Description: " + rs.getString(5) + "\t\t" + "| Start: " + startTime + "\t\t" + "| End: " + endTime + "\t\t" + "| Customer ID: " + rs.getString(8) + "\n";

        }  return report3;

    }
}


