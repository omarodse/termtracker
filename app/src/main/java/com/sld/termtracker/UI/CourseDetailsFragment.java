package com.sld.termtracker.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Course;

public class CourseDetailsFragment extends Fragment {

    private static final String TAG = "courseDetails";
    private Repository repository;
    private static final String ARG_COURSE_ID = "courseId";
    private int courseId;
    private String courseTitle;
    public interface OnCourseTitleUpdatedListener {
        void onCourseTitleUpdated(String courseTitle);
    }

    public static CourseDetailsFragment newInstance(int courseId) {
        CourseDetailsFragment courseDetails = new CourseDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COURSE_ID, courseId);
        courseDetails.setArguments(args);
        return courseDetails;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_details, container, false);

        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
        } else {
            Log.e(TAG, "Arguments are null");
        }

        TextView startDate = view.findViewById(R.id.course_details_start_date);
        TextView endDate = view.findViewById(R.id.course_details_end_date);
        TextView status = view.findViewById(R.id.course_details_status);
        TextView instructorName = view.findViewById(R.id.course_details_instructor_name);
        TextView instructorPhone = view.findViewById(R.id.course_details_instructor_phone);
        TextView instructorEmail = view.findViewById(R.id.course_details_instructor_email);
        TextView courseNote = view.findViewById(R.id.course_note);

        repository = new Repository(getActivity().getApplication());

        // Using a lambda function to implement the onCourseRetrieved abstract method
        repository.getCourseById(courseId, course -> {
            if (course != null) {
                getActivity().runOnUiThread(() -> {
                    startDate.setText(course.getStartDate());
                    endDate.setText(course.getEndDate());
                    status.setText(course.getStatus().toString());
                    instructorName.setText(course.getInstructorName());
                    instructorPhone.setText(course.getInstructorPhone());
                    instructorEmail.setText(course.getInstructorEmail());
                    courseNote.setText(course.getNote());

                    courseTitle = course.getCourseTitle();

                    // Notify the activity that the course title has been updated
                    if (getActivity() instanceof OnCourseTitleUpdatedListener) {
                        ((OnCourseTitleUpdatedListener) getActivity()).onCourseTitleUpdated(courseTitle);
                    }
                });
            } else {
                Log.e(TAG, "Course not found");
            }
        });

        Button assessments = view.findViewById(R.id.course_details_assessments_button);

        assessments.setOnClickListener(v ->{
           if(getActivity() instanceof TermsActivity) {
               ((TermsActivity) getActivity()).showTestsFragment(courseTitle, courseId);

           }
        });


        return view;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

}
