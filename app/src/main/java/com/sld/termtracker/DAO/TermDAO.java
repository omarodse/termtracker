package com.sld.termtracker.DAO;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.Term;

import java.util.List;

public interface TermDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("SELECT * FROM TERMS ORDER BY termId ASC")
    List<Term> getAllTerms();
}
