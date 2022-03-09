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


    /**
     * Constructor for Customer
     * @param id id
     * @param name name
     * @param address address
     * @param postal postal code
     * @param phone phone number
     * @param country country
     * @param countryID countryID
     * @param state state
     * @param stateID stateID
     */
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

    /**
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @return postal code
     */
    public String getPostal() {
        return postal;
    }

    /**
     *
     * @return phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @return Country
     */
    public String getCountry(){
        return country;
    }

    /**
     *
     * @return countryID
     */
    public int getCountryID(){
        return countryID;
    }

    /**
     *
     * @return Division
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @return divisionID
     */
    public int getStateID() {
        return stateID;
    }

    /**
     *
     * @return creates a Country object and returns it
     */
    public Country getCountryObject(){
        Country c = new Country (getCountryID(),getCountry());
        return c;
    }

    /**
     *
     * @return creates a State object and returns it
     */
    public State getStateObject(){
        State s = new State (getStateID(),getState());
        return s;
    }


}