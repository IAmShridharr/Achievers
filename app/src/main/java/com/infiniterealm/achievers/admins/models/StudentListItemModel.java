package com.infiniterealm.achievers.admins.models;

public class StudentListItemModel {
    private String name, phone, profileImageURL, id;

    public StudentListItemModel() {

    }

    public StudentListItemModel(String name, String phone, String profileImageURL, String id) {
        this.name = name;
        this.phone = phone;
        this.profileImageURL = profileImageURL;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageUrl) { this.profileImageURL = profileImageURL; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
