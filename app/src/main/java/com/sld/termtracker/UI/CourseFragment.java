package com.sld.termtracker.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Course;

import java.util.ArrayList;

public class CourseFragment extends Fragment {
    private static final String TAG = "courseFragment";
    private static final String ARG_TERM_ID = "term_id";
    private static final String ARG_TERM_TITLE = "term_title";
    private String termTitle;
    private int termId;
    private ArrayList<Course> courseList;
    private CourseAdapter adapter;

    private TextView termTitleTextView;

    public static CourseFragment newInstance(int termId, String termTitle) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TERM_ID, termId);
        args.putString(ARG_TERM_TITLE, termTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            termId = getArguments().getInt(ARG_TERM_ID);
            termTitle = getArguments().getString(ARG_TERM_TITLE);
        } else {
            Log.e(TAG, "Arguments are null");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseList = new ArrayList<>();
        adapter = new CourseAdapter(courseList, course -> showCourseDetailFragment(course));
        recyclerView.setAdapter(adapter);

        loadCourses(termId, termTitle);

        FloatingActionButton addCourseForm = view.findViewById(R.id.course_add_FAB);

        addCourseForm.setOnClickListener(v -> {
            if (getActivity() instanceof TermsActivity) {
                ((TermsActivity) getActivity()).showAddCourseFragment(termId, termTitle);
            }
        });

        return view;
    }

    private void loadCourses(int termId, String termTitle) {
        Repository repository = new Repository(getActivity().getApplication());
        repository.getCoursesByTermId(termId, courses -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
//                    if (courses == null) {
//                        Log.e(TAG, "Courses are null");
//                        return;
//                    }
//                    Log.d(TAG, "Courses loaded: " + courses.size());
                    if (courses.isEmpty()) {

                        // Pop a stack to avoid going back to the same fragment
                        getParentFragmentManager().popBackStack();

                        if (getActivity() instanceof TermsActivity) {
                            ((TermsActivity) getActivity()).showEmptyStateFragment("No active courses", termTitle, termId);
                            Log.d(TAG, "Courses loaded: " + courses.size());
                        }
                    } else {
                        courseList.clear();
                        courseList.addAll(courses);
                        adapter.notifyDataSetChanged();
                        if(getActivity() instanceof TermsActivity) {
                            ((TermsActivity) getActivity()).setNoCourses(false);
                        }
                    }
                });
            } else {
                Log.e(TAG, "getActivity() returned null");
            }
        });
    }

    private void showCourseDetailFragment(Course course) {
        CourseDetailsFragment fragment = CourseDetailsFragment.newInstance(course.getCourseId());
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public String getTermTitle() {
        return termTitle;
    }

}
