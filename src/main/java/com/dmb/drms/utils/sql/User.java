package com.dmb.drms.utils.sql;

public class User {
    private int userID;
    private String name;
    private String username;
    private String nic;
    private String email;
    private String phone;
    private String userRole;

    public User(int userID, String name, String username, String nic, String email, String phone, String userRole) {
        this.userID = userID;
        this.name = name;
        this.username = username;
        this.nic = nic;
        this.email = email;
        this.phone = phone;
        this.userRole = userRole;
    }

    // Getters and setters for each field
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
}
