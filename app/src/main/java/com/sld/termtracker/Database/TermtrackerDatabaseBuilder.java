package com.sld.termtracker.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sld.termtracker.DAO.CourseDAO;
import com.sld.termtracker.DAO.CourseNoteDAO;
import com.sld.termtracker.DAO.TermDAO;
import com.sld.termtracker.DAO.TestDAO;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.CourseNote;
import com.sld.termtracker.Entities.Term;
import com.sld.termtracker.Entities.Test;

@Database(entities = {Course.class, Term.class, Test.class, CourseNote.class}, version = 6, exportSchema = false)
public abstract class TermtrackerDatabaseBuilder extends RoomDatabase {
    public abstract CourseDAO courseDAO();
    public abstract TermDAO termDAO();
    public abstract TestDAO testDAO();
    public abstract CourseNoteDAO courseNoteDAO();
    private static volatile  TermtrackerDatabaseBuilder INSTANCE;

    public static TermtrackerDatabaseBuilder getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (TermtrackerDatabaseBuilder.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TermtrackerDatabaseBuilder.class, "MyTermTracker.db")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

}
