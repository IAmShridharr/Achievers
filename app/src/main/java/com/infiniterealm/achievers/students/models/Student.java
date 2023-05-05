package com.infiniterealm.achievers.students.models;

import android.net.Uri;

public class Student {
    private String profileImageUrl, name, roll_no;

    Student() {

    }

    public Student(String profileImageUrl, String name, String roll_no) {
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.roll_no = roll_no;
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

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }
}
