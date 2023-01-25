package com.dev.mentormenteesystem.mentor.model;

public class MenteeRequestModel {
    String fullName, group, semester, subject, userUID, userPushID, mentorPushID;

    public MenteeRequestModel() {
    }

    public MenteeRequestModel(String fullName, String group, String semester, String subject, String userUID, String userPushID, String mentorPushID) {
        this.fullName = fullName;
        this.group = group;
        this.semester = semester;
        this.subject = subject;
        this.userUID = userUID;
        this.userPushID = userPushID;
        this.mentorPushID = mentorPushID;
    }

    public String getMentorPushID() {
        return mentorPushID;
    }

    public void setMentorPushID(String mentorPushID) {
        this.mentorPushID = mentorPushID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserPushID() {
        return userPushID;
    }

    public void setUserPushID(String userPushID) {
        this.userPushID = userPushID;
    }
}
