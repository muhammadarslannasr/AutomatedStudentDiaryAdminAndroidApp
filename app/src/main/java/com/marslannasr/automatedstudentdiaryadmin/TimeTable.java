package com.marslannasr.automatedstudentdiaryadmin;

/**
 * Created by ArslanNasr on 5/26/2018.
 */

public class TimeTable {

    public String time_date;
    public String teacher_name;
    public String subject;
    public String timestamp;
    public String room_number;
    public String timetable_nodeID;


    public TimeTable() {
    }

    public TimeTable(String time_date, String teacher_name, String subject, String timestamp, String room_number, String timetable_nodeID) {
        this.time_date = time_date;
        this.teacher_name = teacher_name;
        this.subject = subject;
        this.timestamp = timestamp;
        this.room_number = room_number;
        this.timetable_nodeID = timetable_nodeID;
    }

    public String getTime_date() {
        return time_date;
    }

    public void setTime_date(String time_date) {
        this.time_date = time_date;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getTimetable_nodeID() {
        return timetable_nodeID;
    }

    public void setTimetable_nodeID(String timetable_nodeID) {
        this.timetable_nodeID = timetable_nodeID;
    }
}
