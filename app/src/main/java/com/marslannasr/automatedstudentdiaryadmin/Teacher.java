package com.marslannasr.automatedstudentdiaryadmin;

/**
 * Created by ArslanNasr on 5/25/2018.
 */

public class Teacher {

    public String teacher_name;
    public String teacher_qualification;
    public String teacher_university;
    public String timestamp;
    public String userid;
    public String gender;
    public String phone_number;
    public String teacher_nodeID;


    public Teacher() {
    }

    public Teacher(String teacher_name, String teacher_qualification, String teacher_university, String timestamp, String userid, String gender, String phone_number, String teacher_nodeID) {
        this.teacher_name = teacher_name;
        this.teacher_qualification = teacher_qualification;
        this.teacher_university = teacher_university;
        this.timestamp = timestamp;
        this.userid = userid;
        this.gender = gender;
        this.phone_number = phone_number;
        this.teacher_nodeID = teacher_nodeID;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_qualification() {
        return teacher_qualification;
    }

    public void setTeacher_qualification(String teacher_qualification) {
        this.teacher_qualification = teacher_qualification;
    }

    public String getTeacher_university() {
        return teacher_university;
    }

    public void setTeacher_university(String teacher_university) {
        this.teacher_university = teacher_university;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }


    public String getTeacher_nodeID() {
        return teacher_nodeID;
    }

    public void setTeacher_nodeID(String teacher_nodeID) {
        this.teacher_nodeID = teacher_nodeID;
    }
}
