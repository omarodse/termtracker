package com.sld.termtracker.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tests")
public class Test {
    @PrimaryKey(autoGenerate = true)
    private int testId;
    private String title;
    private String startDate;
    private String endDate;
    private TestType type;
    private int courseId;
    private String courseType;

    public Test(String title, String startDate, String endDate, TestType type, int courseId, String courseType) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.courseId = courseId;
        this.courseType = courseType;
    }

    public int getTestId() {
        return testId;
    }

    public String getStartDate() {
        return startDate;
    }
    public String getTitle() {
        return title;
    }

    public String getEndDate() {
        return endDate;
    }

    public TestType getType() {
        return type;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }
}
