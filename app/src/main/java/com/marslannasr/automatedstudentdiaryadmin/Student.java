package com.marslannasr.automatedstudentdiaryadmin;

/**
 * Created by ArslanNasr on 5/22/2018.
 */

public class Student {

    public String stud_name;
    public String stud_fname;
    public String stud_image;
    public String timestamp;
    public String userid;
    public String month;
    public String day;
    public String year;
    public String gender;
    public String phone_number;
    public String home_address;
    public String program;
    public String stud_rollnumb;
    public String stud_attendmonth;
    public String stud_attendpercent;
    public String stud_result;
    public String stud_feesRecord;
    public String stud_id;

    public String getStud_id() {
        return stud_id;
    }

    public void setStud_id(String stud_id) {
        this.stud_id = stud_id;
    }

    public String getStud_rollnumb() {
        return stud_rollnumb;
    }

    public void setStud_rollnumb(String stud_rollnumb) {
        this.stud_rollnumb = stud_rollnumb;
    }

    public String getStud_attendmonth() {
        return stud_attendmonth;
    }

    public void setStud_attendmonth(String stud_attendmonth) {
        this.stud_attendmonth = stud_attendmonth;
    }

    public String getStud_attendpercent() {
        return stud_attendpercent;
    }

    public void setStud_attendpercent(String stud_attendpercent) {
        this.stud_attendpercent = stud_attendpercent;
    }

    public String getStud_result() {
        return stud_result;
    }

    public void setStud_result(String stud_result) {
        this.stud_result = stud_result;
    }

    public String getStud_feesRecord() {
        return stud_feesRecord;
    }

    public void setStud_feesRecord(String stud_feesRecord) {
        this.stud_feesRecord = stud_feesRecord;
    }

    public Student() {
    }


    public Student(String stud_name, String stud_fname, String stud_image, String timestamp, String userid, String month, String day, String year, String gender, String phone_number, String home_address, String program) {
        this.stud_name = stud_name;
        this.stud_fname = stud_fname;
        this.stud_image = stud_image;
        this.timestamp = timestamp;
        this.userid = userid;
        this.month = month;
        this.day = day;
        this.year = year;
        this.gender = gender;
        this.phone_number = phone_number;
        this.home_address = home_address;
        this.program = program;
    }


    public String getStud_name() {
        return stud_name;
    }

    public void setStud_name(String stud_name) {
        this.stud_name = stud_name;
    }

    public String getStud_fname() {
        return stud_fname;
    }

    public void setStud_fname(String stud_fname) {
        this.stud_fname = stud_fname;
    }

    public String getStud_image() {
        return stud_image;
    }

    public void setStud_image(String stud_image) {
        this.stud_image = stud_image;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }
}

