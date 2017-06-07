package com.app.elisoft.guru.Table;


import java.io.Serializable;

public class User implements Serializable {

    private String uid;
    private String email;
    private String password;
    private long lastLogin;
    private String iconURL;
    private String myWins;
    private String myLoses;
    private String myDraws;

    public void setMyDraws(String myDraws) {
        this.myDraws = myDraws;
    }

    public User(){
        uid = "";
        email = "";
        password = "";
        lastLogin = 0;
        iconURL = null;
        myWins = "0";
        myLoses = "0";
        myDraws = "0";
    }

    public User(User user){
        uid = user.getUid();
        email = user.getEmail();
        password = user.getPassword();
        lastLogin = user.getLastLogin();
        iconURL = user.getIconURL();
        myWins = user.getMyWins();
        myLoses = user.getMyLoses();
        myDraws = user.getMyDraws();
    }

    public User(String uid, String email, String password, long lastLogin) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.lastLogin = lastLogin;
        this.iconURL = null;
    }

    public User(String uid, String email, String password, long lastLogin, String iconURL) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.lastLogin = lastLogin;
        this.iconURL = iconURL;
    }

    public User(String uid, String email, String password, long lastLogin, String iconURL, String myWins, String myLoses, String myDraws) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.lastLogin = lastLogin;
        this.iconURL = iconURL;
        this.myWins = myWins;
        this.myLoses = myLoses;
        this.myDraws = myDraws;
    }

    public String getMyWins() {
        return myWins;
    }

    public void setMyWins(String myWins) {
        this.myWins = myWins;
    }

    public String getMyLoses() {
        return myLoses;
    }

    public void setMyLoses(String myLoses) {
        this.myLoses = myLoses;
    }

    public String getMyDraws() {
        return myDraws;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
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
