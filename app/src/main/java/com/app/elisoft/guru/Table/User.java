package com.app.elisoft.guru.Table;


public class User {

    private String uid;
    private String email;
    private String password;
    private long lastLogin;

    public User(String uid, String email, String password, long lastLogin) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.lastLogin = lastLogin;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
