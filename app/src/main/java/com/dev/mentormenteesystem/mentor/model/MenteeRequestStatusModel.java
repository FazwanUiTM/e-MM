package com.dev.mentormenteesystem.mentor.model;

public class MenteeRequestStatusModel {
    String subjectCode, status;

    public MenteeRequestStatusModel() {
    }

    public MenteeRequestStatusModel(String subjectCode, String status) {
        this.subjectCode = subjectCode;
        this.status = status;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
