package model;

public class User {

    private static int userID;
    private static String username;
    private static String password;

    /**
     * Constructor for User object
     * @param userID userID
     * @param username username
     */
    public User(int userID, String username) {
        this.userID = userID;
        this.username = username;

    }

    /**
     *
     * @return username
     */
    public static String getUsername() {
        return username;
    }

    /**
     * set User object's username
     * @param username
     */
    public static void setUsername(String username) {
        User.username = username;
    }

    /**
     *
     * @return userID
     */
    public static int getUserID() {
        return userID;
    }

}
