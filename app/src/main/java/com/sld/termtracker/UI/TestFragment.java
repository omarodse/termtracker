package com.sld.termtracker.UI;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.Test;

import java.util.ArrayList;

public class TestFragment extends Fragment {
    private static final String TAG = "testFragment";
    private static final String ARG_COURSE_ID = "course_id";
    private static final String ARG_COURSE_TITLE = "term_title";
    private String courseTitle;
    private int courseId;
    private ArrayList<Test> testList;
    private TestAdapter adapter;
    private Repository repository;


    public static TestFragment newInstance(String courseTitle, int courseId) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COURSE_ID, courseId);
        args.putString(ARG_COURSE_TITLE, courseTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tests, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewTests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            courseId = getArguments().getInt(ARG_COURSE_ID);
            courseTitle = getArguments().getString(ARG_COURSE_TITLE);
        } else {
            Log.e(TAG, "Arguments are null");
        }

        repository = new Repository(getActivity().getApplication());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        testList = new ArrayList<>();
        adapter = new TestAdapter(testList, repository, this::showTestDetailFragment);
        recyclerView.setAdapter(adapter);

        loadTests(courseTitle, courseId);

        FloatingActionButton addTestForm = view.findViewById(R.id.test_add_FAB);

        Log.d(TAG, "Passing FAB Button");

        addTestForm.setOnClickListener(v -> {
            if (getActivity() instanceof TermsActivity) {
                ((TermsActivity) getActivity()).showAddTestFragment(courseId, courseTitle);
            }
        });

        return view;
    }

    private void loadTests(String itemTitle, int itemId) {
        Repository repository = new Repository(getActivity().getApplication());
        repository.getAssociatedTests(courseId, tests -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (tests.isEmpty()) {
                        // Pop a stack to avoid going back to the same fragment
                        getParentFragmentManager().popBackStack();

                        if (getActivity() instanceof TermsActivity) {
                            ((TermsActivity) getActivity()).showEmptyStateFragment("No active assessments", itemTitle, itemId);
                            Log.d(TAG, "Tests loaded: " + tests.size());
                        }
                    } else {
                        testList.clear();
                        testList.addAll(tests);
                        adapter.notifyDataSetChanged();
                        updateToolbarTitle(itemTitle + "\nAssessments");
                    }
                });
            } else {
                Log.e(TAG, "getActivity() returned null");
            }
        });
    }

    private void showTestDetailFragment(Test test) {
        TestDetailsFragment fragment = TestDetailsFragment.newInstance(test.getTestId(), courseTitle);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    private void updateToolbarTitle(String title) {
        if (getActivity() instanceof TermsActivity) {
            ((TermsActivity) getActivity()).updateToolbarTitle(title);
        }
    }
}
