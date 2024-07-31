package com.sld.termtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.Test;

import java.util.List;

@Dao
public interface TestDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Test test);

    @Update
    void update(Test test);

    @Delete
    void delete(Test test);

    @Query("SELECT * FROM TESTS ORDER BY testId ASC")
    List<Test> getAllTests();

    @Query("SELECT * FROM TESTS WHERE courseId=:course ORDER BY testId ASC")
    List<Test> getAssociatedTests(int course);

    @Query("SELECT * FROM tests WHERE testId = :testId LIMIT 1")
    Test getTestById(int testId);
}
