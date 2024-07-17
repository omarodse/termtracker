package com.sld.termtracker.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses",
        foreignKeys = @ForeignKey(entity = Term.class,
                parentColumns = "termId",
                childColumns = "termId",
                onDelete = ForeignKey.CASCADE))
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private String courseTitle;
    private String startDate;
    private String endDate;
    private CourseStatus status;
    private int termId;
    private int instructorId;

    public Course(String courseTitle, String startDate, String endDate, CourseStatus status, int termId, int instructorId) {
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.termId = termId;
        this.instructorId = instructorId;
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

    public int getInstructorId() {
        return instructorId;
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
    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }
}
