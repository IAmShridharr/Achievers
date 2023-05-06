package com.infiniterealm.achievers.admins.models;

public class StudentListItemModel {
    private String name, phone, profileImageUrl, id, school;

    public StudentListItemModel() {

    }

    public StudentListItemModel(String name, String profileImageUrl, String id) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.id = id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
