package com.sld.termtracker.UI;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.termtracker.R;
import com.google.android.material.textfield.TextInputEditText;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.CourseStatus;
import com.sld.termtracker.Entities.Term;
import com.sld.termtracker.Entities.TestType;

import java.util.ArrayList;
import java.util.List;

public class AddCourseFragment extends Fragment {
    private static final String TAG = "AddCourse";
    private TextInputEditText courseTitleEditText;
    private TextInputEditText courseStartDateEditText;
    private TextInputEditText courseEndDateEditText;
    private TextInputEditText courseInstructorEditText;
    private TextInputEditText courseInstructorPhoneEditText;
    private TextInputEditText courseInstructorEmailEditText;
    private TextInputEditText courseNoteEditText;
    private Spinner courseStatus;
    private CourseStatus selectedStatus;
    private static final String ARG_TERM_ID = "term_id";
    private static final String ARG_TERM_TITLE = "term_title";
    private static final String ARG_COURSE_ID = "course_id";
    private Course course;
    private int courseId = -1; // Default to -1 to indicate no course ID
    private int termId;

    private boolean noCourses = true;
    private String termTitle;
    private Repository repository;
    public static AddCourseFragment newInstance(int termId, String termTitle, int courseId) {
        AddCourseFragment fragment = new AddCourseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TERM_ID, termId);
        args.putString(ARG_TERM_TITLE, termTitle);
        Log.d(TAG, "courseId: " + courseId);
        if (courseId != -1) {
            args.putInt(ARG_COURSE_ID, courseId);
        }
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_course, container, false);

        if(getActivity() instanceof TermsActivity) {
            ((TermsActivity) getActivity()).updateToolbarTitle("Add Course");
            ((TermsActivity) getActivity()).showBackButton(true);
        }

        if (getArguments() != null) {
            termId = getArguments().getInt(ARG_TERM_ID);
            termTitle = getArguments().getString(ARG_TERM_TITLE);
            courseId = getArguments().getInt(ARG_COURSE_ID, -1);
            Log.d(TAG, "GET ARGUMENTS: The termId is " + termId);
        } else {
            Log.e(TAG, "Arguments are null");
        }

        repository = new Repository(getActivity().getApplication());

        if (courseId != -1) {
            loadCourseDetails();
        }

        courseTitleEditText = view.findViewById(R.id.course_title);
        courseStartDateEditText = view.findViewById(R.id.course_start_date);
        courseEndDateEditText = view.findViewById(R.id.course_end_date);
        courseInstructorEditText = view.findViewById(R.id.course_details_instructor_name);
        courseInstructorPhoneEditText = view.findViewById(R.id.course_details_instructor_phone);
        courseInstructorEmailEditText = view.findViewById(R.id.course_details_instructor_email);
        courseNoteEditText = view.findViewById(R.id.course_note);
        courseStatus = view.findViewById(R.id.course_spinner);
        Button saveCourse = view.findViewById(R.id.course_save_button);
        Button cancelAddCourse = view.findViewById(R.id.course_cancel_button);

        // Enum values to a list of strings
        List<CourseStatus> statusList = new ArrayList<>();
        statusList.add(CourseStatus.SELECT_STATUS);
        for (CourseStatus status : CourseStatus.values()) {
            if (status != CourseStatus.SELECT_STATUS) {
                statusList.add(status);
            }
        }

        // ArrayAdapter using the status list
        ArrayAdapter<CourseStatus> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, statusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapter to the Spinner
        courseStatus.setAdapter(adapter);

        // Set an item selected listener to get the selected status
        courseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = (CourseStatus) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancelAddCourse.setOnClickListener(v -> {
            if(getActivity() instanceof TermsActivity) {
                ((TermsActivity) getActivity()).updateToolbarTitle(termTitle);
            }
            getParentFragmentManager().popBackStack();
        });

        // Create a course
        saveCourse.setOnClickListener(v -> {
            saveCourse();
            // Reset spinner
            courseStatus.setSelection(statusList.indexOf(CourseStatus.SELECT_STATUS));

        });

        return view;

    }


    private void loadCourseDetails() {
        repository.getCourseById(courseId, course -> {
            if (course != null) {
                this.course = course;
                // Ensuring UI updates are run on the main thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        courseTitleEditText.setText(course.getCourseTitle());
                        courseStartDateEditText.setText(course.getStartDate());
                        courseEndDateEditText.setText(course.getEndDate());
                        courseInstructorEditText.setText(course.getInstructorName());
                        courseInstructorPhoneEditText.setText(course.getInstructorPhone());
                        courseInstructorEmailEditText.setText(course.getInstructorEmail());
                        courseNoteEditText.setText(course.getNote());
                        setSpinnerSelection(course.getStatus());

                    });
                }
            }
        });
    }

    private void setSpinnerSelection(CourseStatus status) {
        ArrayAdapter<CourseStatus> adapter = (ArrayAdapter<CourseStatus>) courseStatus.getAdapter();
        int position = adapter.getPosition(status);
        courseStatus.setSelection(position);
    }

    private void saveCourse() {

        if(courseId == -1) {
            String title = courseTitleEditText.getText().toString().trim();
            String startDate = courseStartDateEditText.getText().toString().trim();
            String endDate = courseEndDateEditText.getText().toString().trim();
            String instructorName = courseInstructorEditText.getText().toString().trim();
            String instructorPhone = courseInstructorPhoneEditText.getText().toString().trim();
            String instructorEmail = courseInstructorEmailEditText.getText().toString().trim();
            String note = courseNoteEditText.getText().toString().trim();

            if (title.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || instructorName.isEmpty()
                    || instructorPhone.isEmpty() ||instructorEmail.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_LONG).show();
                return;
            }

            if (selectedStatus == CourseStatus.SELECT_STATUS) {
                Toast.makeText(getContext(), "Please select a valid status", Toast.LENGTH_LONG).show();
                return;
            }
            // Saving the course
            Course newCourse = new Course(title, startDate, endDate, selectedStatus, termId, instructorName, instructorPhone, instructorEmail, note);
            repository.insert(newCourse);
            Toast.makeText(getContext(), "Course saved", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
            clearForm();

            // Reload fragment if there were no courses in that term
            repository.getTermById(termId, term -> {
                boolean hasNoCourses = term.isNoCourses();
                if(getActivity() instanceof TermsActivity && hasNoCourses) {
                    // Change isNoCourses for the term
                    term.setNoCourses(false);
                    // Remove the empty fragment from the stack to avoid showing it again
                    getParentFragmentManager().popBackStack();
                    // Reload the courses for that term
                    reloadCourseFragment();
                }
            });
        } else {

            // Updating an existing test
            if (course != null) {
                course.setCourseTitle(courseTitleEditText.getText().toString());
                course.setStartDate(courseStartDateEditText.getText().toString());
                course.setEndDate(courseEndDateEditText.getText().toString());
                course.setInstructorName(courseInstructorEditText.getText().toString());
                course.setInstructorPhone(courseInstructorPhoneEditText.getText().toString());
                course.setInstructorEmail(courseInstructorEmailEditText.getText().toString());
                course.setNote(courseNoteEditText.getText().toString());
                course.setStatus((CourseStatus) courseStatus.getSelectedItem());
                repository.update(course);
                Toast.makeText(getContext(), "Course updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
        // Go back or close the fragment
        getParentFragmentManager().popBackStack();
    }

    private void clearForm() {
        courseTitleEditText.setText("");
        courseStartDateEditText.setText("");
        courseEndDateEditText.setText("");
        courseInstructorEditText.setText("");
        courseInstructorPhoneEditText.setText("");
        courseInstructorEmailEditText.setText("");
    }

    private void reloadCourseFragment() {
        if (getActivity() instanceof TermsActivity) {
            ((TermsActivity) getActivity()).showCoursesFragment(termId, termTitle);
        }
    }

    public int getCourseId() {
        return courseId;
    }
}
