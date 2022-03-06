package model;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String postal;
    private String phone;
    private String country;
    private String state;
    private int countryID;
    private int stateID;


    public Customer(int id, String name, String address, String postal, String phone, String country, int countryID, String state, int stateID) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.country = country;
        this.countryID = countryID;
        this.state = state;
        this.stateID = stateID;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostal() {
        return postal;
    }

    public String getPhone() {
        return phone;
    }


    public String getCountry(){
        return country;
    }

    public int getCountryID(){
        return countryID;
    }

    public String getState() {
        return state;
    }

    public int getStateID() {
        return stateID;
    }

    public Country getCountryObject(){
        Country c = new Country (getCountryID(),getCountry());
        return c;
    }

    public State getStateObject(){
        State s = new State (getStateID(),getState());
        return s;
    }


}