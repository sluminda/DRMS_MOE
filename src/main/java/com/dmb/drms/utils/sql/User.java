package com.dmb.drms.utils.sql;

import javafx.beans.property.*;

public class User {
    private final IntegerProperty userID;
    private final StringProperty name;
    private final StringProperty username;
    private final StringProperty nic;
    private final StringProperty email;
    private final StringProperty phone;
    private final StringProperty userRole;

    // Constructor
    public User(int userID, String name, String username, String nic, String email, String phone, String userRole) {
        this.userID = new SimpleIntegerProperty(userID);
        this.name = new SimpleStringProperty(name);
        this.username = new SimpleStringProperty(username);
        this.nic = new SimpleStringProperty(nic);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.userRole = new SimpleStringProperty(userRole);
    }

    // Getters and setters for userID
    public int getUserID() {
        return userID.get();
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    // Getters and setters for name
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // Getters and setters for username
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    // Getters and setters for nic
    public String getNic() {
        return nic.get();
    }

    public void setNic(String nic) {
        this.nic.set(nic);
    }

    public StringProperty nicProperty() {
        return nic;
    }

    // Getters and setters for email
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Getters and setters for phone
    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    // Getters and setters for userRole
    public String getUserRole() {
        return userRole.get();
    }

    public void setUserRole(String userRole) {
        this.userRole.set(userRole);
    }

    public StringProperty userRoleProperty() {
        return userRole;
    }
}
