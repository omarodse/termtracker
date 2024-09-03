package com.sld.termtracker.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.CourseType;

public class TestDetailsFragment extends Fragment {
    private static final String TAG = "testDetails";
    private Repository repository;
    private static final String ARG_TEST_ID = "testId";
    private static final String ARG_COURSE_TITLE = "courseTitle";

    private static final String ARG_COURSE_TYPE = "courseType";
    private int testId;
    private String testTitle;
    private String courseTitle;

    private String courseType;
    private RadioGroup testTypeGroup;

    public interface OnTestTitleUpdatedListener {
        void onTestTitleUpdated(String testTitle);
    }

    public static TestDetailsFragment newInstance(int testId, String courseTitle, String courseType) {
        TestDetailsFragment testDetails = new TestDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TEST_ID, testId);
        args.putString(ARG_COURSE_TITLE, courseTitle);
        args.putString(ARG_COURSE_TYPE, courseType);
        testDetails.setArguments(args);
        return testDetails;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_details, container, false);

        if (getArguments() != null) {
            testId = getArguments().getInt(ARG_TEST_ID);
            courseType = getArguments().getString(ARG_COURSE_TYPE);
            courseTitle = getArguments().getString(ARG_COURSE_TITLE);
            Log.d(TAG, "Course Title: " + testId);
        } else {
            Log.e(TAG, "Arguments are null");
        }

        TextView startDate = view.findViewById(R.id.test_details_start_date);
        TextView endDate = view.findViewById(R.id.test_details_end_date);
        TextView courseName = view.findViewById(R.id.course_title_details);
        TextView type = view.findViewById(R.id.test_details_type);

        repository = new Repository(getActivity().getApplication());

        // Lambda function to implement the onCourseRetrieved abstract method
        repository.getTestById(testId, test -> {
            if (test != null) {
                getActivity().runOnUiThread(() -> {
                    startDate.setText(test.getStartDate());
                    endDate.setText(test.getEndDate());

                    // Check if the course title is null
                    if(courseTitle == null || courseTitle.isEmpty()) {
                        Log.d(TAG, "Title is empty");
                        int courseId = test.getCourseId();
                        repository.getCourseById(courseId, courseType, course -> {
                            getActivity().runOnUiThread(() -> {
                                courseTitle = course.getCourseTitle();
                                courseName.setText(courseTitle);
                            });
                        });
                    } else {
                        courseName.setText(courseTitle);
                    }

                    type.setText(test.getType().toString());
                    testTitle = test.getTitle();

                    // Notify the activity that the course title has been updated
                    if (getActivity() instanceof TestDetailsFragment.OnTestTitleUpdatedListener) {
                        ((TestDetailsFragment.OnTestTitleUpdatedListener) getActivity()).onTestTitleUpdated(testTitle);
                    }
                });
            } else {
                Log.e(TAG, "Test not found");
            }
        });


        return view;
    }

    public String getTestTitle() {
        return testTitle;
    }
}
