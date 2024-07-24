package com.sld.termtracker.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Course;

public class CourseDetailsFragment extends Fragment {

    private Repository repository;
    private static final String ARG_COURSE_ID = "courseId";

    private int courseId;

    public CourseDetailsFragment getInstance(int courseId) {
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

        TextView startDate = view.findViewById(R.id.course_details_start_date);
        TextView endDate = view.findViewById(R.id.course_details_end_date);
        TextView status = view.findViewById(R.id.course_details_status);
        TextView instructorName = view.findViewById(R.id.course_details_instructor_name);
        TextView instructorPhone = view.findViewById(R.id.course_details_instructor_phone);
        TextView instructorEmail = view.findViewById(R.id.course_details_instructor_email);

        repository = new Repository(getActivity().getApplication());
        Course course = repository.getCourse(courseId);
        startDate.setText(course.getStartDate());
        endDate.setText(course.getEndDate());
        status.setText(course.getStatus().toString());
        instructorName.setText(course.getInstructorName());
        instructorPhone.setText(course.getInstructorPhone());
        instructorEmail.setText(course.getInstructorEmail());

        return view;
    }

}
