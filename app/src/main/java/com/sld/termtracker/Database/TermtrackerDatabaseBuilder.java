package com.sld.termtracker.Database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.Term;

@Database(entities = {Course.class, Term.class}, version = 1, exportSchema = false)
public class TermtrackerDatabaseBuilder extends RoomDatabase {

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
