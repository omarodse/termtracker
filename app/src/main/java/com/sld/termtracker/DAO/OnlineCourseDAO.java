package com.sld.termtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sld.termtracker.Entities.OfflineCourse;
import com.sld.termtracker.Entities.OnlineCourse;

import java.util.List;

@Dao
public interface OnlineCourseDAO {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(OnlineCourse onlineCourse);

    @Update
    void update(OnlineCourse onlineCourse);

    @Delete
    void delete(OnlineCourse onlineCourse);

    @Query("SELECT * FROM online_courses")
    List<OnlineCourse> getAllOnlineCourses();

    @Query("SELECT * FROM online_courses WHERE courseId = :courseId LIMIT 1")
    OnlineCourse getOnlineCourseById(int courseId);

    @Query("SELECT * FROM online_courses WHERE termId = :termId")
    List<OnlineCourse> getOnlineCoursesByTermId(int termId);
}
