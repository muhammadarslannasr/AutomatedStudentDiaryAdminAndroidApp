package com.marslannasr.automatedstudentdiaryadmin;

/**
 * Created by ArslanNasr on 5/26/2018.
 */

public class Subject {

    public String teacher_name;
    public String subject_name;
    public String subject_nodeID;
    public String timestamp;


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSubject_nodeID() {
        return subject_nodeID;
    }

    public void setSubject_nodeID(String subject_nodeID) {
        this.subject_nodeID = subject_nodeID;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }
}
