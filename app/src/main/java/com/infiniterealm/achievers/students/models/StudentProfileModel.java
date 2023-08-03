package com.infiniterealm.achievers.students.models;

public class StudentProfileModel {
    // Define your user properties (profileImageUrl, name, id, DOB, school, followers, tests, followings)
    private String id, name, profileImageURL, DOB, school, phone, parentPhone, email;
    private int followers, tests, followings;

    public StudentProfileModel() {
    }

    public StudentProfileModel(String id, String name, String profileImageURL, String DOB, String school, String phone, String parentPhone, String email, int followers, int tests, int followings) {
        this.id = id;
        this.name = name;
        this.profileImageURL = profileImageURL;
        this.DOB = DOB;
        this.school = school;
        this.phone = phone;
        this.parentPhone = parentPhone;
        this.email = email;
        this.followers = followers;
        this.tests = tests;
        this.followings = followings;
    }

    // ... getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getTests() {
        return tests;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }

    public int getFollowings() {
        return followings;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
