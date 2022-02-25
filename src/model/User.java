package model;

public class User {

    private static int userID;
    private static String userName;
    private static String password;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        User.userName = userName;
    }
}
