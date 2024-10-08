package com.sld.termtracker.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "offline_courses")
public class OfflineCourse extends Course {
    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private String location;
    private CourseType courseType;

    public OfflineCourse(String courseTitle, String startDate, String endDate, CourseStatus status, int termId,
                         String instructorName, String instructorPhone, String instructorEmail, String note, String location) {
        super(courseTitle, startDate, endDate, status, termId, instructorName, instructorPhone, instructorEmail, note);
        this.courseType = CourseType.OFFLINE_COURSE;
        this.location = location;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getLocation() {
        return location;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }
}
