package com.sld.termtracker.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tests",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "courseId",
                childColumns = "courseId",
                onDelete = ForeignKey.CASCADE))
public class Test {
    @PrimaryKey(autoGenerate = true)
    private int testId;
    private String title;
    private String endDate;
    private TestType type;
    private int courseId;

    public Test(String title, String endDate, TestType type, int courseId) {
        this.title = title;
        this.endDate = endDate;
        this.type = type;
        this.courseId = courseId;
    }

    public int getTestId() {
        return testId;
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

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
