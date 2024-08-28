package com.sld.termtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sld.termtracker.Entities.OfflineCourse;

import java.util.List;

@Dao
public interface OfflineCourseDAO {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(OfflineCourse offlineCourse);

    @Update
    void update(OfflineCourse offlineCourse);

    @Delete
    void delete(OfflineCourse offlineCourse);

    @Query("SELECT * FROM offline_courses")
    List<OfflineCourse> getAllOfflineCourses();

    @Query("SELECT * FROM offline_courses WHERE courseId = :courseId LIMIT 1")
    OfflineCourse getOfflineCourseById(int courseId);

    @Query("SELECT * FROM offline_courses WHERE termId = :termId")
    List<OfflineCourse> getOfflineCoursesByTermId(int termId);
}
