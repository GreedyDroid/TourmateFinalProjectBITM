package com.example.sayed.tourmate.login_signup;

/**
 * Created by nurud on 10/13/2017.
 */

public class User {
    private String userName;
    private String userEmail;
    private String userPhoneNo;
    private String userFireBaseID;

    public User(String userName, String userEmail, String userPhoneNo, String userFireBaseID) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoneNo = userPhoneNo;
        this.userFireBaseID = userFireBaseID;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getUserFireBaseID() {
        return userFireBaseID;
    }

    public void setUserFireBaseID(String userFireBaseID) {
        this.userFireBaseID = userFireBaseID;
    }
}
