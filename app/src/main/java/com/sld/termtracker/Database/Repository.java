package com.sld.termtracker.Database;

import android.app.Application;

import com.sld.termtracker.DAO.CourseDAO;
import com.sld.termtracker.DAO.TermDAO;
import com.sld.termtracker.DAO.TestDAO;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.Term;
import com.sld.termtracker.Entities.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private TermDAO mTermDAO;
    private CourseDAO mCourseDAO;
    private TestDAO mTestDAO;

    private List<Term> mAllTerms;
    private List<Course> mAllCourses;
    private List<Test> mAllTests;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository (Application application) {
        TermtrackerDatabaseBuilder db = TermtrackerDatabaseBuilder.getDatabase(application);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
    }

    public List<Term> getmAllTerms() {
        databaseExecutor.execute(()->{
            mAllTerms = mTermDAO.getAllTerms();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllTerms;
    }

    public void insert(Term term) {
        databaseExecutor.execute(()->{
            mTermDAO.insert(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Term term) {
        databaseExecutor.execute(()->{
            mTermDAO.update(term);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Term term) {
        databaseExecutor.execute(()->{
            mTermDAO.delete(term);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Course> getAssociatedCourses(int termId) {
        databaseExecutor.execute(()->{
            mAllCourses = mCourseDAO.getAssociatedCourses(termId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllCourses;
    }

    public void insert(Course course) {
        databaseExecutor.execute(()->{
            mCourseDAO.insert(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Course course) {
        databaseExecutor.execute(()->{
            mCourseDAO.update(course);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Course course) {
        databaseExecutor.execute(()->{
            mCourseDAO.delete(course);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Course> getmAllCourses() {
        databaseExecutor.execute(()->{
            mAllCourses = mCourseDAO.getAllCourses();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllCourses;
    }

    public List<Test> getAssociatedTests(int courseId) {
        databaseExecutor.execute(()->{
            mAllTests = mTestDAO.getAssociatedTests(courseId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllTests;
    }

    public void insert(Test test) {
        databaseExecutor.execute(()->{
            mTestDAO.insert(test);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Test test) {
        databaseExecutor.execute(()->{
            mTestDAO.update(test);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Test test) {
        databaseExecutor.execute(()->{
            mTestDAO.delete(test);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Test> getmAllTests() {
        databaseExecutor.execute(()->{
            mAllTests = mTestDAO.getAllTests();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllTests;
    }
}
