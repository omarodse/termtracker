package com.sld.termtracker.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(entity = Course.class,
                parentColumns = "courseId",
                childColumns = "courseId",
                onDelete = ForeignKey.CASCADE))
public class CourseNote {
    @PrimaryKey(autoGenerate = true)
    private int noteId;
    private int courseId;
    private String content;

    public CourseNote(int noteId, int courseId, String content) {
        this.noteId = noteId;
        this.courseId = courseId;
        this.content = content;
    }

    public int getNoteId() {
        return noteId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getContent() {
        return content;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
