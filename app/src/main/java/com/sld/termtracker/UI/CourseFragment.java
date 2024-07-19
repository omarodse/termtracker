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
import com.sld.termtracker.Entities.Course;

import java.util.ArrayList;

public class CourseFragment extends Fragment {
    private static final String ARG_TERM_ID = "term_id";
    private static final String ARG_TERM_TITLE = "term_title";

    private static final String TAG = "courseFragment";
    private String termTitle;
    private int termId;
    private ArrayList<Course> courseList;
    private CourseAdapter adapter;

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
        }

        courseList = new ArrayList<>();
        adapter = new CourseAdapter(courseList, getContext());
        recyclerView.setAdapter(adapter);

        loadCourses(termId, termTitle);

        return view;
    }

    private void loadCourses(int termId, String termTitle) {
        Repository repository = new Repository(getActivity().getApplication());
        repository.getCoursesByTermId(termId, courses -> {
            getActivity().runOnUiThread(() -> {
                if (courses.isEmpty()) {
                    if (getActivity() instanceof TermsActivity) {
                        ((TermsActivity) getActivity()).showEmptyStateFragment("No active courses", termTitle);
                    }
                } else {
                    courseList.clear();
                    courseList.addAll(courses);
                    adapter.notifyDataSetChanged();
                }
            });
        });
    }

}
