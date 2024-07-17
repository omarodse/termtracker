package com.sld.termtracker.DAO;

import android.provider.ContactsContract;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.CourseNote;

import java.util.List;

@Dao
public interface CourseNoteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CourseNote note);

    @Update
    void update(CourseNote note);

    @Delete
    void delete(CourseNote note);

    @Query("SELECT * FROM NOTES ORDER BY noteId ASC")
    List<CourseNote> getAllNotes();

    @Query("SELECT * FROM NOTES WHERE courseId=:course ORDER BY courseId ASC")
    List<CourseNote> getAssociatedNotes(int course);
}
