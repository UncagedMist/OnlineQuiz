package com.techbytecare.kk.onlinequiz.Model;

/**
 * Created by kundan on 12/24/2017.
 */

public class User {
    private String userName;
    private String password;
    private String email;
    private String secureCode;

    public User() {
    }

    public User(String userName, String password, String email, String secureCode) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.secureCode = secureCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }
}
