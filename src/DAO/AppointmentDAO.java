package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Appointment;
import util.JDBC;
import util.TimeAndZone;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.Date;


/**
 *
 * Apppointment Data access object class
 * Any function that will interact with SQL will be found here
 */
public class AppointmentDAO {


    /**
     * SQL script top pull appointment data
     * and saves it to variables that can be used to create Appointment class object
     * @return appointmentList
     */
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

        /**
         * Lambda expression #1
         * Sorts the appointment List by comparing Appointment IDs
         */
        Collections.sort(appointmentList, (a1, a2) -> a1.getAppointmentId()-a2.getAppointmentId());
        return appointmentList;

    }

    /**
     * Grab all appointments filtered by current month
     * @return appointmentThisMonth
     */

    public static ObservableList getAppointmentsThisMonth() {
        String currentDate = TimeAndZone.getDate().toString();
        String yearMonth = currentDate.substring(0, 7);
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

                if (startTime.substring(0, 7).equals(yearMonth)) {
                    Appointment appointment = new Appointment(appointmentId, title, description, location, type, startTimeLocal, endTimeLocal, customerID, userID, contactID, contactName);
                    appointmentThisMonth.add(appointment);
                }
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
        return appointmentThisMonth;

    }

    /**
     * Creates time slots for appointment time
     * original time slots were written in UTC time, to reflect 8am-10pm EST business hour
     * @return availableTimeList
     */

    public static ObservableList availableTime() {
        ObservableList<String> availableTimeList = FXCollections.observableArrayList();

        String[] timeList = {"13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45", "00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00"};


        Arrays.stream(timeList).forEach(i -> {
            try {
                availableTimeList.add(TimeAndZone.timeslotConverter(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return availableTimeList;
    }

    /**
     *Receives variables from appointment form and loads it to the MySQL database
     */

    public static void addAppointment(String title, String description, String location, String type, String startTimeLocal, String endTimeLocal, int customerID, int userID, int contactID) {

        Integer appointment_ID = assignAppointmentID();
        Timestamp utcTime = TimeAndZone.getTimestamp();

        try {
            Statement stmt = JDBC.connection.createStatement();
            String q = "INSERT INTO appointments SET appointment_ID=" + appointment_ID + ", Title='" + title + "', Description='" + description + "', Location='" + location + "', Type='" + type + "', Start='" + startTimeLocal + "', End='" + endTimeLocal + "', Create_date='" + utcTime + "', Created_by='script', Last_update='" + utcTime + "', Last_Updated_By='script', Customer_ID=" + customerID + ", User_ID=" + userID + ", Contact_ID=" + contactID;
            stmt.executeUpdate(q);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Assings appointment ID
     * @return appointment_ID
     */
    private static Integer assignAppointmentID() {
        Integer appointment_ID = 1;
        try {
            Statement stmt = JDBC.connection.createStatement();
            String query = "SELECT appointment_ID FROM appointments ORDER BY Appointment_ID";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                while (rs.getInt("appointment_Id") == appointment_ID) {
                    appointment_ID++;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointment_ID;
    }


    /**
     * Parameters were received and are being exported to database
     * @param appointmentID appointment ID
     * @param title title
     * @param description description
     * @param location location
     * @param type type
     * @param startTimeLocal start time in local time zone
     * @param endTimeLocal end time in local time zone
     * @param customerID customer ID
     * @param userID userID
     * @param contactID contactID
     */
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

    /**
     * deletes selected appointment
     * @param appointment_id uses to define which appointment object to be deleted
     * @param type alerts the user the type of appointment to be deleted
     * @throws SQLException
     */
    public static void delAppointment(Integer appointment_id, String type) throws SQLException {
        try {
            Statement stmt = JDBC.connection.createStatement();
            String q = "DELETE FROM appointments WHERE Appointment_ID =" + appointment_id;
            stmt.executeUpdate(q);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Appointment has been cancelled");
            alert.setContentText("Appointment ID: " + appointment_id + "\nAppointment Type: " + type);
            alert.showAndWait();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Determines current week by using today's date in local timezone
     */

    public static ObservableList getAppointmentsThisWeek() {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        int weekOfYear = today.get(WeekFields.of(Locale.getDefault()).weekOfYear());


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
                LocalDate appointDay = LocalDate.parse(startTimeLocal.substring(0, 10));
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

    /**
     * Receives userID and use that as a key to look for all appointments for the customer
     * @param b used to define customerID
     * @return assocAppoint returns all associated appointments for customerID = b
     * @throws SQLException sql error
     */

    public static String associatedAppointment(int b) throws SQLException {
        String assocAppoint = "";
        Statement stmt = JDBC.connection.createStatement();
        String query = "SELECT appointment_ID FROM appointments WHERE Customer_ID =" + b;
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            rs.getInt(1);
            assocAppoint += "Y";
        }
        return assocAppoint;
    }

    /**
     * Returns boolean value after performing logical test appointment using start time and end time.
     * @param start
     * @param end
     * @return boolean validityCheck
     * @throws ParseException
     */
    public static boolean appointmentTimeValidityCheck(String start, String end) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date startDateTime = sdf.parse(start);
        java.util.Date endDateTime = sdf.parse(end);
        boolean validityCheck;

        if (startDateTime.after(endDateTime) || startDateTime.equals(endDateTime)) {
            validityCheck = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("Appointment end time cannot be the same as or before start time");
            alert.setContentText("Adjust your start time and/or end time");
            alert.showAndWait();

        } else {
            validityCheck = true;
        }
        return validityCheck;
    }

    /**
     * Returns boolean value after checking if the customer has any saved appointments that overlaps with the proposed appointment start and end time
     * @param customerID customerID
     * @param start appointment Start time
     * @param end appointment End time
     * @return overlapCheck boolean that returns after logical tests
     * @throws ParseException parse error
     */

    public static boolean customerOverlapCheck(int customerID, String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date newApptStartDateTime = sdf.parse(start);
        java.util.Date newApptEndDateTime = sdf.parse(end);
        boolean overlapCheck = false;

        try {
            String SQL = "SELECT Appointment_ID, Start, End FROM appointments WHERE Customer_ID=" + customerID;
            PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentID = rs.getInt(1);
                String startTime = rs.getString(2);
                String endTime = rs.getString(3);

                java.util.Date startDateTime = sdf.parse(startTime);
                java.util.Date endDateTime = sdf.parse(endTime);


                if ((newApptStartDateTime.before(endDateTime) && newApptStartDateTime.after(startDateTime))||(newApptEndDateTime.before(endDateTime) && newApptEndDateTime.after(startDateTime))) {
                    overlapCheck = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("Customer has another overlapping appointment");
                    alert.setContentText("Review Appointment ID: " + appointmentID + " and adjust either/both start time and end time.");
                    alert.showAndWait();
                } else {
                    overlapCheck = true;
                }
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
        return overlapCheck;
    }

    /**
     * Receives login time and check if there are any appointments starting in the next 15 minutes. Eligible appointments get added to the text string.
     * @param loginTime captured local time when user logged in successfully
     * @param userID used to identify user object
     * @return alertList is a string variable that lists any eligible appointments
     * @throws ParseException parse error
     */


    public static String upcomingAppointments(LocalDateTime loginTime, int userID) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String alertTime = loginTime.plusMinutes(15).toString();
        java.util.Date currentTimeFormatted = sdf.parse(loginTime.toString());
        java.util.Date alertTimeFormatted = sdf.parse(alertTime);
        String alertList = "";

        try {
            String SQL = "SELECT Appointment_ID, Start FROM appointments WHERE user_ID =" + userID;
            PreparedStatement ps = JDBC.connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                String appointmentId = rs.getString("Appointment_ID");
                String startTime = rs.getString("Start");

                DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date timestampLocal = dateFormatUTC.parse(startTime);

                if (timestampLocal.before(alertTimeFormatted) && timestampLocal.after(currentTimeFormatted)) {
                    alertList += "Appointment ID: " + appointmentId + "\t\tStart Time: " + startTime;
                }
            }
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
        return alertList;
    }


}
