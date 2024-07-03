package com.sld.termtracker.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private String courseTitle;
    private String startDate;
    private String endDate;
    private CourseStatus status;
    private int termId;

    public Course(int courseId, String courseTitle, String startDate, String endDate, CourseStatus status, int termId) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.termId = termId;
    }

    public int getCourseId() {
        return courseId;
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

    public int getTermId() {
        return termId;
    }
    public void setCourseId(int courseId) {
        this.courseId = courseId;
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

    public void setTermId(int termId) {
        this.termId = termId;
    }
}
