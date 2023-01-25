package com.dev.mentormenteesystem.mentor.model;

public class ApprovedMentorModel {
    String uID;

    public ApprovedMentorModel() {
    }

    public ApprovedMentorModel(String uID) {
        this.uID = uID;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
