package com.infiniterealm.achievers.admins.helper;

public class StudentSignUpHelper {
    private String name, ID, email, password, uid, role;

    public StudentSignUpHelper() {

    }

    public StudentSignUpHelper(String name, String ID, String email, String password, String uid, String role) {
        this.name = name;
        this.ID = ID;
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}