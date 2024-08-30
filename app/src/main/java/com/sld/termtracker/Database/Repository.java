package com.sld.termtracker.Database;

import android.app.Application;
import android.util.Log;

import com.sld.termtracker.DAO.CourseDAO;
import com.sld.termtracker.DAO.OfflineCourseDAO;
import com.sld.termtracker.DAO.OnlineCourseDAO;
import com.sld.termtracker.DAO.TermDAO;
import com.sld.termtracker.DAO.TestDAO;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.CourseType;
import com.sld.termtracker.Entities.OfflineCourse;
import com.sld.termtracker.Entities.OnlineCourse;
import com.sld.termtracker.Entities.Term;
import com.sld.termtracker.Entities.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private static final String TAG = "repository";
    private TermDAO mTermDAO;
    private CourseDAO mCourseDAO;

    private OfflineCourseDAO mOfflineCourseDAO;

    private OnlineCourseDAO mOnlineCourseDAO;
    private TestDAO mTestDAO;

    private List<Term> mAllTerms;
    private List<Course> mAllCourses;
    private List<Test> mAllTests;


    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository (Application application) {
        TermtrackerDatabaseBuilder db = TermtrackerDatabaseBuilder.getDatabase(application);
        mTermDAO = db.termDAO();
        mOfflineCourseDAO = db.offlineCourseDAO();
        mOnlineCourseDAO = db.onlineCourseDAO();
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

    // Access Online courses
    public void insert(Course course) {
        if (course instanceof OfflineCourse) {
            databaseExecutor.execute(() -> mOfflineCourseDAO.insert((OfflineCourse) course));
        } else if(course instanceof OnlineCourse) {
            databaseExecutor.execute(() -> mOnlineCourseDAO.insert((OnlineCourse) course));
        }
    }

    public void update(Course course) {
        if (course instanceof OfflineCourse) {
            databaseExecutor.execute(() -> mOfflineCourseDAO.update((OfflineCourse) course));
        } else if(course instanceof OnlineCourse) {
            databaseExecutor.execute(() -> mOnlineCourseDAO.update((OnlineCourse) course));
        }
    }

    public void delete(Course course) {
        if (course instanceof OfflineCourse) {
            databaseExecutor.execute(() -> mOfflineCourseDAO.delete((OfflineCourse) course));
        } else if(course instanceof OnlineCourse) {
            databaseExecutor.execute(() -> mOnlineCourseDAO.delete((OnlineCourse) course));
        }
    }

    public void getCourseById(int courseId, String courseType, OnCourseRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            if(courseType.equals("Offline Course")) {
                OfflineCourse course = mOfflineCourseDAO.getOfflineCourseById(courseId);
                listener.onCourseRetrieved(course);
            } else if (courseType.equals("Online Course")) {
                OnlineCourse course = mOnlineCourseDAO.getOnlineCourseById(courseId);
                listener.onCourseRetrieved(course);
            }
        });
    }

    public void getOnlineCourseById(int courseId, OnCourseRetrievedListener listener) {

        databaseExecutor.execute(() -> {
            OfflineCourse course = mOfflineCourseDAO.getOfflineCourseById(courseId);
            listener.onCourseRetrieved(course);
        });
    }

    public void getOfflineCourseById(int courseId, OnCourseRetrievedListener listener) {

        databaseExecutor.execute(() -> {
            OnlineCourse course = mOnlineCourseDAO.getOnlineCourseById(courseId);
            listener.onCourseRetrieved(course);
        });
    }

    public interface OnCourseRetrievedListener {
        void onCourseRetrieved(Course course);
    }

    public void getAllCourses(OnCoursesRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<OfflineCourse> offlineCourses = mOfflineCourseDAO.getAllOfflineCourses();
            List<OnlineCourse> onlineCourses = mOnlineCourseDAO.getAllOnlineCourses();
            List<Course> allCourses = new ArrayList<>();
            allCourses.addAll(offlineCourses);
            allCourses.addAll(onlineCourses);
            listener.onCoursesRetrieved(new ArrayList<>(allCourses));
        });
    }

    public void getCoursesByTermId(int termId, OnCoursesRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<OfflineCourse> offlineCourses = mOfflineCourseDAO.getOfflineCoursesByTermId(termId);
            List<OnlineCourse> onlineCourses = mOnlineCourseDAO.getOnlineCoursesByTermId(termId);
            List<Course> courses = new ArrayList<>();
            courses.addAll(offlineCourses);
            courses.addAll(onlineCourses);
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

    public void getTestsByCourseIdAndType(int courseId, String courseType, OnTestsRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<Test> tests = mTestDAO.getTestsByCourseIdAndType(courseType, courseId);
            listener.onTestsRetrieved(new ArrayList<>(tests));
        });
    }

    public void getTestsByCourseType(String courseType, OnTestsRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<Test> tests = mTestDAO.getTestsByCourseType(courseType);
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
