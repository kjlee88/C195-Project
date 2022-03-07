package model;

public class User {

    private static int userID;
    private static String username;
    private static String password;

    public User(int userID, String username) {
        this.userID = userID;
        this.username = username;

        System.out.println("created");
    }


    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static int getUserID() {
        return userID;
    }

}
