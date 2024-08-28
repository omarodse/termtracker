package com.sld.termtracker.UI;

import android.content.Intent;
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
import com.sld.termtracker.Entities.CourseType;
import com.sld.termtracker.Entities.OfflineCourse;
import com.sld.termtracker.Entities.OnlineCourse;

public class CourseDetailsFragment extends Fragment {

    private static final String TAG = "courseDetails";
    private Repository repository;
    private static final String ARG_COURSE_ID = "courseId";
    private static final String ARG_COURSE_TYPE = "courseType";
    private int courseId;
    private String courseTitle;
    private String courseNoteValue;

    private String courseType;
    public interface OnCourseTitleUpdatedListener {
        void onCourseTitleUpdated(String courseTitle);
    }

    public static CourseDetailsFragment newInstance(int courseId, String courseType) {
        CourseDetailsFragment courseDetails = new CourseDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COURSE_ID, courseId);
        args.putString(ARG_COURSE_TYPE, courseType);
        courseDetails.setArguments(args);
        return courseDetails;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_details, container, false);

        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
            courseType = getArguments().getString(ARG_COURSE_TYPE);
        } else {
            Log.e(TAG, "Arguments are null");
        }

        TextView startDate = view.findViewById(R.id.course_details_start_date);
        TextView endDate = view.findViewById(R.id.course_details_end_date);
        TextView status = view.findViewById(R.id.course_details_status);
        TextView instructorName = view.findViewById(R.id.course_details_instructor_name);
        TextView instructorPhone = view.findViewById(R.id.course_details_instructor_phone);
        TextView instructorEmail = view.findViewById(R.id.course_details_instructor_email);
        TextView shareNoteButton = view.findViewById(R.id.share_note);
        TextView courseNote = view.findViewById(R.id.course_note);
        TextView courseTypeLabel = view.findViewById(R.id.type_label);
        TextView locationPlatformLabel = view.findViewById(R.id.locationPlatform);
        TextView locationPlatformValue = view.findViewById(R.id.location_platform_value);

        repository = new Repository(getActivity().getApplication());

        // Using a lambda function to implement the onCourseRetrieved abstract method
        repository.getCourseById(courseId, courseType, course -> {
            if (course != null) {
                getActivity().runOnUiThread(() -> {
                    startDate.setText(course.getStartDate());
                    endDate.setText(course.getEndDate());
                    status.setText(course.getStatus().toString());
                    instructorName.setText(course.getInstructorName());
                    instructorPhone.setText(course.getInstructorPhone());
                    instructorEmail.setText(course.getInstructorEmail());
                    courseNote.setText(course.getNote());

                    if (courseType.equals("Offline Course")) {
                        courseTypeLabel.setText(R.string.offlineCourse);
                        locationPlatformLabel.setText(R.string.location);
                        locationPlatformValue.setText(((OfflineCourse) course).getLocation());
                    } else if (courseType.equals("Online Course")) {
                        courseTypeLabel.setText(R.string.onlineCourse);
                        locationPlatformLabel.setText(R.string.platform);
                        locationPlatformValue.setText(((OnlineCourse) course).getPlatform());
                    }
                    courseNoteValue = course.getNote();

                    // If there is a note, display the share button.
                    if(courseNoteValue != null && !courseNoteValue.isEmpty()) {
                        shareNoteButton.setVisibility(View.VISIBLE);
                    }

                    shareNoteButton.setOnClickListener(v -> shareNote());

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
               ((TermsActivity) getActivity()).showTestsFragment(courseTitle, courseId, courseType);
           } else if(getActivity() instanceof MainActivity) {
               ((MainActivity) getActivity()).mainShowTestsFragment(courseTitle, courseId);
           } else if(getActivity() instanceof CoursesActivity) {
               ((CoursesActivity) getActivity()).showTestsFragment(courseTitle, courseId, courseType);
           }
        });


        return view;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    private void shareNote() {
        if (courseNoteValue != null && !courseNoteValue.isEmpty()) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, (CharSequence) courseNoteValue);

            startActivity(Intent.createChooser(shareIntent, "Share Note via"));
        }
    }
}
