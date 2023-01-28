package com.dev.mentormenteesystem.mentor.model;

public class SubjectsModel {
    String fullName, subjectName, subjectCode, dayTime, limitStudent, image, uID;

    //public SubjectsModel() {
    //}

    public SubjectsModel(String fullName, String subjectName, String subjectCode,String dayTime, String limitStudent, String image, String uID) {
        this.fullName = fullName;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.dayTime = dayTime;
        this.limitStudent = limitStudent;
        this.image = image;
        this.uID = uID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getLimitStudent() {
        return limitStudent;
    }

    public void setLimitStudent(String limitStudent) {
        this.limitStudent = limitStudent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
