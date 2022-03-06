package model;

import DAO.ContactDAO;

public class Appointment {
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private String startTimeLocal;
    private String endTimeLocal;
    private int customerID;
    private int userID;
    private int contactID;
    private String contactName;



    public Appointment(int appointmentId, String title, String description, String location, String type, String startTimeLocal, String endTimeLocal, int customerID, int userID, int contactID, String contactName) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTimeLocal = startTimeLocal;
        this.endTimeLocal = endTimeLocal;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.contactName = contactName;
    }

    public int getAppointmentId() {return appointmentId;}

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getStartTimeLocal() {return startTimeLocal;}

    public String getEndTimeLocal() {return endTimeLocal;}

    public int getCustomerID() {return customerID;}

    public int getUserID() {
        return userID;
    }

    public int getContactID() {
        return contactID;
    }

    public String getContactName() { return contactName; }

    public Contact getContactObject() {

        Contact c = new Contact(getContactID(), getContactName());
        return c;

    }

}
