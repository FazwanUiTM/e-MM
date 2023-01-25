package com.dev.mentormenteesystem.mentor.model;

public class MentorPendingModel {

    String uID, fullName, studentID, group, semester,code1, code2, code3, code4, grade1, grade2, grade3, grade4;

    public MentorPendingModel() {
    }

    public MentorPendingModel(String uID, String fullName, String studentID, String group, String semester, String code1, String code2, String code3, String code4, String grade1, String grade2, String grade3, String grade4) {
        this.uID = uID;
        this.fullName = fullName;
        this.studentID = studentID;
        this.group = group;
        this.semester = semester;
        this.code1 = code1;
        this.code2 = code2;
        this.code3 = code3;
        this.code4 = code4;
        this.grade1 = grade1;
        this.grade2 = grade2;
        this.grade3 = grade3;
        this.grade4 = grade4;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCode1() {
        return code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getCode3() {
        return code3;
    }

    public void setCode3(String code3) {
        this.code3 = code3;
    }

    public String getCode4() {
        return code4;
    }

    public void setCode4(String code4) {
        this.code4 = code4;
    }

    public String getGrade1() {
        return grade1;
    }

    public void setGrade1(String grade1) {
        this.grade1 = grade1;
    }

    public String getGrade2() {
        return grade2;
    }

    public void setGrade2(String grade2) {
        this.grade2 = grade2;
    }

    public String getGrade3() {
        return grade3;
    }

    public void setGrade3(String grade3) {
        this.grade3 = grade3;
    }

    public String getGrade4() {
        return grade4;
    }

    public void setGrade4(String grade4) {
        this.grade4 = grade4;
    }
}
