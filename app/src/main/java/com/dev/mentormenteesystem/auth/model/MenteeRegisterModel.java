package com.dev.mentormenteesystem.auth.model;

public class MenteeRegisterModel {

    String fullName, userName, email, password, semester, group, phoneNumber, category, image;

    public MenteeRegisterModel() {
    }

    public MenteeRegisterModel(String fullName, String userName, String email, String password, String semester, String group, String phoneNumber, String category, String image) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.semester = semester;
        this.group = group;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
