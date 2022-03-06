package util;

import javafx.util.Pair;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Kyung Jun Lee
 */

public class TimeAndZone {

    private static ZoneId userZoneID = ZoneId.systemDefault();

    public static java.sql.Timestamp getTimestamp() {
        ZoneId zoneid = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneid);
        java.sql.Timestamp timeStamp = Timestamp.valueOf(localDateTime);
        return timeStamp;
    }

    public static java.sql.Date getDate() {
        java.sql.Date date = java.sql.Date.valueOf(LocalDate.now());
        return date;
    }


    public static String generateUTCTimestamp(String date, String time) throws ParseException {
        String timestamp = date + " " + time + ":00";
        DateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date timestampLocal = dateFormatLocal.parse(timestamp);
        ZonedDateTime zoneTimeUTC = timestampLocal.toInstant().atZone(ZoneOffset.UTC);
        String timeUTC = zoneTimeUTC.toString();
        String formattedTimeUTC = timeUTC.replace("T"," ") + ":00";
        String formattedTimeUTC2 = formattedTimeUTC.replace("Z","");

        return formattedTimeUTC2;
    }

    public static String convertToLocalTime(String timestamp) throws ParseException {
        DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormatLocal.setTimeZone(TimeZone.getTimeZone(userZoneID));

        Date timestampUTC = dateFormatUTC.parse(timestamp);
        String timeLocal = dateFormatLocal.format(timestampUTC);

        return timeLocal;
    }

    public static String timeslotConverter(String time) throws ParseException {
        DateFormat timeFormatUTC = new SimpleDateFormat("HH:mm");
        DateFormat timeFormatLocal = new SimpleDateFormat("HH:mm");
        timeFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        timeFormatLocal.setTimeZone(TimeZone.getTimeZone(userZoneID));
        Date timeInUTC = timeFormatUTC.parse(time);
        String timeInLocal = timeFormatLocal.format(timeInUTC);
        return timeInLocal;

    }

    public static Pair<LocalDate, String> timestampToDateAndTime(String timestamp) {
        String[] split = timestamp.split("\\s+");
        LocalDate date = LocalDate.parse(split[0]);
        String time = split[1].substring(0,5);
        return new Pair<LocalDate, String>(date, time);
    }
}