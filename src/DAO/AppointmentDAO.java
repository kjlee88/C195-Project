package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import util.JDBC;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class AppointmentDAO {

    private static ZoneId userZoneID = ZoneId.systemDefault();

    public static ObservableList getAllAppointments() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT * FROM appointments";
            PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp startTime = rs.getTimestamp("Start");
                Timestamp endTime = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_id");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                Appointment appointment = new Appointment(appointmentId, title, description, location, type, startTime, endTime, customerID, userID, contactID);
                appointmentList.add(appointment);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appointmentList;

    }

    public static ObservableList availableTime() {
        DateFormat dateFormatUTC = new SimpleDateFormat("HH:mm");
        DateFormat dateFormatLocal = new SimpleDateFormat("HH:mm");
        ObservableList<String> availableTimeList = FXCollections.observableArrayList();
        dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormatLocal.setTimeZone(TimeZone.getTimeZone(userZoneID));
        //System.out.println(LocalTime.now());
        //System.out.println(Instant.now());


        //SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        //System.out.println(LocalTime.now());

        String[] timeList = {"13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45", "00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00" };

        for (int i = 0; i < timeList.length; i++) {
            String time = timeList[i];
            try {
                // Converting time in String to time in UTC time HH:MM format
                Date timeInUTC = dateFormatUTC.parse(time);

                // Converting UTC time to User Time, then return as String format
                String timeInLocal = dateFormatLocal.format(timeInUTC);

                availableTimeList.add(timeInLocal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return availableTimeList;
    }
}
