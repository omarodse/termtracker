package com.sld.termtracker.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "online_courses")
public class OnlineCourse extends Course {

    @PrimaryKey(autoGenerate = true)
    private int courseId;

    private String platform;

    private CourseType courseType;

    public OnlineCourse(String courseTitle, String startDate, String endDate, CourseStatus status, int termId,
                        String instructorName, String instructorPhone, String instructorEmail, String note, String platform) {
        super(courseTitle, startDate, endDate, status, termId, instructorName, instructorPhone, instructorEmail, note);
        this.courseType = CourseType.ONLINE_COURSE;
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }
}
