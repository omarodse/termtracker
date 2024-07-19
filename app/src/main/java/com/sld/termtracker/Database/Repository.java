package com.sld.termtracker.Database;

import android.app.Application;

import com.sld.termtracker.DAO.CourseDAO;
import com.sld.termtracker.DAO.CourseNoteDAO;
import com.sld.termtracker.DAO.InstructorDAO;
import com.sld.termtracker.DAO.TermDAO;
import com.sld.termtracker.DAO.TestDAO;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.CourseNote;
import com.sld.termtracker.Entities.Instructor;
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

    private CourseNoteDAO mCourseNoteDAO;

    private InstructorDAO mInstructorDAO;

    private List<Term> mAllTerms;
    private List<Course> mAllCourses;
    private List<Test> mAllTests;

    private List<CourseNote> mAllCourseNotes;

    private List<Instructor> mAllInstructors;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository (Application application) {
        TermtrackerDatabaseBuilder db = TermtrackerDatabaseBuilder.getDatabase(application);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
        mTestDAO = db.testDAO();
        mCourseNoteDAO = db.courseNoteDAO();
        mInstructorDAO = db.instructorDAO();
    }
    // Access Terms in DB
    public void getAllTerms(OnTermsRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<Term> terms = mTermDAO.getAllTerms();
            listener.onTermsRetrieved(new ArrayList<>(terms));
        });
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

    public interface OnTermsRetrievedListener {
        void onTermsRetrieved(ArrayList<Term> terms);
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

    // Access Course Notes
    public void getAllCourseNotes(OnCourseNotesRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<CourseNote> notes = mCourseNoteDAO.getAllNotes();
            listener.onCourseNotesRetrieved(new ArrayList<>(notes));
        });
    }

    public void getAssociatedNotes(int courseId, OnCourseNotesRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<CourseNote> notes = mCourseNoteDAO.getAssociatedNotes(courseId);
            listener.onCourseNotesRetrieved(new ArrayList<>(notes));
        });
    }

    public void insert(CourseNote note) {
        databaseExecutor.execute(() -> mCourseNoteDAO.insert(note));
    }

    public void update(CourseNote note) {
        databaseExecutor.execute(() -> mCourseNoteDAO.update(note));
    }

    public void delete(CourseNote note) {
        databaseExecutor.execute(() -> mCourseNoteDAO.delete(note));
    }

    public interface OnCourseNotesRetrievedListener {
        void onCourseNotesRetrieved(ArrayList<CourseNote> notes);
    }

    // Access Instructors
    public void getAllInstructors(OnInstructorsRetrievedListener listener) {
        databaseExecutor.execute(() -> {
            List<Instructor> instructors = mInstructorDAO.getAllInstructors();
            listener.onInstructorsRetrieved(new ArrayList<>(instructors));
        });
    }

    public void insert(Instructor instructor) {
        databaseExecutor.execute(() -> mInstructorDAO.insert(instructor));
    }

    public void update(Instructor instructor) {
        databaseExecutor.execute(() -> mInstructorDAO.update(instructor));
    }

    public void delete(Instructor instructor) {
        databaseExecutor.execute(() -> mInstructorDAO.delete(instructor));
    }

    public interface OnInstructorsRetrievedListener {
        void onInstructorsRetrieved(ArrayList<Instructor> instructors);
    }

}
