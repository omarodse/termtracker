package com.sld.termtracker.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.termtracker.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.OfflineCourse;
import com.sld.termtracker.Entities.OnlineCourse;
import com.sld.termtracker.Entities.Test;
import com.sld.termtracker.Entities.TestType;

import java.util.concurrent.atomic.AtomicReference;

public class AddTestFragment extends Fragment {

    private static final String ARG_TEST_ID = "testId";
    private int testId = -1; // Default to -1 to indicate no test ID
    private Test test;
    private static final String TAG = "addTest";
    private TextInputLayout titleLayout, startDateLayout, endDateLayout;
    private TextInputEditText titleEditText, startDateEditText, endDateEditText;
    private RadioGroup testTypeGroup;
    private TestType testType;
    private Repository repository;
    private static final String ARG_COURSE_ID = "courseId";
    private static final String ARG_COURSE_TITLE = "courseTitle";
    private static final String ARG_COURSE_TYPE = "courseType";
    private int courseId;
    private String courseTitle;
    private String courseType;


    public static AddTestFragment newInstance(int courseId, String courseTitle, int testId, String courseType) {
        AddTestFragment fragment = new AddTestFragment();
        Bundle args = new Bundle();
        Log.d(TAG, "testId: " + testId);
        args.putInt(ARG_COURSE_ID, courseId);
        args.putString(ARG_COURSE_TITLE, courseTitle);
        args.putString(ARG_COURSE_TYPE, courseType);
        if (testId != -1) {
            args.putInt(ARG_TEST_ID, testId);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_test, container, false);

        titleLayout = view.findViewById(R.id.test_title_layout);
        titleEditText = view.findViewById(R.id.test_title);
        startDateLayout = view.findViewById(R.id.test_start_date_layout);
        startDateEditText = view.findViewById(R.id.test_start_date);
        endDateLayout = view.findViewById(R.id.test_end_date_layout);
        endDateEditText = view.findViewById(R.id.test_end_date);
        testTypeGroup = view.findViewById(R.id.test_type_group);

        testTypeGroup.check(R.id.type_performance);

        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
            courseTitle = getArguments().getString(ARG_COURSE_TITLE);
            testId = getArguments().getInt(ARG_TEST_ID, -1);
            courseType = getArguments().getString(ARG_COURSE_TYPE);
            Log.d(TAG, "GET ARGUMENTS: The course type is " + courseType);
        } else {
            Log.e(TAG, "Arguments are null");
        }

        repository = new Repository(getActivity().getApplication());

        if (testId != -1) {
            loadTestDetails();
        }

        view.findViewById(R.id.save_button).setOnClickListener(v -> saveTest());

        view.findViewById(R.id.cancel_button).setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }


    private void loadTestDetails() {
        repository.getTestById(testId, test -> {
            if (test != null) {
                this.test = test;
                // Ensuring UI updates are run on the main thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        titleEditText.setText(test.getTitle());
                        startDateEditText.setText(test.getStartDate());
                        endDateEditText.setText(test.getEndDate());
                        if (test.getType() == TestType.PERFORMANCE_ASSESSMENT) {
                            testTypeGroup.check(R.id.type_performance);
                        } else {
                            testTypeGroup.check(R.id.type_objective);
                        }
                    });
                }
            }
        });
    }

    private void saveTest() {
        boolean isValid = true;

        Log.d(TAG, "testId: " + testId);
        if (testId == -1) {

            String title = titleEditText.getText().toString().trim();
            String startDate = startDateEditText.getText().toString().trim();
            String endDate = endDateEditText.getText().toString().trim();

            if (title.isEmpty()) {
                titleLayout.setError("Title is required");
                isValid = false;
            } else {
                titleLayout.setError(null);
            }

            if (startDate.isEmpty()) {
                startDateLayout.setError("Start date is required");
                isValid = false;
            } else {
                startDateLayout.setError(null);
            }

            if (endDate.isEmpty()) {
                endDateLayout.setError("End date is required");
                isValid = false;
            } else {
                endDateLayout.setError(null);
            }

            if (testTypeGroup.getCheckedRadioButtonId() == -1) {
                isValid = false;
            }

            if (!isValid) {
                return;
            }

            if (DateUtils.areDatesValid(startDate, endDate)) {
//                Context context = getContext();
//                DateUtils.scheduleDateNotification(context, startDate, title + " starts today");
//                DateUtils.scheduleDateNotification(context, endDate, title + " ends today");
            } else {
                Toast.makeText(getActivity(), "Invalid dates", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedTypeId = testTypeGroup.getCheckedRadioButtonId();
            if (selectedTypeId == R.id.type_performance) {
                testType = TestType.PERFORMANCE_ASSESSMENT;
            } else {
                testType = TestType.OBJECTIVE_ASSESSMENT;
            }

            Test test = new Test(title, startDate, endDate, testType, courseId, courseType);

            // Add test count to course
            repository.getCourseById(courseId, courseType, course -> {
                if (course instanceof OfflineCourse) {
                    int testNumber = ((OfflineCourse) course).getOfflineTestsNumber();
                    ((OfflineCourse) course).setOfflineTestsNumber(testNumber + 1);
                    repository.update(course);
                } else if (course instanceof OnlineCourse) {
                    int testNumber = ((OnlineCourse) course).getOnlineTestsNumber();
                    ((OnlineCourse) course).setOnlineTestsNumber(testNumber + 1);
                    repository.update(course);
                }
            });

            repository.insert(test);

            // Reload fragment if there were no courses in that term
            if(getActivity() instanceof TermsActivity) {
                repository.getCourseById(courseId, courseType, course -> {
                    boolean hasNoTests = course.isHasNoTests();
                    if (hasNoTests) {
                        // Change hasNoTests for the course
                        course.setHasNoTests(false);
                        // Remove the empty fragment from the stack to avoid showing it again
                        getParentFragmentManager().popBackStack();
                        // Reload the courses for that term
                        reloadTestFragment();
                    }
                });
            }

        } else {

            // Updating an existing test
            if (test != null) {
                test.setTitle(titleEditText.getText().toString());
                test.setStartDate(startDateEditText.getText().toString());
                test.setEndDate(endDateEditText.getText().toString());
                int selectedTypeId = testTypeGroup.getCheckedRadioButtonId();
                if (selectedTypeId == R.id.type_performance) {
                    test.setType(TestType.PERFORMANCE_ASSESSMENT);
                } else {
                    test.setType(TestType.OBJECTIVE_ASSESSMENT);
                }
                repository.update(test);
                Toast.makeText(getContext(), "Test updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
        // Go back or close the fragment
        getParentFragmentManager().popBackStack();
    }

    public int getTestId() {
        return testId;
    }
    private void reloadTestFragment() {
        if (getActivity() instanceof TermsActivity) {
            ((TermsActivity) getActivity()).showTestsFragment(courseTitle, courseId, courseType);
        }
    }
}
