package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import util.JDBC;
import util.TimeAndZone;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;
import java.util.TimeZone;

public class AppointmentDAO {




    public static ObservableList getAllAppointments() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try {
            String SQL = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.User_ID, appointments.Contact_ID, contacts.contact_Name FROM appointments JOIN contacts ON appointments.contact_ID = contacts.contact_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String startTime = rs.getString("Start");
                String endTime = rs.getString("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                String startTimeLocal = TimeAndZone.convertToLocalTime(startTime);
                String endTimeLocal = TimeAndZone.convertToLocalTime(endTime);


                Appointment appointment = new Appointment(appointmentId, title, description, location, type, startTimeLocal, endTimeLocal, customerID, userID, contactID, contactName);
                appointmentList.add(appointment);
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
        return appointmentList.sorted();

    }

    public static ObservableList getAppointmentsThisMonth() {
        String currentDate = TimeAndZone.getDate().toString();
        String yearMonth = currentDate.substring(0,7);
        ObservableList<Appointment> appointmentThisMonth = FXCollections.observableArrayList();

        try {
            String SQL = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.User_ID, appointments.Contact_ID, contacts.contact_Name FROM appointments JOIN contacts ON appointments.contact_ID = contacts.contact_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String startTime = rs.getString("Start");
                String endTime = rs.getString("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                String startTimeLocal = TimeAndZone.convertToLocalTime(startTime);
                String endTimeLocal = TimeAndZone.convertToLocalTime(endTime);

                if (startTime.substring(0,7).equals(yearMonth)) {
                    Appointment appointment = new Appointment(appointmentId, title, description, location, type, startTimeLocal, endTimeLocal, customerID, userID, contactID, contactName);
                    appointmentThisMonth.add(appointment);
                }
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
        return appointmentThisMonth;

    }


    public static ObservableList availableTime() {
        ObservableList<String> availableTimeList = FXCollections.observableArrayList();

        String[] timeList = {"13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45", "00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00" };

        for (int i = 0; i < timeList.length; i++) {
            String time = timeList[i];
            try {
                availableTimeList.add(TimeAndZone.timeslotConverter(time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        availableTimeList = availableTimeList.sorted();
        return availableTimeList;
    }

    public static void addAppointment(String title, String description, String location, String type, String startTimeLocal, String endTimeLocal, int customerID, int userID, int contactID) {
        Integer appointment_ID = assignAppointmentID();
        Timestamp utcTime = TimeAndZone.getTimestamp();

        try {
            Statement stmt = JDBC.connection.createStatement();
            String q = "INSERT INTO appointments SET appointment_ID=" + appointment_ID + ", Title='" + title + "', Description='" + description + "', Location='" + location + "', Type='" + type + "', Start='" + startTimeLocal + "', End='" + endTimeLocal + "', Create_date='" + utcTime + "', Created_by='script', Last_update='" + utcTime + "', Last_Updated_By='script', Customer_ID=" + customerID + ", User_ID=" + userID + ", Contact_ID=" + contactID;
            stmt.executeUpdate(q);
            System.out.println("Appointment was added");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static Integer assignAppointmentID() {
        Integer appointment_ID = 1;
        try {
            Statement stmt = JDBC.connection.createStatement();
            String query = "SELECT appointment_ID FROM appointments ORDER BY Appointment_ID";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                while (rs.getInt("appointment_Id") == appointment_ID) {
                    appointment_ID++;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointment_ID;
    }

    public static void editAppointment(int appointmentID, String title, String description, String location, String type, String startTimeLocal, String endTimeLocal, int customerID, int userID, int contactID) {

        Timestamp utcTime = TimeAndZone.getTimestamp();

        try {
            Statement stmt = JDBC.connection.createStatement();
            String q = "UPDATE appointments SET Title='" + title + "', Description='" + description + "', Location='" + location + "', Type='" + type + "', Start='" + startTimeLocal + "', End='" + endTimeLocal + "', Last_update='" + utcTime + "', Last_Updated_By='script', Customer_ID=" + customerID + ", User_ID=" + userID + ", Contact_ID=" + contactID + " WHERE Appointment_ID=" + appointmentID;
            stmt.executeUpdate(q);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void delAppointment(Integer appointment_id) throws SQLException {
        try {
            Statement stmt = JDBC.connection.createStatement();
            String q = "DELETE FROM appointments WHERE Appointment_ID =" + appointment_id;
            stmt.executeUpdate(q);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static ObservableList getAppointmentsThisWeek() throws ParseException {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        int weekOfYear = today.get(WeekFields.of(Locale.getDefault()).weekOfYear());


        System.out.println(weekOfYear);

        ObservableList<Appointment> appointmentThisWeek = FXCollections.observableArrayList();

        try {
            String SQL = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.User_ID, appointments.Contact_ID, contacts.contact_Name FROM appointments JOIN contacts ON appointments.contact_ID = contacts.contact_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String startTime = rs.getString("Start");
                String endTime = rs.getString("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                String startTimeLocal = TimeAndZone.convertToLocalTime(startTime);
                String endTimeLocal = TimeAndZone.convertToLocalTime(endTime);
                LocalDate appointDay = LocalDate.parse(startTimeLocal.substring(0,10));
                //java.util.Date appointmentDate = dateFormatLocal.parse(startTimeLocal);
                int appointWeekOfYear = appointDay.get(WeekFields.of(Locale.getDefault()).weekOfYear());

                if (appointWeekOfYear == weekOfYear) {
                    Appointment appointment = new Appointment(appointmentId, title, description, location, type, startTimeLocal, endTimeLocal, customerID, userID, contactID, contactName);
                    appointmentThisWeek.add(appointment);
                }
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
        return appointmentThisWeek;
    }
}
