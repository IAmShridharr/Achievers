package com.infiniterealm.achievers;

public class Student {
    private String name, standard, rollNumber, phone, parentPhone, school;

    Student(String name, String standard, String rollNumber, String phone, String parentPhone, String school) {
        this.name = name;
        this.standard = standard;
        this.rollNumber = rollNumber;
        this.phone = phone;
        this.parentPhone = parentPhone;
        this.school = school;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
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

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}