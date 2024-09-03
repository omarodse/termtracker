package com.sld.termtracker.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sld.termtracker.DAO.OfflineCourseDAO;
import com.sld.termtracker.DAO.OnlineCourseDAO;
import com.sld.termtracker.DAO.TermDAO;
import com.sld.termtracker.DAO.TestDAO;
import com.sld.termtracker.DAO.UserDAO;
import com.sld.termtracker.Entities.OfflineCourse;
import com.sld.termtracker.Entities.OnlineCourse;
import com.sld.termtracker.Entities.Term;
import com.sld.termtracker.Entities.Test;
import com.sld.termtracker.Entities.User;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

@Database(entities = {OfflineCourse.class, OnlineCourse.class, Term.class, Test.class, User.class}, version = 27, exportSchema = false)
public abstract class TermtrackerDatabaseBuilder extends RoomDatabase {
    public abstract OfflineCourseDAO offlineCourseDAO();
    public abstract OnlineCourseDAO onlineCourseDAO();
    public abstract TermDAO termDAO();
    public abstract TestDAO testDAO();
    public abstract UserDAO userDAO();
    private static volatile  TermtrackerDatabaseBuilder INSTANCE;

    public static TermtrackerDatabaseBuilder getDatabase(final Context context, boolean useEncryption) {
        if (INSTANCE == null) {
            synchronized (TermtrackerDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    RoomDatabase.Builder<TermtrackerDatabaseBuilder> builder = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TermtrackerDatabaseBuilder.class,
                            "MyTermTracker.db"
                    );

                    if (useEncryption) {
                        // Use SQLCipher for encryption
                        byte[] passphrase = SQLiteDatabase.getBytes("wgucapstoneproject2024".toCharArray());
                        SupportFactory factory = new SupportFactory(passphrase);
                        builder.openHelperFactory(factory);
                    }

                    INSTANCE = builder
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
