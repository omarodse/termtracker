package com.sld.termtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sld.termtracker.Entities.Course;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM COURSES ORDER BY courseId ASC")
    List<Course> getAllCourses();

    @Query("SELECT * FROM COURSES WHERE termId=:term ORDER BY courseId ASC")
    List<Course> getAssociatedCourses(int term);

    @Query("SELECT * FROM courses WHERE termId = :termId")
    List<Course> getCoursesByTermId(int termId);

    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    Course getCourseById(int courseId);
}
