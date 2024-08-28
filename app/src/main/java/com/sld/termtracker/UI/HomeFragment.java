package com.sld.termtracker.UI;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Database.TermtrackerDatabaseBuilder;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.CourseType;
import com.sld.termtracker.Entities.OfflineCourse;
import com.sld.termtracker.Entities.OnlineCourse;
import com.sld.termtracker.Entities.Test;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView coursesRecyclerView, testsRecyclerView;
    private CourseAdapter courseAdapter;
    private TestAdapter testAdapter;
    private Repository repository;
    private TextView toolbarTitle;
    private ArrayList<Course> coursesList = new ArrayList<>();
    private ArrayList<Test> testsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        coursesRecyclerView = view.findViewById(R.id.activeCoursesRecyclerView);
        testsRecyclerView = view.findViewById(R.id.upcomingTestsRecyclerView);

        // Initialize repository
        repository = new Repository(getActivity().getApplication());

        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        courseAdapter = new CourseAdapter(coursesList, repository, this::showCourseDetailFragment);
        testAdapter = new TestAdapter(testsList, repository, this::showTestDetailFragment);

        coursesRecyclerView.setAdapter(courseAdapter);
        testsRecyclerView.setAdapter(testAdapter);

        loadCourses();
        loadTests();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCourses() {
        repository.getAllCourses(courses -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    coursesList.clear();

                    // Limit to two courses if more are available
                    if (courses.size() > 2) {
                        coursesList.addAll(courses.subList(0, 2));
                    } else {
                        coursesList.addAll(courses);
                    }

                    courseAdapter.notifyDataSetChanged();

                    if (coursesList.isEmpty()) {
                        if(getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).showEmptyStateFragment();
                        }
                    }
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadTests() {
        repository.getAllTests(tests -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    testsList.clear();

                    // Limit to two courses if more are available
                    if (tests.size() > 2) {
                        testsList.addAll(tests.subList(0, 2));
                    } else {
                        testsList.addAll(tests);
                    }

                    testAdapter.notifyDataSetChanged();

//                    if (testsList.isEmpty()) {
//                        findViewById(R.id.emptyStateContainer).setVisibility(View.GONE);
//                    }
                });
            }
        });
    }

    private void showCourseDetailFragment(Course course) {
        Fragment fragment = new CourseDetailsFragment();
        if (course instanceof OfflineCourse) {
            fragment = CourseDetailsFragment.newInstance(course.getCourseId(), "Offline Course");
        } else if (course instanceof OnlineCourse) {
            fragment = CourseDetailsFragment.newInstance(course.getCourseId(), "Online Course");
        }
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showTestDetailFragment(Test test) {
        Fragment fragment = new TestDetailsFragment();
        if (test.getCourseType().equals(CourseType.OFFLINE_COURSE)) {
            fragment = TestDetailsFragment.newInstance(test.getTestId(), test.getTitle(), "Offline Course");
        } else if (test.getCourseType().equals(CourseType.ONLINE_COURSE)) {
            fragment = TestDetailsFragment.newInstance(test.getTestId(), test.getTitle(), "Online Course");
        }
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void updateToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }
}
