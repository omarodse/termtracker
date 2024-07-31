package com.sld.termtracker.Database;

import android.app.Application;

import com.sld.termtracker.DAO.CourseDAO;
import com.sld.termtracker.DAO.TermDAO;
import com.sld.termtracker.DAO.TestDAO;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.Term;
import com.sld.termtracker.Entities.Test;

import java.util.ArrayList;
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
        mTestDAO = db.testDAO();
    }
    // Access Terms in DB
    public void getAllTerms(OnTermsRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<Term> terms = mTermDAO.getAllTerms();
            listener.onTermsRetrieved(new ArrayList<>(terms));
        });
    }

    public interface OnTermsRetrievedListener {
        void onTermsRetrieved(ArrayList<Term> terms);
    }

    public void insert(Term term) {
        databaseExecutor.execute(() -> mTermDAO.insert(term));
    }

    public void update(Term term) {
        databaseExecutor.execute(() -> mTermDAO.update(term));
    }

    public void delete(Term term) {
        databaseExecutor.execute(() -> mTermDAO.delete(term));
    }

    public void getAssociatedCourses(int termId, OnCoursesRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<Course> courses = mCourseDAO.getAssociatedCourses(termId);
            listener.onCoursesRetrieved(new ArrayList<>(courses));
        });
    }

    public void getTermById(int termId, OnTermRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            Term term = mTermDAO.getTermById(termId);
            listener.onTermRetrieved(term);
        });
    }

    public interface OnTermRetrievedListener {
        void onTermRetrieved(Term term);
    }

    // Access Courses
    public void insert(Course course) {
        databaseExecutor.execute(() -> mCourseDAO.insert(course));
    }

    public void update(Course course) {
        databaseExecutor.execute(() -> mCourseDAO.update(course));
    }

    public void delete(Course course) {
        databaseExecutor.execute(() -> mCourseDAO.delete(course));
    }

    public void getCourseById(int courseId, OnCourseRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            Course course = mCourseDAO.getCourseById(courseId);
            listener.onCourseRetrieved(course);
        });
    }

    public interface OnCourseRetrievedListener {
        void onCourseRetrieved(Course course);
    }

    public void getAllCourses(OnCoursesRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<Course> courses = mCourseDAO.getAllCourses();
            listener.onCoursesRetrieved(new ArrayList<>(courses));
        });
    }

    public void getCoursesByTermId(int termId, OnCoursesRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<Course> courses = mCourseDAO.getCoursesByTermId(termId);
            listener.onCoursesRetrieved(new ArrayList<>(courses));
        });
    }

    public interface OnCoursesRetrievedListener {
        void onCoursesRetrieved(ArrayList<Course> courses);
    }

    // Access tests
    public void insert(Test test) {
        databaseExecutor.execute(() -> mTestDAO.insert(test));
    }

    public void update(Test test) {
        databaseExecutor.execute(() -> mTestDAO.update(test));
    }

    public void delete(Test test) {
        databaseExecutor.execute(() -> mTestDAO.delete(test));
    }

    public void getTestById(int testId, OnTestRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            Test test = mTestDAO.getTestById(testId);
            listener.onTestRetrieved(test);
        });
    }

    public void getAllTests(OnTestsRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<Test> tests = mTestDAO.getAllTests();
            listener.onTestsRetrieved(new ArrayList<>(tests));
        });
    }

    public void getAssociatedTests(int courseId, OnTestsRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<Test> tests = mTestDAO.getAssociatedTests(courseId);
            listener.onTestsRetrieved(new ArrayList<>(tests));
        });
    }

    public interface OnTestsRetrievedListener {
        void onTestsRetrieved(ArrayList<Test> tests);
    }

    public interface OnTestRetrievedListener {
        void onTestRetrieved(Test test);
    }

}
