package com.sld.termtracker.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Database.TermtrackerDatabaseBuilder;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.Test;

import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView activeCoursesRecyclerView;
    private RecyclerView upcomingTestsRecyclerView;
    private CourseAdapter coursesAdapter;
    private TestAdapter testsAdapter;
    private Repository dataRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        activeCoursesRecyclerView = view.findViewById(R.id.activeCoursesRecyclerView);
        upcomingTestsRecyclerView = view.findViewById(R.id.upcomingTestsRecyclerView);

        // Initialize repository
        dataRepository = new Repository(getActivity().getApplication());

        // Setup RecyclerView for Active Courses
//        activeCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        List<Course> activeCourses = dataRepository.getmAllCourses();
//        coursesAdapter = new CourseAdapter(activeCourses);
//        activeCoursesRecyclerView.setAdapter(coursesAdapter);

//        // Setup RecyclerView for Upcoming Tests
//        upcomingTestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        List<Test> upcomingTests = dataRepository.getmAllTests();
//        testsAdapter = new TestsAdapter(upcomingTests);
//        upcomingTestsRecyclerView.setAdapter(testsAdapter);

        return view;
    }
}
