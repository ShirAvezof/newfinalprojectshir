package com.example.finalprojectshir2;

public class User {
    private String userName;
    private String userPass;
    private String userPhone;

    public User(String userName, String userPass, String userPhone) {
        this.userName = userName;
        this.userPass = userPass;
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", userPhone='" + userPhone + '\'' +
                '}';
    }
}
