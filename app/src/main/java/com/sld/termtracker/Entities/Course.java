package com.sld.termtracker.Entities;

import androidx.room.Entity;
@Entity(tableName = "parts")
public class Course {

    private String courseTitle;
    private String startDate;
    private String endDate;
    private CourseStatus status;

    public Course(String courseTitle, String startDate, String endDate, CourseStatus status) {
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }
}
